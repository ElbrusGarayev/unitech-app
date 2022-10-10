package az.uni.unitechapp.controller;

import az.uni.unitechapp.model.UserClaims;
import az.uni.unitechapp.model.request.TransferRequest;
import az.uni.unitechapp.model.response.AccountDetailResponse;
import az.uni.unitechapp.model.response.TransferResponse;
import az.uni.unitechapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PutMapping("/{accountNumber}/balance")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBalance(UserClaims userClaims, @PathVariable String accountNumber,
                           @RequestParam BigDecimal balance) {
        accountService.addBalance(userClaims, accountNumber, balance);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(UserClaims userClaims) {
        accountService.createAccount(userClaims);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDetailResponse> getAccountDetail(UserClaims userClaims,
                                                                  @PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountDetail(userClaims, accountNumber));
    }

    @GetMapping
    public ResponseEntity<List<AccountDetailResponse>> getAllAccounts(UserClaims userClaims) {
        return ResponseEntity.ok(accountService.getAccounts(userClaims));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> createTransfer(UserClaims userClaims,
                                                           @RequestBody TransferRequest transferRequest) {
        return ResponseEntity.ok(accountService.createTransfer(userClaims, transferRequest));
    }

    @PutMapping("/transfer/{transactionId}")
    public void approveTransfer(UserClaims userClaims, @PathVariable Long transactionId) {
        accountService.approveTransfer(userClaims, transactionId);
    }

}
