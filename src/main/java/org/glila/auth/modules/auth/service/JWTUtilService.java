package org.glila.auth.modules.auth.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

public class JWTUtilService {

    private static final String SECRET = "hne3gg4yp0MUdxHio39+Wt8hcTzJ0v9bPPFjN4+3EOtuJwxdiRiH57JD+WwVnDHdpHYXNz7LyxP+0bhfURTl3A==";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days


    public static String generateToken(String username) {
        return Jwts.builder()
                .subject(username) // Set the subject (username)
                .issuedAt(new Date()) // Set the current time as the issue time
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Set expiration time
                .signWith( Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact(); // Build and serialize the JWT
    }



    public static String extractUsername(String token) throws Exception{
        // extract the subject
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))

                .build()
                .parseSignedClaims(token)
                .getPayload();

        // check if expired
        if(claims.getExpiration().before(new Date())){
            throw new Exception("JWT expired");
        }

        return claims.getSubject();
    }

}
