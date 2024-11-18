package com.alessiofontani.taskmanagementtool.security.jwt;

import com.alessiofontani.taskmanagementtool.model.Role;
import com.alessiofontani.taskmanagementtool.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret.key}") String key) {
        byte[] bytes = Decoders.BASE64.decode(key);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }

    public String generateJwt(User user) {
        Date date= new Date();
        return  Jwts.builder()
                .issuer("")
                .subject("")
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles().stream().map(Role::getName).toList())
                .claim("email", user.getEmail())
                .issuedAt(date)
                .expiration(new Date(date.getTime() + 86400000))
                .signWith(secretKey)
                .compact();
    }

    public Authentication validateJwt(String jwt) {
        try {
            JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
            Claims claims = jwtParser.parseSignedClaims(jwt).getPayload();
            List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(claims);
            if(isTokenValid(claims)) {
                return new UsernamePasswordAuthenticationToken(getUsernameFromToken(claims), null, grantedAuthorities);
            }
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return null;
        }
        return null;
    }

    private boolean isTokenValid(Claims claims) {
        return !getUsernameFromToken(claims).isEmpty() && !isTokenExpired(claims);
    }

    private String getUsernameFromToken(Claims claims) {
        return (String) claims.getOrDefault("username", "");
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.getOrDefault("roles", Collections.emptyList());
        if (!roles.isEmpty()) {
            return roles.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

}
