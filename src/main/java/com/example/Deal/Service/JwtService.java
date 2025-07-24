package com.example.Deal.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Responsible for token validation.
 */
@Service
@Getter
@Setter
public class JwtService {

    @Value("${token.secret.key}")
    private String secret;

    /**
     * Checks if token not expired.
     *
     * @param token token that must be checked
     * @return result of checking
     */
    public boolean isTokenValid(String token) {
        return !getExpiration(token).before(new Date());
    }

    /**
     * Extracts username from passed token.
     *
     * @param token token that store data
     * @return extracted username
     */
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extracts user role list from passed token.
     *
     * @param token token that store data
     * @return extracted role list
     */
    public List<String> getRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }

    /**
     * Extracts expiration from passed token.
     *
     * @param token token that store data
     * @return extracted expiration
     */
    private Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    /**
     * Extract all claims from passed token.
     *
     * @param token token that contains data
     * @return all extracted claims
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Forms key for JWT.
     *
     * @return created key
     */
    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

}
