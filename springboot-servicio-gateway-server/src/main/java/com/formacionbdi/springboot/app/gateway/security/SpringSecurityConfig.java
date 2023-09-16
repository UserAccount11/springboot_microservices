package com.formacionbdi.springboot.app.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SpringSecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;

    public SpringSecurityConfig(JwtAuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/api/security/oauth/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/productos/listar",
                        "/api/items/listar",
                        "/api/users/rest-template",
                        "/api/items/ver/{id}/cantidad/{cantidad}",
                        "/api/productos/ver/{id}").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/users/rest-template/{id}").hasAnyRole("USER", "ADMIN")
                .pathMatchers("/api/productos/**",
                        "/api/items/**",
                        "/api/users/rest-template/**").hasRole("ADMIN")
                .anyExchange().authenticated()
                .and()
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .build();
    }

}
