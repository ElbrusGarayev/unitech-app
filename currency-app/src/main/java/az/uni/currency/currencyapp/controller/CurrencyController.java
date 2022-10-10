package az.uni.currency.currencyapp.controller;

import az.uni.currency.currencyapp.model.response.CurrencyResponse;
import az.uni.currency.currencyapp.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<CurrencyResponse> getCurrencies() {
        return ResponseEntity.ok(currencyService.getCurrencies());
    }

}
