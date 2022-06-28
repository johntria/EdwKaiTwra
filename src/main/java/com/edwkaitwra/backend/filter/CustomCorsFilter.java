package com.edwkaitwra.backend.filter;

import com.edwkaitwra.backend.config.exception.security.CorsNotExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomCorsFilter implements Filter {
    private final String allowedCors;

    public CustomCorsFilter(String allowedCors) {
        this.allowedCors = allowedCors;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String corsHeader = httpServletRequest.getHeader(ORIGIN);
        try {
            if (corsHeader == null || corsHeader.trim().isEmpty()) {
                log.error("Cors is empty or blank");
                throw new CorsNotExistsException("Cors Not Exists");
            }
            if (!corsHeader.trim().contains(allowedCors)) {
                log.error("Cors not contains origin");
                throw new CorsNotExistsException("Cors Not Exists");
            }
            httpResponse.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, allowedCors);
            filterChain.doFilter(request, response);
        } catch (RuntimeException runtimeException) {
            httpResponse.setStatus(UNAUTHORIZED.value());
            Map<String, String> error = new HashMap<>();
            error.put("status", UNAUTHORIZED.getReasonPhrase());
            error.put("timestamp", String.valueOf(LocalDateTime.now()));
            error.put("message", runtimeException.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
}
