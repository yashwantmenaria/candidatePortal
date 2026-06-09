package com.example.candidateportal.security;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    private String secret = "3u/MKRR+vCeIpgpeVV5a6Zo8N4EBhiv8mUA5pVsTDBCJzYm6O9YA+ZOVlx9zd95iYEVuPJ7z8whxCjRqjXKMMA==";

    public String generateToken(String username,Long empId, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("empId", empId)
                .claim("role", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}