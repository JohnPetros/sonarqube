package br.edu.fatecsjc.lgnspringapi.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTests {
    private JwtService jwtService;
    private String base64SecretKey;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        String rawKey = "12345678901234567890123456789012";
        base64SecretKey = Base64.getEncoder()
                .encodeToString(rawKey.getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(jwtService, "secretKey", base64SecretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 60000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 120000L);
    }

    @Test
    void testGenerateTokenAndExtractUsername() {
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        assertThat(token)
                .isNotNull()
                .isNotEmpty();

        String extractedUsername = jwtService.extractUsername(token);
        assertThat(extractedUsername).isEqualTo(userDetails.getUsername());
    }

    @Test
    void testGenerateRefreshToken() {
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("password")
                .roles("USER")
                .build();

        String refreshToken = jwtService.generateRefreshToken(userDetails);
        assertThat(refreshToken)
                .isNotNull()
                .isNotEmpty();
        String extractedUsername = jwtService.extractUsername(refreshToken);
        assertThat(extractedUsername).isEqualTo(userDetails.getUsername());
    }

    @Test
    void testExtractClaim() {
        UserDetails userDetails = User.withUsername("claim@example.com")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
        assertThat(expiration)
                .isNotNull()
                .isAfter(new Date());
    }

    @Test
    void testIsTokenValidForValidToken() {
        UserDetails userDetails = User.withUsername("valid@example.com")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        boolean valid = jwtService.isTokenValid(token, userDetails);
        assertThat(valid).isTrue();
    }

    @Test
    void testIsTokenValidForExpiredToken() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1L);
        UserDetails userDetails = User.withUsername("expired@example.com")
                .password("password")
                .roles("USER")
                .build();
        String token = jwtService.generateToken(userDetails);
        boolean valid = jwtService.isTokenValid(token, userDetails);
        assertThat(valid).isFalse();
    }

    @Test
    void testIsTokenExpiredHandling() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        UserDetails userDetails = User.withUsername("expired2@example.com")
                .password("password")
                .roles("USER")
                .build();
        String token = jwtService.generateToken(userDetails);
        boolean valid = jwtService.isTokenValid(token, userDetails);
        assertThat(valid).isFalse();
    }

    @Test
    void testExtractAllClaimsForMalformedToken() {
        String malformedToken = "abc.def.ghi";
        assertThrows(Exception.class, () -> jwtService.extractClaim(malformedToken, Claims::getSubject));
    }
}