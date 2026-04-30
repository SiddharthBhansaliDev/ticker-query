package io.github.siddharthbhansalidev.tickerquery.repository;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.github.siddharthbhansalidev.tickerquery.interfaces.PriceCache;
import io.github.siddharthbhansalidev.tickerquery.model.DateRange;
import io.github.siddharthbhansalidev.tickerquery.model.PriceData;

@Service
@ConditionalOnProperty(name = "price-cache", havingValue = "in-memory", matchIfMissing = true)
public class InMemoryPriceCache implements PriceCache {

    private final ConcurrentMap<String, Map<LocalDate, PriceData>> cache = new ConcurrentHashMap<>();

    @Override
    public Map<LocalDate, PriceData> get(String ticker, DateRange range) {
        return cache.get(generateKey(ticker, range));
    }

    @Override
    public void put(String ticker, DateRange range, Map<LocalDate, PriceData> data) {
        cache.put(generateKey(ticker, range), data);
    }

    @Scheduled(initialDelayString = "PT24H", fixedDelayString = "PT24H")
    private void flush() {
        cache.clear();
    }

}
