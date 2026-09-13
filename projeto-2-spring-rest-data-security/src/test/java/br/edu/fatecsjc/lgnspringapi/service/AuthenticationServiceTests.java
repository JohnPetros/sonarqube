package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationRequestDTO;
import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationResponseDTO;
import br.edu.fatecsjc.lgnspringapi.dto.RegisterRequestDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Token;
import br.edu.fatecsjc.lgnspringapi.entity.User;
import br.edu.fatecsjc.lgnspringapi.enums.Role;
import br.edu.fatecsjc.lgnspringapi.repository.TokenRepository;
import br.edu.fatecsjc.lgnspringapi.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTests {
        @Mock
        private UserRepository repository;

        @Mock
        private TokenRepository tokenRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private JwtService jwtService;

        @Mock
        private AuthenticationManager authenticationManager;

        @InjectMocks
        private AuthenticationService authenticationService;

        @Test
        void testRegisterSuccessful() {
                RegisterRequestDTO request = RegisterRequestDTO.builder()
                                .firstname("John")
                                .lastname("Doe")
                                .email("john.doe@example.com")
                                .password("password")
                                .build();

                when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
                when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

                User savedUser = User.builder()
                                .id(1L)
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .password("encodedPassword")
                                .role(Role.USER)
                                .build();
                when(repository.save(any(User.class))).thenReturn(savedUser);
                when(jwtService.generateToken(any(User.class))).thenReturn("accessToken");
                when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

                AuthenticationResponseDTO responseDTO = authenticationService.register(request);

                // Encadeando as asserções: verifica que responseDTO não é nulo e que os tokens
                // retornados são os esperados.
                assertThat(responseDTO)
                                .isNotNull()
                                .returns("accessToken", AuthenticationResponseDTO::getAccessToken)
                                .returns("refreshToken", AuthenticationResponseDTO::getRefreshToken);
                verify(tokenRepository).save(any(Token.class));
        }

        @Test
        void testRegisterEmailAlreadyExists() {
                RegisterRequestDTO request = RegisterRequestDTO.builder()
                                .firstname("John")
                                .lastname("Doe")
                                .email("john.doe@example.com")
                                .password("password")
                                .build();
                when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

                ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                () -> authenticationService.register(request));
                assertThat(exception)
                                .extracting(ResponseStatusException::getStatusCode, ResponseStatusException::getReason)
                                .containsExactly(HttpStatus.BAD_REQUEST, "Email já cadastrado.");
        }

        @Test
        void testAuthenticateSuccessful() {
                AuthenticationRequestDTO request = AuthenticationRequestDTO.builder()
                                .email("john.doe@example.com")
                                .password("password")
                                .build();

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(new UsernamePasswordAuthenticationToken(request.getEmail(),
                                                request.getPassword()));

                User user = User.builder()
                                .id(1L)
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .password("encodedPassword")
                                .role(Role.USER)
                                .build();
                when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

                when(jwtService.generateToken(any(User.class))).thenReturn("accessToken");
                when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

                AuthenticationResponseDTO responseDTO = authenticationService.authenticate(request);

                assertThat(responseDTO)
                                .isNotNull()
                                .returns("accessToken", AuthenticationResponseDTO::getAccessToken)
                                .returns("refreshToken", AuthenticationResponseDTO::getRefreshToken);
                verify(tokenRepository).save(any(Token.class));
        }

        @Test
        void testAuthenticateUserNotFound() {
                AuthenticationRequestDTO request = AuthenticationRequestDTO.builder()
                                .email("nonexistent@example.com")
                                .password("password")
                                .build();

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(new UsernamePasswordAuthenticationToken(request.getEmail(),
                                                request.getPassword()));
                when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

                ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                () -> authenticationService.authenticate(request));
                assertThat(exception)
                                .extracting(ResponseStatusException::getStatusCode, ResponseStatusException::getReason)
                                .containsExactly(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos");
        }

        @Test
        void testRefreshTokenInvalidHeader() {
                HttpServletRequest request = mock(HttpServletRequest.class);
                HttpServletResponse response = mock(HttpServletResponse.class);
                when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

                ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                () -> authenticationService.refreshToken(request, response));
                assertThat(exception)
                                .extracting(ResponseStatusException::getStatusCode, ResponseStatusException::getReason)
                                .containsExactly(HttpStatus.BAD_REQUEST, "Token inválido");
        }

        @Test
        void testRefreshTokenTokenNotValid() {
                String refreshToken = "validRefreshToken";
                String userEmail = "john.doe@example.com";
                HttpServletRequest request = mock(HttpServletRequest.class);
                HttpServletResponse response = mock(HttpServletResponse.class);
                when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
                when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);

                User user = User.builder()
                                .id(1L)
                                .firstName("John")
                                .email(userEmail)
                                .build();
                when(repository.findByEmail(userEmail)).thenReturn(Optional.of(user));
                when(jwtService.isTokenValid(eq(refreshToken), any(User.class))).thenReturn(false);

                ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                () -> authenticationService.refreshToken(request, response));
                assertThat(exception)
                                .extracting(ResponseStatusException::getStatusCode, ResponseStatusException::getReason)
                                .containsExactly(HttpStatus.FORBIDDEN, "Token inválido ou expirado");
        }

        @Test
        void testRefreshTokenSuccessful() throws IOException {
                String refreshToken = "validRefreshToken";
                String userEmail = "john.doe@example.com";
                HttpServletRequest request = mock(HttpServletRequest.class);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                HttpServletResponse response = mock(HttpServletResponse.class);
                when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(baos));

                when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
                when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);

                User user = User.builder()
                                .id(1L)
                                .firstName("John")
                                .email(userEmail)
                                .build();
                when(repository.findByEmail(userEmail)).thenReturn(Optional.of(user));
                when(jwtService.isTokenValid(eq(refreshToken), any(User.class))).thenReturn(true);
                when(jwtService.generateToken(any(User.class))).thenReturn("newAccessToken");

                authenticationService.refreshToken(request, response);

                String jsonOutput = baos.toString();
                assertThat(jsonOutput)
                                .isNotEmpty()
                                .contains("newAccessToken")
                                .contains(refreshToken);
                verify(tokenRepository).save(any(Token.class));
        }

        private static class DelegatingServletOutputStream extends jakarta.servlet.ServletOutputStream {
                private final OutputStream targetStream;

                DelegatingServletOutputStream(OutputStream targetStream) {
                        this.targetStream = targetStream;
                }

                @Override
                public void write(int b) throws IOException {
                        targetStream.write(b);
                }

                @Override
                public boolean isReady() {
                        return true;
                }

                @Override
                public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                        // no op
                }
        }
}