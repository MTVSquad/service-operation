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

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        // form Login을 disable하고, httpBasic을 disable하고, csrf를 disable하고, sessionManagement를 stateless로 설정
//
//        http
//            .cors()
//                .disable()
//            .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api/v1.0/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//            .and()
//            .csrf()
//                .disable()
//            .formLogin()
//                .disable()
//            .httpBasic()
//                .disable()
//            .exceptionHandling()
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//            .and()
//            .oauth2Login();
//
//        http
//            .addFilterBefore(customOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class)
//            .addFilterBefore(steamAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .cors().disable()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api/v1.0/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(customOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(steamAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // 여기서 Steam 인증 필터 추가
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api/v1.0/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .openidLogin()
                .defaultSuccessUrl("/login/success")
//                .authenticationUserDetailsService(userDetailsService)
//                .attributeExchange("http://specs.openid.net/auth/2.0")
//                .attribute("openid.identity").type("http://schema.openid.net/namePerson")
//                .and()
//                .attribute("openid.steam_id").type("http://schema.steam.com/steam_id")
                .and()
//                .and()
//                .and()
                .logout()
                .permitAll();
    }
//    @Bean
//    public SteamAuthenticationFilter steamAuthenticationFilter() throws Exception {
//        SteamAuthenticationFilter filter = new SteamAuthenticationFilter(new AntPathRequestMatcher("/login/steam", "POST"));
//        filter.setAuthenticationManager(authenticationManagerBean());
//        return filter;
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(steamAuthenticationProvider); // Steam 인증 공급자 추가
//    }
}
