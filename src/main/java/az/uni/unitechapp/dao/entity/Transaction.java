package az.uni.unitechapp.dao.entity;

import az.uni.unitechapp.enums.TransactionStatus;
import az.uni.unitechapp.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private BigDecimal fromAccountBalanceBeforeTransaction;
    private BigDecimal fromAccountBalanceAfterTransaction;
    private BigDecimal toAccountBalanceBeforeTransaction;
    private BigDecimal toAccountBalanceAfterTransaction;
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    private Account fromAccount;

    @ManyToOne
    private Account toAccount;

    @ManyToOne
    private User user;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
