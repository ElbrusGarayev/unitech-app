package az.uni.unitechapp.dao.repository;

import az.uni.unitechapp.dao.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByActiveTrue();

}
