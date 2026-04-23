package com.example.cbrmonitoringspring.integration;


import com.example.cbrmonitoringspring.integration.dto.CurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "cbr-api",
        url = "${business-logic.cbr-api.url}"
)
public interface CbrFeignClient {
    @GetMapping(value = "/daily_json.js", produces = { "application/javascript" })
    CurrencyResponse getCurrencies();
}
