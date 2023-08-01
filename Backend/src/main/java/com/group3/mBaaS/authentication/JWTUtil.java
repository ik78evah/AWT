package com.group3.mBaaS.authentication;

import com.group3.mBaaS.user.Mbuser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.*;

/**
 * This class contains utilities for a Json web token
 */
@Component
public class JWTUtil {

    @Value("${springbootwebfluxjjwt.jjwt.secret}")
    private String secret;

    @Value("${springbootwebfluxjjwt.jjwt.expiration}")
    private String expirationTime;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * gets all the claims of the provided JWT
     * @param token from which the claims should be extracted
     * @return Claims which were extracted from the token
     */
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * extracts the role of a user from the Json Web Token (JWT)
     * @param token from which the role should be extracted
     * @return role of the subject of the JWT
     */
    public String getRolesFromToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        ArrayList<String> roles = claims.get("role", ArrayList.class);
        if(roles.size()>1){
            throw new IllegalStateException("User can only have one role");
        }
        return roles.get(0);
    }

    /**
     * gets the Username from a Token
     * @param token from which the username should be extracted
     * @return Username which was extracted from the token
     */
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * gets the expiration date of the transferred token
     * @param token from which the expiration date should be extracted
     * @return expiration date which was extracted from the token
     */
    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    /**
     * checks if the transferred token is expired
     * @param token which will be checked if it is expired
     * @return boolean indicating if the token is expired
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Generates a json web token and adds the role of the provided MBUser as a claim
     * @param MBUser from whom the role will be added in the token as a claim
     * @return String containing the Json web token
     */
    public String generateToken(Mbuser MBUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", MBUser.getRoles());
        return doGenerateToken(claims, MBUser.getUsername());
    }

    /**
     * Creates a Json web token containing the specified clams and username
     * @param claims which should be added in the token
     * @param username subject of the token
     * @return a token containing the specified clams and subject
     */
    private String doGenerateToken(Map<String, Object> claims, String username) {
        Long expirationTimeLong = Long.parseLong(expirationTime); //in second
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    /**
     * checks if a token is valid by checking if it is expired
     * @param token which should be checked for its validity
     * @return boolean indicating if the token is valid
     */
    public Boolean validateToken(String token) {
        Boolean isValid = false;
        try{
           isValid =  !isTokenExpired(token);
        } catch (Exception e){
            isValid = false;
        }
        return isValid;

    }

}
