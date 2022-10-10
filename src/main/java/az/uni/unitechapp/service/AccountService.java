package az.uni.unitechapp.service;

import az.uni.unitechapp.model.UserClaims;
import az.uni.unitechapp.model.request.TransferRequest;
import az.uni.unitechapp.model.response.AccountDetailResponse;
import az.uni.unitechapp.model.response.TransferResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    void addBalance(UserClaims userClaims, String accountNumber, BigDecimal balance);

    void createAccount(UserClaims userClaims);

    AccountDetailResponse getAccountDetail(UserClaims userClaims, String accountNumber);

    List<AccountDetailResponse> getAccounts(UserClaims userClaims);

    TransferResponse createTransfer(UserClaims userClaims, TransferRequest transferRequest);

    void approveTransfer(UserClaims userClaims, Long transactionId);

}
