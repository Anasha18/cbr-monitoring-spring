package com.example.cbrmonitoringspring.integration;


import com.example.cbrmonitoringspring.integration.dto.CurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "cbr-api",
        url = "${business-logic.cbr-api.url}",
        configuration = CbrFeignConfig.class
)
public interface CbrFeignClient {
    @GetMapping("/daily_json.js")
    CurrencyResponse getCurrencies();
}
