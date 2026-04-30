package io.github.siddharthbhansalidev.tickerquery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

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

}
