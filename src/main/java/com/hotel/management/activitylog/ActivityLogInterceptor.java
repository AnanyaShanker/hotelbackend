package com.hotel.management.activitylog;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to automatically log all HTTP requests (CCTV-like monitoring)
 */
@Component
public class ActivityLogInterceptor implements HandlerInterceptor {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) {
        // Record start time for execution time calculation
        startTime.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,
                               @Nullable Object handler, @Nullable Exception ex) {
        try {
            // Calculate execution time
            long executionTime = System.currentTimeMillis() - startTime.get();
            startTime.remove();

            // Get user ID from request attributes (set by JwtFilter)
            Integer userId = (Integer) request.getAttribute("userId");

            // Create activity log
            ActivityLog log = new ActivityLog();
            log.setUserId(userId);
            log.setAction(request.getMethod() + " " + request.getRequestURI());
            log.setRequestMethod(request.getMethod());
            log.setEndpoint(request.getRequestURI());
            log.setStatusCode(response.getStatus());
            log.setIpAddress(getClientIP(request));
            log.setDeviceInfo(getDeviceInfo(request));
            log.setUserAgent(request.getHeader("User-Agent"));
            log.setExecutionTimeMs(executionTime);

            // Log error if exception occurred
            if (ex != null) {
                log.setErrorMessage(ex.getMessage());
                log.setStatusCode(500); // Internal server error
            }

            // Save to database (async to not slow down response)
            activityLogRepository.save(log);

        } catch (Exception e) {
            // Silent fail - don't break application flow
            System.err.println("Failed to log request: " + e.getMessage());
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) return "Unknown";

        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("mobile") || userAgent.contains("android") ||
            userAgent.contains("iphone")) {
            return "Mobile";
        }
        if (userAgent.contains("tablet") || userAgent.contains("ipad")) {
            return "Tablet";
        }
        return "Desktop";
    }
}

