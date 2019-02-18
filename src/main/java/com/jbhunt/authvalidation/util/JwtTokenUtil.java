package com.jbhunt.authvalidation.util;

import com.jbhunt.authvalidation.constant.ApplicationConstant;
import com.jbhunt.authvalidation.dto.CustomerAuthDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenUtil implements Serializable {

    @Value("${authKey}")
    private String jwtKey;

    @Value("${authTokenExpiration}")
    private Long expirationHours;

    public String doGenerateToken(String subject) {
        String token = "";
        Claims claims = Jwts.claims().setSubject(subject);
        try {
            token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuer("http://jbhunt.com")
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(Date.from(LocalDateTime.now().plusHours(expirationHours).atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(SignatureAlgorithm.HS256, jwtKey.getBytes())
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }
    public Mono<String> isValidToken(CustomerAuthDTO customerAuthDTO) {
        Claims claims = getClaims(customerAuthDTO.getToken());
        Date currentDate = new Date();
        if (claims.getExpiration().after(currentDate)) {
            return Mono.just(claims.getSubject());
        }
        return Mono.empty();
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtKey.getBytes()).parseClaimsJws(token).getBody();
    }
}