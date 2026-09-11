package com.fsd.exp5.filter;

import com.fsd.exp5.interceptor.CorrelationInterceptor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // Ensure correlationId is present in MDC during filter execution
        String correlationId = request.getHeader(CorrelationInterceptor.CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        MDC.put(CorrelationInterceptor.MDC_CORRELATION_ID_KEY, correlationId);
        response.setHeader(CorrelationInterceptor.CORRELATION_ID_HEADER, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            MDC.put(CorrelationInterceptor.MDC_CORRELATION_ID_KEY, correlationId);
            log.info("Request processed: method=[{}] uri=[{}] status=[{}] executionTime=[{} ms]",
                    method, uri, status, executionTime);
            MDC.remove(CorrelationInterceptor.MDC_CORRELATION_ID_KEY);
        }
    }
}
