package az.uni.unitechapp.service.impl;

import az.uni.unitechapp.client.impl.CurrencyClientImpl;
import az.uni.unitechapp.dao.entity.Currency;
import az.uni.unitechapp.dao.repository.CurrencyRepository;
import az.uni.unitechapp.model.response.CurrencyClientResponse;
import az.uni.unitechapp.model.response.CurrencyResponse;
import az.uni.unitechapp.model.response.RateResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceImplTest {

    private static final String RATES = "AZN/USD=0.59,USD/AZN=1.7";

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Mock
    private CurrencyClientImpl currencyClient;

    @Mock
    private CurrencyRepository currencyRepository;

    private Currency currency;
    private RateResponse rateResponse;
    private CurrencyClientResponse currencyClientResponse;

    @BeforeEach
    void setUp() {
        currency = Currency.builder()
                .rates(RATES)
                .updatedAt(LocalDateTime.now())
                .build();
        rateResponse = RateResponse.builder()
                .pair("AZN/USD")
                .rate(0.59)
                .build();
        currencyClientResponse = CurrencyClientResponse.builder()
                .rates(List.of(rateResponse))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getCurrencies_WhenActiveCurrencyExists_ThenThrowException() {
        // arrange
        when(currencyRepository.findByActiveTrue()).thenReturn(Optional.empty());
        when(currencyClient.getCurrencies()).thenReturn(currencyClientResponse);

        // act
        CurrencyResponse currencies = currencyService.getCurrencies();

        // assert
        Assertions.assertFalse(currencies.getRates().isEmpty());
    }

    @Test
    void getCurrencies_WhenActiveCurrencyExists_ThenReturnCurrency() {
        // arrange
        when(currencyRepository.findByActiveTrue()).thenReturn(Optional.of(currency));

        // act
        CurrencyResponse currencyResponse = currencyService.getCurrencies();

        // arrange
        List<String> ratesAsList = Arrays.stream(RATES.split(",")).collect(Collectors.toList());
        Assertions.assertEquals(ratesAsList, currencyResponse.getRates());
    }

    @Test
    void currencyUpdate_WhenActiveCurrencyNotExists_ThenGetFromCurrencyService() {
        // arrange
        when(currencyRepository.findByActiveTrue()).thenReturn(Optional.empty());
        when(currencyClient.getCurrencies()).thenReturn(currencyClientResponse);

        // act & assert
        currencyService.currencyUpdate();
    }

    @Test
    void currencyUpdate_WhenActiveCurrencyNotExists_ThenGetFrom() {
        // arrange
        currency.setUpdatedAt(LocalDateTime.now().minusMinutes(2));
        when(currencyRepository.findByActiveTrue()).thenReturn(Optional.of(currency));
        when(currencyClient.getCurrencies()).thenReturn(currencyClientResponse);

        // act & assert
        currencyService.currencyUpdate();
    }

}
