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
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SteamAuthenticationProvider steamAuthenticationProvider;

    @Bean
    public CustomOncePerRequestFilter customOncePerRequestFilter() {
        return new CustomOncePerRequestFilter();
    }

    private final CustomUserDetailService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .authorizeRequests()
                .antMatchers("/", "/login", "/swagger", "/v3/api-docs/**",  "/swagger-ui.html", "/swagger-ui/**", "/api/v1/**", "/api/v1/**/**", "/error")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .openidLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .logout()
                .permitAll();
    }


}
