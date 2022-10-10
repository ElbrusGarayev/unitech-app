package az.uni.unitechapp.client.impl;


import az.uni.unitechapp.client.CurrencyClient;
import az.uni.unitechapp.exception.CurrencyNotFoundException;
import az.uni.unitechapp.exception.InternalServerException;
import az.uni.unitechapp.model.response.CurrencyClientResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyClientImpl {

    private final CurrencyClient currencyClient;

    public CurrencyClientResponse getCurrencies() {
        try {
            return currencyClient.getCurrencies().getBody();
        } catch (FeignException.NotFound ex) {
            log.error("Error from currency client: {}", ex.getMessage());
            throw new CurrencyNotFoundException();
        } catch (Exception ex) {
            log.error("Error from currency client: {}", ex.getMessage());
            throw new InternalServerException(ex);
        }
    }
}
