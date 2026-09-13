package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.entity.Token;
import br.edu.fatecsjc.lgnspringapi.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTests {
        @Mock
        private TokenRepository tokenRepository;

        @InjectMocks
        private LogoutService logoutService;

        @Mock
        private HttpServletRequest request;

        @Mock
        private HttpServletResponse response;

        @Mock
        private Authentication authentication;

        @Test
        void logout_doesNothingWhenNoAuthorizationHeader() {
                when(request.getHeader("Authorization")).thenReturn(null);

                logoutService.logout(request, response, authentication);

                verify(tokenRepository, never()).findByTokenValue(any());
        }

        @Test
        void logout_doesNothingWhenAuthorizationHeaderNotBearer() {
                when(request.getHeader("Authorization")).thenReturn("Basic abc");

                logoutService.logout(request, response, authentication);

                verify(tokenRepository, never()).findByTokenValue(any());
        }

        @Test
        void logout_doesNothingWhenTokenNotFound() {
                String token = "testToken";
                when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
                when(tokenRepository.findByTokenValue(token)).thenReturn(Optional.empty());

                logoutService.logout(request, response, authentication);

                verify(tokenRepository, never()).save(any());
        }

        @Test
        void logout_setsTokenExpiredAndClearsContextWhenTokenFound() {
                String token = "testToken";
                when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
                Token storedToken = new Token();
                storedToken.setExpired(false);
                storedToken.setRevoked(false);
                when(tokenRepository.findByTokenValue(token)).thenReturn(Optional.of(storedToken));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                logoutService.logout(request, response, authentication);

                assertThat(storedToken.isExpired())
                                .as("O token deve estar marcado como expirado")
                                .isTrue();
                assertThat(storedToken.isRevoked())
                                .as("O token deve estar marcado como revogado")
                                .isTrue();
                verify(tokenRepository).save(storedToken);
                assertThat(SecurityContextHolder.getContext().getAuthentication())
                                .as("O contexto de segurança deve ser limpo")
                                .isNull();
        }
}