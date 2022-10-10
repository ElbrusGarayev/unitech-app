package az.uni.unitechapp.dao.repository;

import az.uni.unitechapp.dao.entity.Account;
import az.uni.unitechapp.dao.entity.User;
import az.uni.unitechapp.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByAccountNumberAndStatusAndUser(String accountNumber, AccountStatus status, User user);

    Optional<Account> findAccountByAccountNumberAndStatus(String accountNumber, AccountStatus status);


}
