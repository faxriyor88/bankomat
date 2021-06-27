package com.example.bankomat.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProwider {
private String secret="kalitso'z";
private Long expireTime=36_000_000l;
    public String generateToken(String email){
        String token = Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expireTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return token;
    }
    public String usernameFromToken(String token){
        try {
            String subject = Jwts
                    .parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return subject;
        }catch (Exception e){
            return null;
        }
    }
}
