package az.uni.currency.currencyapp.service.impl;

import az.uni.currency.currencyapp.model.response.CurrencyResponse;
import az.uni.currency.currencyapp.model.response.RateResponse;
import az.uni.currency.currencyapp.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private static final Random RANDOM = new Random();
    private static final List<RateResponse> RATES = List.of(
            RateResponse.builder()
                    .pair("USD/AZN")
                    .rate(1.7)
                    .build(),
            RateResponse.builder()
                    .pair("AZN/USD")
                    .rate(0.59)
                    .build(),
            RateResponse.builder()
                    .pair("TL/AZN")
                    .rate(0.09)
                    .build(),
            RateResponse.builder()
                    .pair("AZN/TL")
                    .rate(10.92)
                    .build()
    );
    private static final CurrencyResponse CURRENCY_RESPONSE = CurrencyResponse.builder()
            .rates(RATES)
            .updatedAt(LocalDateTime.now())
            .build();

    @Override
    public CurrencyResponse getCurrencies() {
        return CURRENCY_RESPONSE;
    }

    @Scheduled(cron = "*/60 * * * * *")
    private void currencyUpdate() {
        log.info("starting to update currencies...");
        CURRENCY_RESPONSE.getRates().forEach(rate ->
                rate.setRate(round(RANDOM.nextDouble()))
        );
        CURRENCY_RESPONSE.setUpdatedAt(LocalDateTime.now());
        log.info("updated rates: {}", RATES);
    }

    private double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
