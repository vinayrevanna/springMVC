package com.vinay.cust.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

	@Value("${secret.key}")
	private String SECRET_KEY;
	
	@Value("${ACCESS_TOKEN}")
	private String access_token;
	
	@Value("${REFRESH_TOKEN}")
	private String refresh_token;
	
	private int ACCESS_TOKEN_EXPIRATION_TIME = 1 * 60 * 1000;
	private int REFRESH_TOKEN_EXPIRATION_TIME = 5 * 60 * 1000;
	
	private Boolean genearateRefToken = false;
	
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        //get role from db
        claims.put("role","admin");
        claims.put("acessType",access_token);
        genearateRefToken = false;
        return createToken(claims, userDetails.getUsername(),genearateRefToken);
    }
    
    public String generateRefToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("acessType",refresh_token);
        genearateRefToken = true;
        return createToken(claims, userDetails.getUsername(),genearateRefToken);
    }

    private String createToken(Map<String, Object> claims, String subject, Boolean genearateRefToken) {
    	
    	return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(genearateRefToken ? new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME) : new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
	
}
