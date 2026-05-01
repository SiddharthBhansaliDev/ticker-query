package io.github.siddharthbhansalidev.tickerquery.repository;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Service;

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

    public PriceRepository(PriceCache cache, PriceCaller caller) {
        this.cache = cache;
        this.caller = caller;
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
