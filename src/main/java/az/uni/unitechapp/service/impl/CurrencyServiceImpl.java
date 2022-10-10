package az.uni.unitechapp.service.impl;

import az.uni.unitechapp.client.impl.CurrencyClientImpl;
import az.uni.unitechapp.dao.entity.Currency;
import az.uni.unitechapp.dao.repository.CurrencyRepository;
import az.uni.unitechapp.model.response.CurrencyClientResponse;
import az.uni.unitechapp.model.response.CurrencyResponse;
import az.uni.unitechapp.model.response.RateResponse;
import az.uni.unitechapp.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyClientImpl currencyClient;
    private final CurrencyRepository currencyRepository;

    @Override
    public CurrencyResponse getCurrencies() {
        Optional<Currency> optionalCurrency = currencyRepository.findByActiveTrue();
        List<String> rates;
        LocalDateTime updatedAt;
        if (optionalCurrency.isPresent()) {
            Currency currency = optionalCurrency.get();
            rates = Arrays.stream(currency.getRates().split(","))
                    .collect(Collectors.toList());
            updatedAt = currency.getUpdatedAt();
        } else {
            CurrencyClientResponse currencyClientResponse = currencyClient.getCurrencies();
            rates = currencyClientResponse.getRates().stream()
                    .map(rateResponse -> String.format("%s=%s", rateResponse.getPair(), rateResponse.getRate()))
                    .collect(Collectors.toList());
            updatedAt = currencyClientResponse.getUpdatedAt();
        }
        return CurrencyResponse.builder()
                .rates(rates)
                .updatedAt(updatedAt)
                .build();
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void currencyUpdate() {
        log.info("starting to check currencies from 3rd party...");
        Optional<Currency> optionalCurrency = currencyRepository.findByActiveTrue();
        if (optionalCurrency.isPresent()) {
            Currency currency = optionalCurrency.get();
            long minuteDiff = ChronoUnit.MINUTES.between(currency.getUpdatedAt(), LocalDateTime.now());
            if (minuteDiff == 1 || minuteDiff > 1) {
                currency.setActive(false);
                Currency newCurrency = buildNewCurrency();
                currencyRepository.saveAll(List.of(currency, newCurrency));
                log.info("new currencies: {}", newCurrency);
            }
        } else {
            Currency newCurrency = buildNewCurrency();
            currencyRepository.save(newCurrency);
            log.info("new currencies: {}", newCurrency);
        }
    }

    private Currency buildNewCurrency() {
        CurrencyClientResponse currencyClientResponse = currencyClient.getCurrencies();
        return Currency.builder()
                .rates(buildRates(currencyClientResponse.getRates()))
                .active(true)
                .updatedAt(currencyClientResponse.getUpdatedAt())
                .build();
    }

    private String buildRates(List<RateResponse> rates) {
        List<String> ratesList = rates.stream()
                .map(rateResponse -> String.format("%s=%s", rateResponse.getPair(), rateResponse.getRate()))
                .collect(Collectors.toList());
        return String.join(",", ratesList);
    }

}
