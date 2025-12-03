package com.hotel.management.auth;

import jakarta.servlet.FilterChain;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

public class JwtFilter extends OncePerRequestFilter {

    @Autowired

    private JwtUtil jwtUtil;

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)

            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Public endpoints that don't require authentication
        boolean isPublicEndpoint =
                // Auth endpoints
                path.startsWith("/api/auth/") ||
                        path.equals("/api/users/login") ||
                        (path.equals("/api/users") && request.getMethod().equalsIgnoreCase("POST")) ||

                        // Static files
                        path.startsWith("/uploads/") ||
                        path.startsWith("/media/") ||

                        // Activity logs (for monitoring)
                        path.startsWith("/api/activity-logs/") ||
                        path.startsWith("/api/activity-demo/") ||

                        // Facilities and bookings (public access for browsing)
                        path.startsWith("/facilities/") ||
                        path.startsWith("/facility-bookings/") ||

                        // Rooms (public access for browsing available rooms)
                        (path.startsWith("/api/rooms") && request.getMethod().equalsIgnoreCase("GET")) ||

                        // Branches (public access for viewing locations)
                        (path.startsWith("/api/branches") && request.getMethod().equalsIgnoreCase("GET")) ||

                        // Room types (public access for browsing)
                        (path.startsWith("/api/roomtypes") && request.getMethod().equalsIgnoreCase("GET")) ||

                        // Payments (public GET access for retrieving payment info)
                        (path.startsWith("/api/payments/") && request.getMethod().equalsIgnoreCase("GET"));

        if (isPublicEndpoint) {
            filterChain.doFilter(request, response);
            return;
        }


        String auth = request.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write("Missing or invalid Authorization header");

            return;

        }

        String token = auth.substring(7);

        if (!jwtUtil.validateToken(token)) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write("Invalid or expired token");

            return;

        }

        // Extract user info

        Integer userId = jwtUtil.getUserId(token);

        Integer roleId = jwtUtil.getRoleId(token);

        // Attach attributes for RBAC checks

        request.setAttribute("userId", userId);

        request.setAttribute("roleId", roleId);

        filterChain.doFilter(request, response);

    }

}

