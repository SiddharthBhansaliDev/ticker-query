package io.github.siddharthbhansalidev.tickerquery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.siddharthbhansalidev.tickerquery.exception.PriceRetrievalException;
import io.github.siddharthbhansalidev.tickerquery.model.DateRange;
import io.github.siddharthbhansalidev.tickerquery.model.PriceData;
import io.github.siddharthbhansalidev.tickerquery.repository.PriceRepository;
import io.github.siddharthbhansalidev.tickerquery.service.PriceService;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

    private PriceRepository repository;
    private PriceService priceService;

    @BeforeEach
    void setUp() {
        repository = mock(PriceRepository.class);
        priceService = new PriceService(repository);
    }

    @Test
    void testHappyPath() {
        when(repository.retrieve(anyString(), any(DateRange.class))).thenAnswer(invocation -> {
                Map<LocalDate, PriceData> ret = new HashMap<>();
                DateRange range = invocation.getArgument(1);
                ret.put(range.startDate(), new PriceData(1000.0));
                ret.put(range.endDate(), new PriceData(2000.0));
                return ret;
        });

        Map<String, DateRange> requestMap = new HashMap<>();
        DateRange range1 = new DateRange(LocalDate.of(1000, 1, 1), LocalDate.of(1250, 1, 1));
        DateRange range2 = new DateRange(LocalDate.of(1500, 1, 1), LocalDate.of(1750, 1, 1));
        requestMap.put("TEST1", range1);
        requestMap.put("TEST2", range2);

        Map<LocalDate, PriceData> expectedPriceData1 = new HashMap<>();
        Map<LocalDate, PriceData> expectedPriceData2 = new HashMap<>();
        expectedPriceData1.put(LocalDate.of(1000, 1, 1), new PriceData(1000.0));
        expectedPriceData1.put(LocalDate.of(1250, 1, 1), new PriceData(2000.0));
        expectedPriceData2.put(LocalDate.of(1500, 1, 1), new PriceData(1000.0));
        expectedPriceData2.put(LocalDate.of(1750, 1, 1), new PriceData(2000.0));

        Map<String, Map<LocalDate, PriceData>> expectedResponse = new HashMap<>();
        expectedResponse.put("TEST1", expectedPriceData1);
        expectedResponse.put("TEST2", expectedPriceData2);

        assertEquals(expectedResponse, priceService.retrieveAll(requestMap));
    }

    @Test
    void testRepositoryError() {
        Map<String, DateRange> requestMap = new HashMap<>();
        DateRange range1 = new DateRange(LocalDate.of(1000, 1, 1), LocalDate.of(1250, 1, 1));
        requestMap.put("TEST1", range1);

        when(repository.retrieve(anyString(), any(DateRange.class))).thenThrow(new RuntimeException("Error!"));

        PriceRetrievalException ex = assertThrows(PriceRetrievalException.class, () -> {
                priceService.retrieveAll(requestMap);
        });

        assertEquals("java.util.concurrent.ExecutionException: java.lang.RuntimeException: Error!", ex.getMessage());
    }

}
