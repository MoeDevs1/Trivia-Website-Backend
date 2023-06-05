package com.muslimtrivia.Trivia.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String secretKey = "5971337336763979244226452948404D635166546A576E5A7234753777217A25\n";
    public String extractUserName(String token) {
        // Implementation goes here...
        return extractClaim(token, Claims::getSubject);
    }
    public String tokenGenerator(UserDetails userDetails){
        return tokenGenerator(new HashMap<>(), userDetails);
    }


    public String tokenGenerator(
            Map<String, Object> extraClaims,
            UserDetails details
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(details.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean tokenValidation(String token,UserDetails details){
        final String email = extractUserName(token);
        return (email.equals(details.getUsername())) && !tokenExpiration(token);
    }

    private boolean tokenExpiration(String token) {
        return extractExp(token).before(new Date());
    }

    private Date extractExp(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



    public <T> T extractClaim(String token, Function<Claims, T> Reslover){
        final Claims claims = extractClaims(token);
        return Reslover.apply(claims);
    }


    private Claims extractClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}