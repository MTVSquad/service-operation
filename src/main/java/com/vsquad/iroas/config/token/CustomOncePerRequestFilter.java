package com.vsquad.iroas.config.token;

import com.vsquad.iroas.service.auth.CustomTokenProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomOncePerRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomTokenProviderService customTokenProviderService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);

        if(StringUtils.hasText(jwt) && customTokenProviderService.getAuthentication(jwt).isAuthenticated()) {
            UsernamePasswordAuthenticationToken authentication = null;

            try {
                authentication = customTokenProviderService.getAuthenticationById(jwt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {

            if(bearerToken.equals("Bearer")) {
                throw new StringIndexOutOfBoundsException("Bearer 이후 토큰 없음");
            }

            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }
}
