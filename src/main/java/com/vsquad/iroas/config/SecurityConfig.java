package com.vsquad.iroas.config;


import com.vsquad.iroas.config.token.CustomAuthenticationEntryPoint;
import com.vsquad.iroas.config.token.CustomOncePerRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
    @Order(0)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .csrf()
                .disable()
            .formLogin()
                .disable()
            .httpBasic()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .and()
            .authorizeRequests()
                .antMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v1/api-docs/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
            .oauth2Login();
//            .authorizeHttpRequests(authorize -> authorize
//                            .antMatchers("/swagger-ui").permitAll()
//                            .anyRequest().permitAll()
//            )

        http.addFilterBefore(customOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
