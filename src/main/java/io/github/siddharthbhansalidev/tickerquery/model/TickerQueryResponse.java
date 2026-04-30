package io.github.siddharthbhansalidev.tickerquery.model;

import java.util.Map;

public record TickerQueryResponse(
    Map<String, TickerQueryResponseUnit> responseMap
) {
    public record TickerQueryResponseUnit(
        DateRange range,
        Double startPrice,
        Double endPrice,
        Double rateOfReturn
    ) {}
}
