package com.vsquad.iroas.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.dto.ErrorReason;
import com.vsquad.iroas.aggregate.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

import static com.vsquad.iroas.config.SecurityConfig.SwaggerPatterns;

@RequiredArgsConstructor
@Component
public class AccessDeniedFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return PatternMatchUtils.simpleMatch(SwaggerPatterns, servletPath);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {

            ErrorReason errorReason = ErrorReason.builder()
                    .reason("에러")
                    .code("에러")
                    .status(403)
                    .build();

            ErrorResponse access_denied =
                    new ErrorResponse(errorReason, request.getRequestURL().toString());
            responseToClient(response, access_denied);
        }
    }

    private void responseToClient(HttpServletResponse response, ErrorResponse errorResponse)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatus());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
