package com.vsquad.iroas.config;


import com.vsquad.iroas.config.token.CustomAuthenticationEntryPoint;
import com.vsquad.iroas.config.token.CustomOncePerRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

public class SecurityConfig {

    @Bean
    public CustomOncePerRequestFilter customOncePerRequestFilter() {
        return new CustomOncePerRequestFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors()
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .csrf()
                .and()
            .formLogin()
                .disable()
            .httpBasic()
                .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
            .authorizeHttpRequests(authorize -> authorize
                            .anyRequest().permitAll()
            )
            .oauth2Login(withDefaults());

        http.addFilterBefore(customOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
