package com.example.customer_api.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    // Store buckets in memory (Map<IP Address, Bucket>)
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKey = request.getRemoteAddr(); // Identify user by IP address
        
        // Get or create a bucket for this IP
        Bucket bucket = cache.computeIfAbsent(apiKey, this::createNewBucket);

        // Try to consume 1 token
        if (bucket.tryConsume(1)) {
            return true; // Success, proceed to Controller
        } else {
            // Fail, return 429 Error
            response.setStatus(429);
            response.getWriter().write("Too many requests - Rate limit exceeded");
            return false; // Block request
        }
    }

    private Bucket createNewBucket(String key) {
        // Rule: 100 requests allowed per 1 minute
        Bandwidth limit = Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}