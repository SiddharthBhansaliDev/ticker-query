package io.github.siddharthbhansalidev.tickerquery.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TiingoPrice(
    @JsonProperty("date")
    LocalDate date,
    
    @JsonProperty("adjClose")
    Double adjClose
) {}
