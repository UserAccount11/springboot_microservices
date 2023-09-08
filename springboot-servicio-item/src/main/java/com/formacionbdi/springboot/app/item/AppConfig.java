package com.formacionbdi.springboot.app.item;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AppConfig {

	@LoadBalanced
	@Bean("restTemplate")
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id ->
				new Resilience4JConfigBuilder(id)
						.circuitBreakerConfig(CircuitBreakerConfig.custom()
								.slidingWindowSize(10) // default 100
								.failureRateThreshold(50) // default 50
								.waitDurationInOpenState(Duration.ofSeconds(10L)) // default 60s
								.permittedNumberOfCallsInHalfOpenState(5) // default 10
								.slowCallRateThreshold(50) // default 100
								.slowCallDurationThreshold(Duration.ofSeconds(2L))
								.build())
						.timeLimiterConfig(TimeLimiterConfig.custom()
								.timeoutDuration(Duration.ofSeconds(2L))
								.build()) // default 1s
						.build());
	}
}
