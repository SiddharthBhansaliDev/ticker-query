package io.github.siddharthbhansalidev.tickerquery.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import io.github.siddharthbhansalidev.tickerquery.model.DateRange;
import io.github.siddharthbhansalidev.tickerquery.model.PriceData;
import io.github.siddharthbhansalidev.tickerquery.repository.PriceRepository;

@Service
public class PriceService {
    
    private final PriceRepository repository;

    public PriceService(PriceRepository repository) {
        this.repository = repository;
    }

    public Map<String, Map<LocalDate, PriceData>> retrieveAll(Map<String, DateRange> requestMap) {
        Map<String, Map<LocalDate, PriceData>> allData = new HashMap<>();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            ConcurrentMap<String, Future<Map<LocalDate, PriceData>>> taskMap = new ConcurrentHashMap<>();

            for (String ticker : requestMap.keySet()) {
                taskMap.put(ticker, executor.submit(() -> repository.retrieve(ticker, requestMap.get(ticker))));
            }

            for (Map.Entry<String, Future<Map<LocalDate, PriceData>>> taskMapEntry : taskMap.entrySet()) {
                allData.put(taskMapEntry.getKey(), taskMapEntry.getValue().get());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return allData;
    }

}
