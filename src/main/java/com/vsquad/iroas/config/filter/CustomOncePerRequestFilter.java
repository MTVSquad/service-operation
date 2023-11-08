package com.vsquad.iroas.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.dto.BaseErrorCode;
import com.vsquad.iroas.aggregate.dto.ErrorReason;
import com.vsquad.iroas.aggregate.dto.ErrorResponse;
import com.vsquad.iroas.service.auth.CustomTokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor
@Component
public class CustomOncePerRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomTokenProviderService customTokenProviderService;

    private final ObjectMapper objectMapper;

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

        try{
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            ErrorReason errorReason = ErrorReason.builder()
                    .reason("에러")
                    .status(403)
                    .code(e.getMessage())
                    .build();
            responseToClient(response, getErrorResponse(errorReason, request.getRequestURL().toString()));
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }

    private ErrorResponse getErrorResponse(ErrorReason errorCode, String path) {

        return new ErrorResponse(errorCode, path);
    }

    private void responseToClient(HttpServletResponse response, ErrorResponse errorResponse)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatus());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
