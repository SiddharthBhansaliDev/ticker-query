package io.github.siddharthbhansalidev.tickerquery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@EnableScheduling
@SpringBootApplication
public class TickerQueryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TickerQueryApplication.class, args);
	}

	@Bean
	RestClient.Builder restBuilder() {
		return RestClient.builder();
	}

	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}

}
