package io.github.siddharthbhansalidev.tickerquery.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.github.siddharthbhansalidev.tickerquery.model.PriceData;

@Service
public class CalculationService {

    public double calculateStartPrice(Map<LocalDate, PriceData> data) {
        if (data == null || data.isEmpty()) {
            return 0.0;
        }

        LocalDate startDate = data.keySet().stream().min(Comparator.naturalOrder()).get();
        return data.get(startDate).adjClose();        
    }

    public double calculateEndPrice(Map<LocalDate, PriceData> data) {
        if (data == null || data.isEmpty()) {
            return 0.0;
        }

        LocalDate endDate = data.keySet().stream().max(Comparator.naturalOrder()).get();
        return data.get(endDate).adjClose();        
    }

    public double calculateRateOfReturn(Map<LocalDate, PriceData> data) {
        return (calculateEndPrice(data) / calculateStartPrice(data)) - 1.0;
    }

}
