package com.learning.gymback.security.config;

import com.learning.gymback.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequest -> {
                    httpRequest.requestMatchers("/v1/auth/register", "/v1/auth/login")
                            .permitAll();
                    httpRequest.requestMatchers(HttpMethod.GET, "/v1/slots")
                            .permitAll();
                    httpRequest.requestMatchers(HttpMethod.DELETE, "/v1/slots")
                            .hasAnyAuthority("SYS_ADMIN", "CLUB_ADMIN", "TRAINER");
                    httpRequest.requestMatchers(HttpMethod.POST, "/v1/slots")
                            .hasAnyAuthority("SYS_ADMIN", "CLUB_ADMIN", "TRAINER");
                    httpRequest.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider);
        return httpSecurity.build();

    }
}
