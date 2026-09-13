package br.edu.fatecsjc.lgnspringapi.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
 class TokenTypeTests {
    @Test
    void testTokenTypeValues() {
        assertEquals("BEARER", TokenType.BEARER.name());
    }
}