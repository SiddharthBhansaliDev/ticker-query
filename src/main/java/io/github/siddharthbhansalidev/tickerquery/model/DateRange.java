package io.github.siddharthbhansalidev.tickerquery.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record DateRange(
    @NotNull
    LocalDate startDate,

    @Past
    @NotNull
    LocalDate endDate
) {
    public DateRange {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            startDate = endDate;
        }
    }
}
