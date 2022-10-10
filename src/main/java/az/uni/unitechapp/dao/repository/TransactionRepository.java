package az.uni.unitechapp.dao.repository;

import az.uni.unitechapp.dao.entity.Transaction;
import az.uni.unitechapp.dao.entity.User;
import az.uni.unitechapp.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByIdAndStatusAndUser(Long transactionId, TransactionStatus status, User user);

}
