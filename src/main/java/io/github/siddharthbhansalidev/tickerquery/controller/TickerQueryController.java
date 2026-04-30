package io.github.siddharthbhansalidev.tickerquery.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.siddharthbhansalidev.tickerquery.model.TickerQueryRequest;
import io.github.siddharthbhansalidev.tickerquery.model.TickerQueryResponse;
import io.github.siddharthbhansalidev.tickerquery.service.TickerQueryService;
import jakarta.validation.Valid;

@RestController
public class TickerQueryController {

    private final TickerQueryService service;

    public TickerQueryController(TickerQueryService service) {
        this.service = service;
    }

    @PostMapping(path = "/query")
    public TickerQueryResponse getResponse(@RequestBody @Valid TickerQueryRequest request) {
        return service.computeResponse(request);
    }

}
