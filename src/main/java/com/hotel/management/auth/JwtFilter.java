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
 
		// Allow login & create-user without token

		boolean isLogin = path.equals("/api/users/login");

		boolean isCreateUser = path.equals("/api/users") 
&& request.getMethod().equalsIgnoreCase("POST");

		boolean isStaticFiles = path.startsWith("/uploads/");
 
		if (isLogin || isCreateUser || isStaticFiles) {

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

 