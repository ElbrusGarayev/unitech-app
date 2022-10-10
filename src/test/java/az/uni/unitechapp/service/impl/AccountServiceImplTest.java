package az.uni.unitechapp.service.impl;

import az.uni.unitechapp.dao.entity.Account;
import az.uni.unitechapp.dao.entity.Transaction;
import az.uni.unitechapp.dao.entity.User;
import az.uni.unitechapp.dao.repository.AccountRepository;
import az.uni.unitechapp.dao.repository.TransactionRepository;
import az.uni.unitechapp.enums.AccountStatus;
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
import az.uni.unitechapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    private static final String ACCOUNT_NUMBER = "1234";
    private static final String TO_ACCOUNT_NUMBER = "4321";
    private static final String IBAN = "AZ1234";
    private static final BigDecimal BALANCE = BigDecimal.TEN;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private UserService userService;

    private User user;
    private UserClaims userClaims;
    private Account account;
    private Account toAccount;
    private AccountDetailResponse accountDetailResponse;
    private TransferRequest transferRequest;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .build();
        userClaims = UserClaims.builder()
                .pin("123456A")
                .id(1L)
                .build();
        account = Account.builder()
                .id(1L)
                .accountNumber(ACCOUNT_NUMBER)
                .iban(IBAN)
                .balance(BALANCE)
                .status(AccountStatus.ACTIVE)
                .build();
        accountDetailResponse = AccountDetailResponse.builder()
                .accountNumber(ACCOUNT_NUMBER)
                .iban(IBAN)
                .balance(BALANCE)
                .build();
        toAccount = Account.builder()
                .accountNumber(TO_ACCOUNT_NUMBER)
                .balance(BALANCE)
                .build();
        transferRequest = TransferRequest.builder()
                .fromAccount(ACCOUNT_NUMBER)
                .toAccount(TO_ACCOUNT_NUMBER)
                .amount(BigDecimal.ONE)
                .build();
        transaction = Transaction.builder()
                .id(1L)
                .fromAccount(account)
                .toAccount(toAccount)
                .amount(BigDecimal.TEN)
                .build();
    }

    @Test
    void createAccount_ThenSuccess() {
        // arrange
        when(userService.getCurrentUser(any())).thenReturn(user);

        // act & assert
        accountService.createAccount(userClaims);
    }

    @Test
    void addBalance_WhenAccountExists_ThenSuccess() {
        // arrange
        when(userService.getCurrentUser(any())).thenReturn(user);
        when(accountRepository.findAccountByAccountNumberAndStatusAndUser(any(), any(), any()))
                .thenReturn(Optional.of(account));

        // act & assert
        accountService.addBalance(userClaims, ACCOUNT_NUMBER, BigDecimal.TEN);
    }

    @Test
    void addBalance_WhenAccountNotExists_ThenNotFound() {
        // arrange
        when(userService.getCurrentUser(any())).thenReturn(user);
        when(accountRepository.findAccountByAccountNumberAndStatusAndUser(any(), any(), any()))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(AccountNotFoundException.class, () -> accountService
                .addBalance(userClaims, ACCOUNT_NUMBER, BigDecimal.TEN));
    }

    @Test
    void getAccountDetail_WhenAccountExists_ThenReturnAccountInfo() {
        // arrange
        when(userService.getCurrentUser(any())).thenReturn(user);
        when(accountRepository.findAccountByAccountNumberAndStatusAndUser(any(), any(), any()))
                .thenReturn(Optional.of(account));
        when(accountMapper.toAccountDetailResponse(any())).thenReturn(accountDetailResponse);

        // act
        AccountDetailResponse accountDetail = accountService.getAccountDetail(userClaims, ACCOUNT_NUMBER);

        // assert
        assertEquals(ACCOUNT_NUMBER, accountDetail.getAccountNumber());
        assertEquals(IBAN, accountDetail.getIban());
        assertEquals(BALANCE, accountDetail.getBalance());
    }

    @Test
    void getAccountDetail_WhenAccountNotExists_ThenNotFound() {
        // arrange
        when(userService.getCurrentUser(any())).thenReturn(user);
        when(accountRepository.findAccountByAccountNumberAndStatusAndUser(any(), any(), any()))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(AccountNotFoundException.class, () -> accountService
                .getAccountDetail(userClaims, ACCOUNT_NUMBER));
    }

    @Test
    void getAccounts_WhenUserHasAccounts_ThenReturnListOfUsersAccounts() {
        // arrange
        user.setAccounts(Arrays.asList(account));
        when(userService.getCurrentUser(any())).thenReturn(user);
        when(accountMapper.toAccountDetailResponse(any())).thenReturn(accountDetailResponse);

        // act
        List<AccountDetailResponse> accounts = accountService.getAccounts(userClaims);

        // assert
        assertFalse(accounts.isEmpty());
    }

    @Test
    void getAccounts_WhenUserHasNoAccounts_ThenReturnEmptyList() {
        // arrange
        user.setAccounts(emptyList());
        when(userService.getCurrentUser(any())).thenReturn(user);

        // act
        List<AccountDetailResponse> accounts = accountService.getAccounts(userClaims);

        // assert
        assertTrue(accounts.isEmpty());
    }

    @Test
    void createTransfer_WhenFromAndToAccountSame_ThenThrowException() {
        // arrange
        transferRequest.setToAccount(ACCOUNT_NUMBER);
        when(userService.getCurrentUser(any())).thenReturn(user);

        // act & assert
        assertThrows(FromAndToAccountSameException.class, () -> accountService.createTransfer(userClaims, transferRequest));
    }

    @Test
    void createTransfer_WhenTransferAmountIsGreaterThanBalance_ThenThrowException() {
        // arrange
        transferRequest.setAmount(BigDecimal.valueOf(100));
        when(userService.getCurrentUser(any())).thenReturn(user);
        when(accountRepository.findAccountByAccountNumberAndStatusAndUser(any(), any(), any()))
                .thenReturn(Optional.of(account));

        // act & assert
        assertThrows(NotEnoughBalanceException.class, () -> accountService.createTransfer(userClaims, transferRequest));
    }

    @Test
    void createTransfer_ThenSuccess() {
        // arrange
        when(userService.getCurrentUser(any())).thenReturn(user);
        when(accountRepository.findAccountByAccountNumberAndStatusAndUser(any(), any(), any()))
                .thenReturn(Optional.of(account));
        when(accountRepository.findAccountByAccountNumberAndStatus(any(), any()))
                .thenReturn(Optional.of(toAccount));
        when(transactionRepository.save(any())).thenReturn(transaction);

        // act
        TransferResponse transfer = accountService.createTransfer(userClaims, transferRequest);

        // assert
        assertEquals(1, transfer.getTransactionId());
    }

    @Test
    void approveTransfer_WhenTransactionNotExists_ThenThrowException() {
        // arrange
        when(userService.getCurrentUser(any())).thenReturn(user);
        when(transactionRepository.findByIdAndStatusAndUser(any(), any(), any()))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(TransactionNotFoundException.class, () -> accountService.approveTransfer(userClaims, 1L));
    }

    @Test
    void approveTransfer_WhenTransactionExpired_ThenThrowException() {
        // arrange
        transaction.setExpiredAt(LocalDateTime.now().minusMinutes(1));
        when(userService.getCurrentUser(any())).thenReturn(user);
        when(transactionRepository.findByIdAndStatusAndUser(any(), any(), any()))
                .thenReturn(Optional.of(transaction));

        // act & assert
        assertThrows(TransactionExpiredException.class, () -> accountService.approveTransfer(userClaims, 1L));
    }

    @Test
    void approveTransfer_WhenTransactionNotExpired_ThenSuccess() {
        // arrange
        transaction.setExpiredAt(LocalDateTime.now().plusMinutes(1));
        when(userService.getCurrentUser(any())).thenReturn(user);
        when(transactionRepository.findByIdAndStatusAndUser(any(), any(), any()))
                .thenReturn(Optional.of(transaction));
        when(accountRepository.findAccountByAccountNumberAndStatusAndUser(any(), any(), any()))
                .thenReturn(Optional.of(account));
        when(accountRepository.findAccountByAccountNumberAndStatus(any(), any()))
                .thenReturn(Optional.of(toAccount));

        // act & assert
        accountService.approveTransfer(userClaims, 1L);
    }

}
