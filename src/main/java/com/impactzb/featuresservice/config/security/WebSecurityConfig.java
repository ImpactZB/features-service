package com.impactzb.featuresservice.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
public class WebSecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/v3/api-docs", "/v3/api-docs.yaml", "/swagger-ui.html").permitAll()
                .requestMatchers(/*HttpMethod.POST,*/"/feature").authenticated()
                .requestMatchers(/*HttpMethod.POST,*/"/user/feature").authenticated()
                .anyRequest().authenticated())
                        .httpBasic();
        return http.build();
    }
}