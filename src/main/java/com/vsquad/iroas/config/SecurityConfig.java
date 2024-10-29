package com.vsquad.iroas.config;


import com.vsquad.iroas.config.filter.SteamAuthenticationProvider;
import com.vsquad.iroas.config.token.CustomOncePerRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SteamAuthenticationProvider steamAuthenticationProvider;

    @Bean
    public CustomOncePerRequestFilter customOncePerRequestFilter() {
        return new CustomOncePerRequestFilter();
    }

    public SecurityConfig(SteamAuthenticationProvider steamAuthenticationProvider) {
        this.steamAuthenticationProvider = steamAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(requests -> requests
                    .antMatchers("/", "steam/login", "steam/login/redirect", "steam/failed", "/swagger",
                            "/v3/api-docs/**",  "/swagger-ui.html", "/swagger-ui/**", "/api/v1/player", "/api/v1/player/login", "/error")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            );

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(steamAuthenticationProvider);

        // JWT 인증 필터 추가
        http.addFilterBefore(customOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
