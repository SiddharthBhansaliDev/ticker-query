package io.github.siddharthbhansalidev.tickerquery.repository;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.github.siddharthbhansalidev.tickerquery.enums.PriceCacheType;
import io.github.siddharthbhansalidev.tickerquery.interfaces.PriceCache;
import io.github.siddharthbhansalidev.tickerquery.model.DateRange;
import io.github.siddharthbhansalidev.tickerquery.model.PriceData;

@Service
public class InMemoryPriceCache implements PriceCache {

    private final ConcurrentMap<String, Map<LocalDate, PriceData>> cache = new ConcurrentHashMap<>();

    public PriceCacheType getType() {
        return PriceCacheType.IN_MEMORY;
    }

    public Map<LocalDate, PriceData> get(String ticker, DateRange range) {
        return cache.get(generateKey(ticker, range));
    }

    public void put(String ticker, DateRange range, Map<LocalDate, PriceData> data) {
        cache.put(generateKey(ticker, range), data);
    }

    @Scheduled(initialDelayString = "PT1H", fixedDelayString = "PT1H")
    private void flush() {
        cache.clear();
    }

}
