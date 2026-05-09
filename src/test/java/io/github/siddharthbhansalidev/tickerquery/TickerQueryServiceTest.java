package io.github.siddharthbhansalidev.tickerquery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.siddharthbhansalidev.tickerquery.model.DateRange;
import io.github.siddharthbhansalidev.tickerquery.model.PriceData;
import io.github.siddharthbhansalidev.tickerquery.model.TickerQueryRequest;
import io.github.siddharthbhansalidev.tickerquery.model.TickerQueryResponse;
import io.github.siddharthbhansalidev.tickerquery.model.TickerQueryResponse.TickerQueryResponseUnit;
import io.github.siddharthbhansalidev.tickerquery.service.CalculationService;
import io.github.siddharthbhansalidev.tickerquery.service.PriceService;
import io.github.siddharthbhansalidev.tickerquery.service.TickerQueryService;

@ExtendWith(MockitoExtension.class)
public class TickerQueryServiceTest {

    private PriceService priceService;
    private CalculationService calculationService;
    private TickerQueryService tickerQueryService;

    @BeforeEach
    void setUp() {
        priceService = mock(PriceService.class);
        calculationService = new CalculationService(); // For now, this just does deterministic math. No need to mock it.
        tickerQueryService = new TickerQueryService(priceService, calculationService);
    }

    @Test
    void testHappyPath() {
        Map<LocalDate, PriceData> test1PriceData = new HashMap<>();
        test1PriceData.put(LocalDate.of(1000, 1, 1), new PriceData(200.0));
        test1PriceData.put(LocalDate.of(1250, 1, 1), new PriceData(500.0));

        Map<LocalDate, PriceData> test2PriceData = new HashMap<>();
        test2PriceData.put(LocalDate.of(1500, 1, 1), new PriceData(400.0));
        test2PriceData.put(LocalDate.of(1750, 1, 1), new PriceData(1200.0));

        Map<String, Map<LocalDate, PriceData>> priceData = new HashMap<>();
        priceData.put("TEST1", test1PriceData);
        priceData.put("TEST2", test2PriceData);

        when(priceService.retrieveAll(anyMap())).thenReturn(priceData);

        Map<String, DateRange> requestMap = new HashMap<>();
        DateRange range1 = new DateRange(LocalDate.of(1000, 1, 1), LocalDate.of(1250, 1, 1));
        DateRange range2 = new DateRange(LocalDate.of(1500, 1, 1), LocalDate.of(1750, 1, 1));
        requestMap.put("TEST1", range1);
        requestMap.put("TEST2", range2);

        Map<String, TickerQueryResponseUnit> expectedResponseMap = new HashMap<>();
        expectedResponseMap.put("TEST1", new TickerQueryResponseUnit(range1, 200.0, 500.0, 1.5));
        expectedResponseMap.put("TEST2", new TickerQueryResponseUnit(range2, 400.0, 1200.0, 2.0));

        assertEquals(
                new TickerQueryResponse(expectedResponseMap),
                tickerQueryService.computeResponse(new TickerQueryRequest(requestMap))
        );
    }

    @Test
    void testPriceServiceError() {
        when(priceService.retrieveAll(anyMap())).thenThrow(new RuntimeException("Error!"));

        Map<String, DateRange> requestMap = new HashMap<>();
        DateRange range1 = new DateRange(LocalDate.of(1000, 1, 1), LocalDate.of(1250, 1, 1));
        requestMap.put("TEST1", range1);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> { tickerQueryService.computeResponse(
                new TickerQueryRequest(requestMap)); 
        });
        
        assertEquals("Error!", ex.getMessage());
    }

}
