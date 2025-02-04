package com.wn.wandernest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.wn.wandernest.utils.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtTokenUtilTest {

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    private String secret = "mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey"; // Example 256-bit secret key
    private int expirationMs = 3600000; // Example expiration time (1 hour)

    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userDetails = new User("testuser", "password", new ArrayList<>());

        jwtTokenUtil.setSecret(secret);
        jwtTokenUtil.setExpirationMs(expirationMs);
    }

    @Test
    public void testGenerateToken() {
        String token = jwtTokenUtil.generateToken(userDetails);
        assertEquals(userDetails.getUsername(), jwtTokenUtil.extractUsername(token));
    }

    @Test
    public void testValidateToken() {
        String token = jwtTokenUtil.generateToken(userDetails);
        assertTrue(jwtTokenUtil.validateToken(token, userDetails));
    }

    @Test
    public void testExtractUsername() {
        String token = jwtTokenUtil.generateToken(userDetails);
        assertEquals("testuser", jwtTokenUtil.extractUsername(token));
    }

    @Test
    public void testIsTokenExpired() {
        String token = jwtTokenUtil.generateToken(userDetails);
        assertFalse(jwtTokenUtil.isTokenExpired(token));

        // Create an expired token
        String expiredToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - expirationMs - 1000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS512)
                .compact();
        try {
            assertTrue(jwtTokenUtil.isTokenExpired(expiredToken));
        } catch (ExpiredJwtException e) {
            assertTrue(true); // Token is expected to be expired
        }
    }
}
