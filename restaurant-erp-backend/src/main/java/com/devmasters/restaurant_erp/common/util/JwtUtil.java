package com.devmasters.restaurant_erp.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtil {

    public static String extractClaim(String token, String secret, String key) {

        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get(key, String.class);
    }
}
