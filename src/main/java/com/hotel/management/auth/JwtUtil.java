package com.hotel.management.auth;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.io.Decoders;

import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.security.Key;

import java.util.Date;

import java.util.Map;

@Component

public class JwtUtil {

	private final Key key;

	private final long expirationMs;

	public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expirationMs}") long expirationMs) {

		byte[] keyBytes = Decoders.BASE64.decode(secret);

		this.key = Keys.hmacShaKeyFor(keyBytes);

		this.expirationMs = expirationMs;

	}

	

	public String generateToken(int userId, int roleId) {

		Date now = new Date();

		Date expiry = new Date(now.getTime() + expirationMs);

		return Jwts.builder().subject(String.valueOf(userId)).issuedAt(now).expiration(expiry)

				.claims(Map.of("userId", userId, "roleId", roleId)).signWith(key) 

				.compact();

	}

	

	@SuppressWarnings("deprecation")

	public boolean validateToken(String token) {

		try {

			Jwts.parser().setSigningKey(key) 

					.build().parseSignedClaims(token);

			return true;

		} catch (Exception ex) {

			return false;

		}

	}

	

	public Claims getClaims(String token) {

		return Jwts.parser().setSigningKey(key) // ⬅ NEW syntax

				.build().parseSignedClaims(token).getPayload();

	}

	

	public Integer getUserId(String token) {

		Object v = getClaims(token).get("userId");

		return v == null ? null : ((Number) v).intValue();

	}

	

	public Integer getRoleId(String token) {

		Object v = getClaims(token).get("roleId");

		return v == null ? null : ((Number) v).intValue();

	}

}
