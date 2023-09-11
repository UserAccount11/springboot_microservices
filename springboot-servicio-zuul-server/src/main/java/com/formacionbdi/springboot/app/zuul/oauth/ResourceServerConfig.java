package com.formacionbdi.springboot.app.zuul.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${config.security.oauth.jwt.key}")
    private String jwtKey;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/api/security/oauth/token").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/productos/listar", "/api/items/listar",
//                        "/api/users/rest-template").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/productos/ver/{id}",
//                        "/api/items/ver/{id}/cantidad/{cantidad}",
//                        "/api/users/rest-template/{id}").hasAnyRole("USER", "ADMIN")
//                .antMatchers(HttpMethod.POST, "/api/productos/crear",
//                        "/api/items/crear",
//                        "/api/users/rest-template").hasRole("ADMIN")
//                .antMatchers(HttpMethod.PUT, "/api/productos/editar/{id}",
//                        "/api/items/editar/{id}",
//                        "/api/users/rest-template/{id}").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/api/productos/eliminar/{id}",
//                        "/api/items/eliminar/{id}",
//                        "/api/users/rest-template/{id}").hasRole("ADMIN");

        http.authorizeRequests()
                .antMatchers("/api/security/oauth/token").permitAll()
                .antMatchers(HttpMethod.GET, "/api/productos/listar",
                        "/api/items/listar",
                        "/api/users/rest-template").permitAll()
                .antMatchers(HttpMethod.GET, "/api/productos/ver/{id}",
                        "/api/items/ver/{id}/cantidad/{cantidad}",
                        "/api/users/rest-template/{id}").hasAnyRole("USER", "ADMIN")
                .antMatchers( "/api/productos/**",
                        "/api/items/**",
                        "/api/users/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .cors().configurationSource(corsConfigurationSource());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistration() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey(jwtKey);

        return tokenConverter;
    }

}
