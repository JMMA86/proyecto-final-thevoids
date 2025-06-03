package org.thevoids.oncologic.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.Setter;

@Service
public class JwtService {

    @Value("${app.security.secretkey}")
    @Setter
    @Getter
    private String secret;

    @Value("${app.security.expirationMinutes}")
    @Setter
    @Getter
    private int expirationMinutes;

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1000L * 60L * expirationMinutes);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setClaims(createClaims(userDetails))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public Map<String, Object> createClaims(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("identification", userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return claims;
    }

    public String extractUsername(String token) {
        return getClaims(token).get("identification", String.class);
    }
    
    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
    
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return userDetails.getUsername().equals(username) && !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            return false;
        } catch (MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return false;
        }
    }
}
