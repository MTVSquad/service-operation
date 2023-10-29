package com.vsquad.iroas.config;


import com.vsquad.iroas.config.filter.SteamAuthenticationFilter;
import com.vsquad.iroas.config.filter.SteamAuthenticationProvider;
import com.vsquad.iroas.config.token.CustomAuthenticationEntryPoint;
import com.vsquad.iroas.config.token.CustomOncePerRequestFilter;
import com.vsquad.iroas.service.auth.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

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
                            "/v3/api-docs/**",  "/swagger-ui.html", "/swagger-ui/**", "/api/v1/**", "/api/v1/**/**", "/api/v1/player", "/error")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            );

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(steamAuthenticationProvider);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
