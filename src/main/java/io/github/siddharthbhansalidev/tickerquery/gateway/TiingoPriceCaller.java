package io.github.siddharthbhansalidev.tickerquery.gateway;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import io.github.siddharthbhansalidev.tickerquery.interfaces.PriceCaller;
import io.github.siddharthbhansalidev.tickerquery.model.DateRange;
import io.github.siddharthbhansalidev.tickerquery.model.PriceData;
import io.github.siddharthbhansalidev.tickerquery.model.TiingoPrice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(name = "price-caller", havingValue = "tiingo", matchIfMissing = true)
public class TiingoPriceCaller implements PriceCaller {

    private final RestClient restClient;
    private final String apiKey;

    public TiingoPriceCaller(RestClient.Builder restBuilder, @Value("${tiingo-api-key}") String apiKey) {
        this.restClient = restBuilder.baseUrl("https://api.tiingo.com/tiingo/daily").build();
        this.apiKey = apiKey;
    }

    @Override
    public Map<LocalDate, PriceData> fetch(String ticker, DateRange range) {
        log.info("Starting Tiingo API call for " + ticker + " from " + range.startDate() + " to " + range.endDate() + ".");
        List<TiingoPrice> response = restClient.get().uri(uriBuilder -> uriBuilder.path("/" + ticker + "/prices")
                .queryParam("startDate", range.startDate())
                .queryParam("endDate", range.endDate())
                .queryParam("token", apiKey)
                .build()).retrieve().body(new ParameterizedTypeReference<>() {});

        log.info("Finished Tiingo API call for " + ticker + " from " + range.startDate() + " to " + range.endDate() + ".");
        Map<LocalDate, PriceData> data = new HashMap<>();

        for (TiingoPrice price : response) {
            data.put(price.date(), new PriceData(price.adjClose()));
        }

        return data;
    }

}
