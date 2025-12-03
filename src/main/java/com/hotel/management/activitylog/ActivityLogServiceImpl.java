package com.hotel.management.activitylog;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@SuppressWarnings("unused")
public class ActivityLogServiceImpl implements ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Override
    public void logActivity(Integer userId, String action, String entityType, Integer entityId,
                           String description, HttpServletRequest request) {
        try {
            ActivityLog log = new ActivityLog();
            log.setUserId(userId);
            log.setAction(action);
            log.setEntityType(entityType);
            log.setEntityId(entityId);
            log.setDescription(description);
            log.setIpAddress(getClientIP(request));
            log.setDeviceInfo(getDeviceInfo(request));
            log.setUserAgent(request.getHeader("User-Agent"));
            log.setRequestMethod(request.getMethod());
            log.setEndpoint(request.getRequestURI());

            activityLogRepository.save(log);
        } catch (Exception e) {
            // Silent fail - don't break application flow if logging fails
            System.err.println("Failed to log activity: " + e.getMessage());
        }
    }

    @Override
    public void logSimple(Integer userId, String action, String description, HttpServletRequest request) {
        logActivity(userId, action, null, null, description, request);
    }

    @Override
    public List<ActivityLogWithUser> getAllActivities() {
        return activityLogRepository.findAllWithUserDetails();
    }

    @Override
    public List<ActivityLogWithUser> getUserActivityHistory(Integer userId) {
        return activityLogRepository.findByUserIdWithUserDetails(userId);
    }

    @Override
    public List<ActivityLogWithUser> getEntityHistory(String entityType, Integer entityId) {
        return activityLogRepository.findByEntity(entityType, entityId);
    }

    @Override
    public List<ActivityLogWithUser> getActivitiesByDateRange(Timestamp start, Timestamp end) {
        return activityLogRepository.findByDateRange(start, end);
    }

    @Override
    public List<ActivityLogWithUser> getRecentActivities(int limit) {
        return activityLogRepository.findRecentLogs(limit);
    }

    @Override
    public List<ActivityLogWithUser> getActivitiesByIpAddress(String ipAddress) {
        return activityLogRepository.findByIpAddress(ipAddress);
    }

    @Override
    public List<ActivityLogWithUser> getFailedRequests() {
        return activityLogRepository.findFailedRequests();
    }

    @Override
    public int getUserActivityCount(Integer userId) {
        return activityLogRepository.countByUserId(userId);
    }

    /**
     * Extract client IP address from request
     */
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // Handle multiple IPs in X-Forwarded-For
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * Detect device type from User-Agent
     */
    private String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "Unknown";
        }

        userAgent = userAgent.toLowerCase();

        // Check for mobile devices
        if (userAgent.contains("mobile") || userAgent.contains("android") ||
            userAgent.contains("iphone") || userAgent.contains("ipod")) {
            return "Mobile";
        }

        // Check for tablets
        if (userAgent.contains("tablet") || userAgent.contains("ipad")) {
            return "Tablet";
        }

        // Check for common browsers
        if (userAgent.contains("chrome")) {
            return "Desktop - Chrome";
        } else if (userAgent.contains("firefox")) {
            return "Desktop - Firefox";
        } else if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
            return "Desktop - Safari";
        } else if (userAgent.contains("edge")) {
            return "Desktop - Edge";
        } else if (userAgent.contains("msie") || userAgent.contains("trident")) {
            return "Desktop - IE";
        }

        return "Desktop";
    }
}

