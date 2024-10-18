package com.lautaro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        Instant now = Instant.now();
        Instant expirationTime = now.plusMillis(expiration);

        System.out.println("Generando token para usuario: " + userDetails.getUsername());
        System.out.println("Hora actual (UTC): " + now);
        System.out.println("Hora de expiración (UTC): " + expirationTime);
        System.out.println("Duración del token (ms): " + expiration);

        String token = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationTime))
                .claim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .signWith(getSignInKey())
                .compact();

        System.out.println("Token generado: " + token);
        return token;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        Date expiration = extractExpiration(token);
        System.out.println("Validando token - Usuario: " + username);
        System.out.println("Fecha de expiración del token (UTC): " + expiration.toInstant());
        System.out.println("Fecha actual (UTC): " + Instant.now());
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final long clockSkewSeconds = 60; // 1 minuto de margen
        Instant expiration = extractExpiration(token).toInstant();
        Instant now = Instant.now();
        boolean isExpired = expiration.isBefore(now.minusSeconds(clockSkewSeconds));
        System.out.println("Fecha de expiración del token: " + expiration);
        System.out.println("Fecha actual: " + now);
        System.out.println("¿Token expirado?: " + isExpired);
        return isExpired;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Error al parsear el token: " + e.getMessage());
            System.out.println("Token problemático: " + token);
            e.printStackTrace();
            throw e;
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
