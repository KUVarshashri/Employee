package com.example.employeecrud.security;

import com.example.employeecrud.dao.Employees;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRETE_KEY_STRING = "9Ceu2yTBcgAjEZlN30jeIT3DAa9gkvMe";
    private final SecretKey SECRETE_KEY = Keys.hmacShaKeyFor(SECRETE_KEY_STRING.getBytes());

    public String generateToken(UserDetails userDetails, Employees employees) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("empId", employees.getEmpId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(SECRETE_KEY, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 days
                .signWith(SECRETE_KEY, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(SECRETE_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(SECRETE_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Long extractsEmployeesId(String token) {
        return Jwts.parser()
                .verifyWith(SECRETE_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("empId", Long.class);
    }
}
