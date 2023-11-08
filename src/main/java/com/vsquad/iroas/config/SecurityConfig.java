package com.vsquad.iroas.config;


import com.vsquad.iroas.config.common.SpringEnvironmentHelper;
import com.vsquad.iroas.config.filter.AccessDeniedFilter;
import com.vsquad.iroas.config.filter.SteamAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SteamAuthenticationProvider steamAuthenticationProvider;

    private final FilterConfig filterConfig;

    @Value("${swagger.user}")
    private String swaggerUser;

    @Value("${swagger.password}")
    private String swaggerPassword;

    private final SpringEnvironmentHelper springEnvironmentHelper;

    private final AccessDeniedFilter accessDeniedFilter;

//    @Bean
//    public CustomOncePerRequestFilter customOncePerRequestFilter() {
//        return new CustomOncePerRequestFilter();
//    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    .antMatchers(
                            "/images/**",
                            "/js/**",
                            "/css/**",
                            "favicon.ico"
                            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if (springEnvironmentHelper.isProdAndDevProfile()) {
            http
                .authorizeRequests()
                        .mvcMatchers("/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs")
                        .hasRole("SWAGGER")
                        .and()
                        .httpBasic();
        }

        http.csrf().disable()
            .authorizeRequests(requests -> requests
                    .antMatchers("/", "steam/login", "steam/login/redirect", "steam/failed",
                            "/v3/api-docs/**", "/api/v1/player", "/api/v1/player/login", "/error"
                    ,"/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs"
                    )
                    .permitAll()
                    .anyRequest()
                    .hasRole("PLAYER")
            );
        http.apply(filterConfig);

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(steamAuthenticationProvider);

        // JWT 인증 필터 추가
//        http.addFilterBefore(customOncePerRequestFilter(), BasicAuthenticationFilter.class);
//        http.addFilterBefore(customOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(customOncePerRequestFilter(), FilterSecurityInterceptor.class);
//        http.addFilterBefore(accessDeniedFilter, FilterSecurityInterceptor.class);

        return http.build();
    }

    // SwaggerPatterns
    public static final String[] SwaggerPatterns = {
            "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs", "/favicon.ico"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user =
                User.withUsername(swaggerUser)
                        .password(passwordEncoder().encode(swaggerPassword))
                        .roles("SWAGGER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
