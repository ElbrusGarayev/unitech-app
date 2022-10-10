package az.uni.unitechapp.service.impl;

import az.uni.unitechapp.dao.entity.Account;
import az.uni.unitechapp.dao.entity.Transaction;
import az.uni.unitechapp.dao.entity.User;
import az.uni.unitechapp.dao.repository.AccountRepository;
import az.uni.unitechapp.dao.repository.TransactionRepository;
import az.uni.unitechapp.exception.AccountNotFoundException;
import az.uni.unitechapp.exception.FromAndToAccountSameException;
import az.uni.unitechapp.exception.NotEnoughBalanceException;
import az.uni.unitechapp.exception.TransactionExpiredException;
import az.uni.unitechapp.exception.TransactionNotFoundException;
import az.uni.unitechapp.mapper.AccountMapper;
import az.uni.unitechapp.model.UserClaims;
import az.uni.unitechapp.model.request.TransferRequest;
import az.uni.unitechapp.model.response.AccountDetailResponse;
import az.uni.unitechapp.model.response.TransferResponse;
import az.uni.unitechapp.service.AccountService;
import az.uni.unitechapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static az.uni.unitechapp.enums.AccountStatus.ACTIVE;
import static az.uni.unitechapp.enums.TransactionStatus.APPROVED;
import static az.uni.unitechapp.enums.TransactionStatus.PENDING_APPROVAL;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final String IBAN_PREFIX = "AZAZ0IBB";
    private static final String ACCOUNT_NUMBER_PREFIX = "12345098767";

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final UserService userService;

    @Override
    public void createAccount(UserClaims userClaims) {
        User currentUser = userService.getCurrentUser(userClaims.getId());
        String accountNumber = generateAccountNumber();
        Account account = Account.builder()
                .status(ACTIVE)
                .user(currentUser)
                .accountNumber(accountNumber)
                .iban(IBAN_PREFIX.concat(accountNumber))
                .balance(BigDecimal.ZERO)
                .build();
        accountRepository.save(account);
    }

    @Override
    public void addBalance(UserClaims userClaims, String accountNumber, BigDecimal balance) {
        User currentUser = userService.getCurrentUser(userClaims.getId());
        Account account = accountRepository.findAccountByAccountNumberAndStatusAndUser(accountNumber, ACTIVE, currentUser)
                .orElseThrow(AccountNotFoundException::new);
        account.setBalance(balance);
        accountRepository.save(account);
    }

    @Override
    public AccountDetailResponse getAccountDetail(UserClaims userClaims, String accountNumber) {
        User currentUser = userService.getCurrentUser(userClaims.getId());
        Account account = accountRepository
                .findAccountByAccountNumberAndStatusAndUser(accountNumber, ACTIVE, currentUser)
                .orElseThrow(AccountNotFoundException::new);
        return accountMapper.toAccountDetailResponse(account);
    }

    @Override
    public List<AccountDetailResponse> getAccounts(UserClaims userClaims) {
        User currentUser = userService.getCurrentUser(userClaims.getId());
        return currentUser.getAccounts()
                .stream()
                .filter(account -> account.getStatus().equals(ACTIVE))
                .map(accountMapper::toAccountDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransferResponse createTransfer(UserClaims userClaims, TransferRequest transferRequest) {
        User currentUser = userService.getCurrentUser(userClaims.getId());
        checkTransferAccounts(transferRequest);
        Account fromAccount = accountRepository
                .findAccountByAccountNumberAndStatusAndUser(transferRequest.getFromAccount(), ACTIVE, currentUser)
                .orElseThrow(AccountNotFoundException::new);
        checkBalance(fromAccount.getBalance(), transferRequest.getAmount());
        Account toAccount = accountRepository
                .findAccountByAccountNumberAndStatus(transferRequest.getToAccount(), ACTIVE)
                .orElseThrow(AccountNotFoundException::new);
        Long transactionId = createTransaction(fromAccount, toAccount, transferRequest, currentUser);
        return TransferResponse.builder()
                .transactionId(transactionId)
                .build();
    }

    @Override
    public void approveTransfer(UserClaims userClaims, Long transactionId) {
        User currentUser = userService.getCurrentUser(userClaims.getId());
        Transaction transaction = transactionRepository
                .findByIdAndStatusAndUser(transactionId, PENDING_APPROVAL, currentUser)
                .orElseThrow(TransactionNotFoundException::new);
        checkTransactionExpire(transaction);
        transaction.setStatus(APPROVED);
        Account fromAccount = updateFromAccountInfo(transaction, currentUser);
        Account toAccount = updateToAccountInfo(transaction);
        accountRepository.saveAll(List.of(fromAccount, toAccount));
        transactionRepository.save(transaction);
    }

    private Long createTransaction(Account fromAccount, Account toAccount, TransferRequest transferRequest, User user) {
        Transaction transaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(transferRequest.getAmount())
                .status(PENDING_APPROVAL)
                .expiredAt(LocalDateTime.now().plusMinutes(1))
                .user(user)
                .build();
        return transactionRepository.save(transaction).getId();
    }

    private String generateAccountNumber() {
        return ACCOUNT_NUMBER_PREFIX.concat(String.valueOf(new Random().nextInt(999999999)));
    }

    private void checkTransactionExpire(Transaction transaction) {
        if (transaction.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new TransactionExpiredException();
        }
    }

    private void checkTransferAccounts(TransferRequest transferRequest) {
        if (transferRequest.getToAccount().equals(transferRequest.getFromAccount())) {
            throw new FromAndToAccountSameException();
        }
    }

    private void checkBalance(BigDecimal balance, BigDecimal amount) {
        if (amount.compareTo(balance) > 0) {
            throw new NotEnoughBalanceException();
        }
    }

    private Account updateFromAccountInfo(Transaction transaction, User currentUser) {
        Account fromAccount = accountRepository
                .findAccountByAccountNumberAndStatusAndUser(
                        transaction.getFromAccount().getAccountNumber(), ACTIVE, currentUser)
                .orElseThrow(AccountNotFoundException::new);
        transaction.setFromAccountBalanceBeforeTransaction(fromAccount.getBalance());
        BigDecimal decreasedBalance = fromAccount.getBalance().subtract(transaction.getAmount());
        fromAccount.setBalance(decreasedBalance);
        transaction.setFromAccountBalanceAfterTransaction(decreasedBalance);
        return fromAccount;
    }

    private Account updateToAccountInfo(Transaction transaction) {
        Account toAccount = accountRepository
                .findAccountByAccountNumberAndStatus(transaction.getToAccount().getAccountNumber(), ACTIVE)
                .orElseThrow(AccountNotFoundException::new);
        transaction.setToAccountBalanceBeforeTransaction(toAccount.getBalance());
        BigDecimal increasedBalance = toAccount.getBalance().add(transaction.getAmount());
        toAccount.setBalance(increasedBalance);
        transaction.setToAccountBalanceAfterTransaction(increasedBalance);
        return toAccount;
    }

}
