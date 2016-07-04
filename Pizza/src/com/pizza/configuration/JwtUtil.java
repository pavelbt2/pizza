package com.pizza.configuration;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
    //TODO @Value("${jwt.secret}")
    private String secret = "12345";

    // TODO expiration

    /**
     * Tries to parse specified String as a JWT token. If successful, returns User object with username, 
     * id and role prefilled (extracted from token).
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
     * 
     * @param token the JWT token to parse
     * @return the User object extracted from specified token or null if a token is invalid.
     */
    public JwtUser parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();

            JwtUser u = new JwtUser(username, ""); // TODO OK?
            // TODO use user factory
            return u;

        } catch (JwtException | ClassCastException e) {
        	// TODO log?
            return null;
        }
    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     * 
     * @param u the user for which the token will be generated
     * @return the JWT token
     */
    public String generateToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("userId", username);
        claims.put("created", new Date());

        return Jwts.builder()
                .setClaims(claims)
                //TODO .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
