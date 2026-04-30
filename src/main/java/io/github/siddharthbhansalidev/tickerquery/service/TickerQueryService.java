package io.github.siddharthbhansalidev.tickerquery.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.github.siddharthbhansalidev.tickerquery.model.PriceData;
import io.github.siddharthbhansalidev.tickerquery.model.TickerQueryRequest;
import io.github.siddharthbhansalidev.tickerquery.model.TickerQueryResponse;
import io.github.siddharthbhansalidev.tickerquery.model.TickerQueryResponse.TickerQueryResponseUnit;

@Service
public class TickerQueryService {

    private final PriceService priceService;
    private final CalculationService calculationService;

    public TickerQueryService(PriceService priceService, CalculationService calculationService) {
        this.priceService = priceService;
        this.calculationService = calculationService;
    }

    public TickerQueryResponse computeResponse(TickerQueryRequest request) {
        Map<String, TickerQueryResponseUnit> responseMap = new HashMap<>();
        Map<String, Map<LocalDate, PriceData>> allData = priceService.retrieveAll(request.requestMap());

        for (String ticker : allData.keySet()) {
            responseMap.put(ticker, new TickerQueryResponseUnit(
                    request.requestMap().get(ticker),
                    calculationService.calculateStartPrice(allData.get(ticker)),
                    calculationService.calculateEndPrice(allData.get(ticker)),
                    calculationService.calculateRateOfReturn(allData.get(ticker))
            ));
        }

        return new TickerQueryResponse(responseMap);
    }

}
