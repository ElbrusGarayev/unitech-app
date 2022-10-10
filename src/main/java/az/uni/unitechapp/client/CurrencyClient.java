package az.uni.unitechapp.client;

import az.uni.unitechapp.model.response.CurrencyClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "currencyAppUrl", url = "${feign.client.config.currencyAppUrl.url}")
public interface CurrencyClient {

    @GetMapping("/currencies")
    ResponseEntity<CurrencyClientResponse> getCurrencies();
}
