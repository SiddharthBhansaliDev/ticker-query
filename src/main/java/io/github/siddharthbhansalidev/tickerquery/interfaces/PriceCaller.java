package io.github.siddharthbhansalidev.tickerquery.interfaces;

import java.time.LocalDate;
import java.util.Map;

import io.github.siddharthbhansalidev.tickerquery.enums.PriceCallerType;
import io.github.siddharthbhansalidev.tickerquery.model.DateRange;
import io.github.siddharthbhansalidev.tickerquery.model.PriceData;

public interface PriceCaller {

    public PriceCallerType getType();

    public Map<LocalDate, PriceData> fetch(String ticker, DateRange range);

}
