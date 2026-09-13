package br.edu.fatecsjc.lgnspringapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import br.edu.fatecsjc.lgnspringapi.entity.Token;
import br.edu.fatecsjc.lgnspringapi.entity.User;
import br.edu.fatecsjc.lgnspringapi.enums.TokenType;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class TokenRepositoryTests {
        @Autowired
        private TokenRepository tokenRepository;

        @Autowired
        private UserRepository userRepository;

        @Test
        void testFindByTokenValue() {
                User user = User.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .password("password")
                                .build();
                user = userRepository.save(user);

                String tokenValue = "test-token-value";
                Token token = Token.builder()
                                .tokenValue(tokenValue)
                                .tokenType(TokenType.BEARER)
                                .revoked(false)
                                .expired(false)
                                .user(user)
                                .build();
                tokenRepository.save(token);

                Optional<Token> optionalToken = tokenRepository.findByTokenValue(tokenValue);
                assertThat(optionalToken).isPresent();
                Token foundToken = optionalToken.get();
                assertThat(foundToken.getTokenValue()).isEqualTo(tokenValue);
                assertThat(foundToken.getUser()).isEqualTo(user);
        }

        @Test
        void testFindAllValidTokenByUser() {
                User user = User.builder()
                                .firstName("Alice")
                                .lastName("Smith")
                                .email("alice.smith@example.com")
                                .password("password")
                                .build();
                user = userRepository.save(user);

                Token token1 = Token.builder()
                                .tokenValue("valid-token-1")
                                .tokenType(TokenType.BEARER)
                                .revoked(false)
                                .expired(false)
                                .user(user)
                                .build();
                tokenRepository.save(token1);

                Token token2 = Token.builder()
                                .tokenValue("valid-token-2")
                                .tokenType(TokenType.BEARER)
                                .revoked(false)
                                .expired(true)
                                .user(user)
                                .build();
                tokenRepository.save(token2);

                Token token3 = Token.builder()
                                .tokenValue("invalid-token")
                                .tokenType(TokenType.BEARER)
                                .revoked(true)
                                .expired(true)
                                .user(user)
                                .build();
                tokenRepository.save(token3);

                List<Token> validTokens = tokenRepository.findAllValidTokenByUser(user.getId());

                assertThat(validTokens).hasSize(2);
                assertThat(validTokens)
                                .extracting(Token::getTokenValue)
                                .containsExactlyInAnyOrder("valid-token-1", "valid-token-2");
                assertThat(validTokens)
                                .extracting(Token::getTokenValue)
                                .doesNotContain("invalid-token");
        }
}