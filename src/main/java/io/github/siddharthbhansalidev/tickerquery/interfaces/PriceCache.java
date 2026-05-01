package io.github.siddharthbhansalidev.tickerquery.interfaces;

import java.time.LocalDate;
import java.util.Map;

import io.github.siddharthbhansalidev.tickerquery.enums.PriceCacheType;
import io.github.siddharthbhansalidev.tickerquery.model.DateRange;
import io.github.siddharthbhansalidev.tickerquery.model.PriceData;

public interface PriceCache {

    default public String generateKey(String ticker, DateRange range) {
        return ticker + "/" + range.startDate() + "/" + range.endDate();
    }

    public PriceCacheType getType();

    public Map<LocalDate, PriceData> get(String ticker, DateRange range);

    public void put(String ticker, DateRange range, Map<LocalDate, PriceData> data);

}
