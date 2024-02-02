package com.the_daul_intra.mini.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtil {
    //토큰 발급
    public static String createJwt(String email, String secretKey){
        Date expirationDate = Date.from(ZonedDateTime.now().plus(1, ChronoUnit.MONTHS).toInstant());
        Date expirationDate2 = Date.from(ZonedDateTime.now().plusSeconds(5).toInstant());
        return Jwts.builder()
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //발급된 토큰 유효시간 검증
    public static boolean isExpired(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    //토큰에서 email 추출
    public static String getEmailFromToken(String token, String secretKey) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("email", String.class);
    }
}