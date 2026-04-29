package io.github.siddharthbhansalidev.tickerquery.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.siddharthbhansalidev.tickerquery.enums.PriceCacheType;
import io.github.siddharthbhansalidev.tickerquery.enums.PriceCallerType;
import io.github.siddharthbhansalidev.tickerquery.interfaces.PriceCache;
import io.github.siddharthbhansalidev.tickerquery.interfaces.PriceCaller;
import io.github.siddharthbhansalidev.tickerquery.model.DateRange;
import io.github.siddharthbhansalidev.tickerquery.model.PriceData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PriceRepository {

    private final PriceCache cache;
    private final PriceCaller caller;

    public PriceRepository(
            List<PriceCache> caches, @Value("${price-cache-type:IN_MEMORY}") PriceCacheType cacheType,
            List<PriceCaller> callers, @Value("${price-caller-type:TIINGO}") PriceCallerType callerType
    ) {
        this.cache = caches.stream().filter(it -> it.getType().equals(cacheType)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Invalid catchType " + cacheType));

        this.caller = callers.stream().filter(it -> it.getType().equals(callerType)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Invalid callerType " + callerType));

        log.info("Using " + this.cache.getType() + " and " + this.caller.getType() + ".");
    }

    public Map<LocalDate, PriceData> retrieve(String ticker, DateRange range) {
        Map<LocalDate, PriceData> data = cache.get(ticker, range);

        if (data == null || data.isEmpty()) {
            data = caller.fetch(ticker, range);

            if (data != null && !data.isEmpty()) {
                cache.put(ticker, range, data);
            }
        }

        return data;
    }

}
