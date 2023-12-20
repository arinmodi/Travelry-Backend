package com.learning.travelry.utils;

import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "hjGJKnmkj89WHncdksw5";
    private static final long EXPIRATION_TIME = 3600000; // 1 hour in milliseconds
    private static final long EXPIRATION_TIME_LOGIN = 604_800_000;

    public static String generateToken(String email) {
        return Jwts.builder()
            .setSubject(email)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }

    public static String generateTokenForLogin(String email) {
        return Jwts.builder()
            .setSubject(email)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_LOGIN))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }

    public static String extractEmail(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
