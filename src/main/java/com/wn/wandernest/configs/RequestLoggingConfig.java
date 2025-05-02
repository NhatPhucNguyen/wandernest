package com.wn.wandernest.configs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RequestLoggingConfig {
    @Bean
    public OncePerRequestFilter secureRequestLoggingFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                
                ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
                
                // Continue with the filter chain
                filterChain.doFilter(wrappedRequest, response);
                
                // Log after the request has been processed
                String uri = request.getRequestURI();
                String queryString = request.getQueryString();
                if (queryString != null) {
                    uri += "?" + queryString;
                }
                
                String method = request.getMethod();
                String contentType = request.getContentType();
                
                // Extract the request body
                String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
                
                // Redact sensitive information
                if (requestBody != null && !requestBody.isEmpty()) {
                    // Redact password field
                    requestBody = requestBody.replaceAll("\"password\"\\s*:\\s*\"[^\"]*\"", "\"password\":\"[REDACTED]\"");
                    // Redact other sensitive fields if needed
                    requestBody = requestBody.replaceAll("\"newPassword\"\\s*:\\s*\"[^\"]*\"", "\"newPassword\":\"[REDACTED]\"");
                    requestBody = requestBody.replaceAll("\"confirmPassword\"\\s*:\\s*\"[^\"]*\"", "\"confirmPassword\":\"[REDACTED]\"");
                }
                
                log.info("REQUEST: {} {} (Content-Type: {})", method, uri, contentType);
                if (requestBody != null && !requestBody.isEmpty()) {
                    log.info("REQUEST DATA: {}", requestBody);
                }
            }
        };
    }
}
