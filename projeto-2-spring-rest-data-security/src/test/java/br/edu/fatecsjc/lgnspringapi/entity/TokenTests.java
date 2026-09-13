package br.edu.fatecsjc.lgnspringapi.entity;

import br.edu.fatecsjc.lgnspringapi.enums.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TokenTests {
        private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        @Test
        void testNoArgsConstructorAndSetters() {
                Token token = new Token();
                token.setId(1L);
                token.setTokenValue("abc123");
                token.setTokenType(TokenType.BEARER);
                token.setRevoked(true);
                token.setExpired(false);
                User user = User.builder()
                                .id(100L)
                                .firstName("Teste")
                                .lastName("User")
                                .email("teste.user@example.com")
                                .password("senha")
                                .role(br.edu.fatecsjc.lgnspringapi.enums.Role.USER)
                                .build();
                token.setUser(user);
                assertEquals(1L, token.getId());
                assertEquals("abc123", token.getTokenValue());
                assertEquals(TokenType.BEARER, token.getTokenType());
                assertEquals(true, token.isRevoked());
                assertEquals(false, token.isExpired());
                assertNotNull(token.getUser());
                assertEquals(100L, token.getUser().getId());
        }

        @Test
        void testAllArgsConstructor() {
                User user = User.builder()
                                .id(101L)
                                .firstName("Alice")
                                .lastName("Silva")
                                .email("alice@example.com")
                                .password("senhaAlice")
                                .role(br.edu.fatecsjc.lgnspringapi.enums.Role.ADMIN)
                                .build();
                Token token = new Token(2L, "def456", TokenType.BEARER, false, true, user);
                assertEquals(2L, token.getId());
                assertEquals("def456", token.getTokenValue());
                assertEquals(TokenType.BEARER, token.getTokenType());
                assertEquals(false, token.isRevoked());
                assertEquals(true, token.isExpired());
                assertNotNull(token.getUser());
                assertEquals("alice@example.com", token.getUser().getEmail());
        }

        @Test
        void testBuilder() {
                Token token = Token.builder()
                                .id(3L)
                                .tokenValue("ghi789")
                                .revoked(false)
                                .expired(false)
                                .build();
                assertEquals(3L, token.getId());
                assertEquals("ghi789", token.getTokenValue());
                assertEquals(TokenType.BEARER, token.getTokenType(), "tokenType deve vir como default BEARER");
                assertEquals(false, token.isRevoked());
                assertEquals(false, token.isExpired());
        }

        @Test
        void testJsonSerializationAndDeserialization() throws Exception {
                User user = User.builder()
                                .id(102L)
                                .firstName("Bruna")
                                .lastName("Soares")
                                .email("bruna@example.com")
                                .password("senhaBruna")
                                .role(br.edu.fatecsjc.lgnspringapi.enums.Role.USER)
                                .build();
                Token token = Token.builder()
                                .id(4L)
                                .tokenValue("jkl012")
                                .tokenType(TokenType.BEARER)
                                .revoked(false)
                                .expired(false)
                                .user(user)
                                .build();
                String json = mapper.writeValueAsString(token);
                assertEquals(true, json.contains("\"id\":4"), "JSON deve conter o id");
                assertEquals(true, json.contains("\"tokenValue\":\"jkl012\""), "JSON deve conter tokenValue");
                assertEquals(true, json.contains("\"tokenType\":\"BEARER\""), "JSON deve conter tokenType como BEARER");
                assertEquals(true, json.contains("\"revoked\":false"), "JSON deve conter o valor false para revoked");
                assertEquals(true, json.contains("\"expired\":false"), "JSON deve conter o valor false para expired");
                assertEquals(false, json.contains("user"),
                                "JSON não deve conter o campo user devido ao @JsonBackReference");
                Token deserialized = mapper.readValue(json, Token.class);
                assertEquals(token.getId(), deserialized.getId());
                assertEquals(token.getTokenValue(), deserialized.getTokenValue());
                assertEquals(token.getTokenType(), deserialized.getTokenType());
                assertEquals(token.isRevoked(), deserialized.isRevoked());
                assertEquals(token.isExpired(), deserialized.isExpired());
                assertNull(deserialized.getUser(), "Após desserialização, user deve ser null");
        }

        @Test
        void testEqualsAndHashCode() {
                User user = User.builder()
                                .id(103L)
                                .firstName("Carlos")
                                .lastName("Ferreira")
                                .email("carlos@example.com")
                                .password("senhaCarlos")
                                .role(br.edu.fatecsjc.lgnspringapi.enums.Role.USER)
                                .build();
                Token token1 = Token.builder()
                                .id(5L)
                                .tokenValue("mno345")
                                .tokenType(TokenType.BEARER)
                                .revoked(true)
                                .expired(false)
                                .user(user)
                                .build();
                Token token2 = Token.builder()
                                .id(5L)
                                .tokenValue("mno345")
                                .tokenType(TokenType.BEARER)
                                .revoked(true)
                                .expired(false)
                                .user(user)
                                .build();
                assertEquals(token1, token2, "Tokens com os mesmos valores devem ser iguais");
                assertEquals(token1.hashCode(), token2.hashCode(), "Hashcodes devem ser iguais para tokens iguais");
                token2.setExpired(true);
                assertNotEquals(token1, token2, "Tokens devem diferir se algum campo for diferente");
        }

        @Test
        void testEqualsWithNullFields() {
                Token token1 = new Token();
                Token token2 = new Token();
                assertEquals(token1, token2, "Dois tokens com campos nulos devem ser iguais");
                assertEquals(token1.hashCode(), token2.hashCode(),
                                "Hashcodes devem ser iguais quando os campos são nulos");
        }

        @Test
        void testNotEqualsDifferentType() {
                Token token = Token.builder()
                                .id(6L)
                                .tokenValue("pqr678")
                                .build();
                assertNotEquals("Uma String", token, "Token não deve ser igual a um objeto de outro tipo");
        }

        @Test
        void testToString() {
                Token token = Token.builder()
                                .id(7L)
                                .tokenValue("stu901")
                                .tokenType(TokenType.BEARER)
                                .revoked(false)
                                .expired(true)
                                .build();
                String str = token.toString();
                assertNotNull(str, "toString() não deve retornar null");
                assertEquals(true, str.contains("7"), "toString deve conter o id");
                assertEquals(true, str.contains("stu901"), "toString deve conter o tokenValue");
                assertEquals(true, str.contains("BEARER"), "toString deve conter o tokenType");
                assertEquals(true, str.contains("false") || str.contains("true"),
                                "toString deve conter os valores booleanos");
        }

        @Test
        void testDefaultNoArgsValues() {
                Token token = new Token();
                assertNull(token.getId(), "Por padrão, id deve ser null");
                assertNull(token.getTokenValue(), "Por padrão, tokenValue deve ser null");
                assertEquals(TokenType.BEARER, token.getTokenType(), "Por padrão, tokenType deve ser BEARER");
                assertEquals(false, token.isRevoked(), "Por padrão, revoked deve ser false");
                assertEquals(false, token.isExpired(), "Por padrão, expired deve ser false");
                assertNull(token.getUser(), "Por padrão, user deve ser null");
                String str = token.toString();
                assertNotNull(str, "toString() não deve retornar null mesmo com campos nulos");
        }

        @Test
        void testHashCodeStability() {
                Token token = Token.builder()
                                .id(8L)
                                .tokenValue("vwx234")
                                .build();
                int hash1 = token.hashCode();
                int hash2 = token.hashCode();
                assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
        }

        @Test
        void testCanEqual() {
                Token token = Token.builder()
                                .id(9L)
                                .tokenValue("yz123")
                                .build();
                assertEquals(true, token.canEqual(token), "Um objeto deve poder comparar consigo mesmo via canEqual()");
                assertEquals(false, token.canEqual("Outro Tipo"),
                                "canEqual deve retornar false para objeto de tipo diferente");
                FakeToken fake = new FakeToken();
                assertEquals(false, token.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
        }

        @Test
        void testEqualsSameInstance() {
                Token token = Token.builder()
                                .id(10L)
                                .tokenValue("sameToken")
                                .build();
                assertEquals(true, token.equals(token), "Um objeto deve ser igual a si mesmo");
        }

        @Test
        void testEqualsNull() {
                Token token = Token.builder()
                                .id(10L)
                                .tokenValue("nullToken")
                                .build();
                assertEquals(false, token.equals(null), "Um objeto não deve ser igual a null");
        }

        @Test
        void testCanEqualDifferentInstances() {
                Token token1 = Token.builder()
                                .id(11L)
                                .tokenValue("diffToken")
                                .build();
                Token token2 = Token.builder()
                                .id(11L)
                                .tokenValue("diffToken")
                                .build();
                assertEquals(true, token1.canEqual(token2), "Objetos do mesmo tipo devem retornar true em canEqual()");
        }

        static class FakeToken extends Token {
                @Override
                public boolean canEqual(Object other) {
                        return false;
                }
        }
}