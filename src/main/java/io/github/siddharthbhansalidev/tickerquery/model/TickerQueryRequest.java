package io.github.siddharthbhansalidev.tickerquery.model;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TickerQueryRequest(
    @Schema(example =
            """
            {
                "AAPL": {"startDate": "2023-01-20", "endDate": "2023-09-25"},
                "TSLA": {"startDate": "2024-06-05", "endDate": "2025-03-30"},
                "NVDA": {"startDate": "2025-08-15", "endDate": "2026-01-10"},
                "MSFT": {"startDate": "2026-01-23", "endDate": "2026-04-17"}
            }
            """
    )
    @NotNull
    @Size(min = 1, max = 10)
    Map<@NotBlank String, @Valid DateRange> requestMap
) {
    public TickerQueryRequest {
        if (requestMap != null) {
            requestMap = Map.copyOf(requestMap);
        }
    }
}
