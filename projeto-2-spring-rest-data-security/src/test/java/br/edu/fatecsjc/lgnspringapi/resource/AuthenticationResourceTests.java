package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationRequestDTO;
import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationResponseDTO;
import br.edu.fatecsjc.lgnspringapi.dto.RegisterRequestDTO;
import br.edu.fatecsjc.lgnspringapi.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationResourceTests {
        @Mock
        private AuthenticationService authenticationService;

        @InjectMocks
        private AuthenticationResource authenticationResource;

        @Test
        void testRegister() {
                RegisterRequestDTO registerRequest = RegisterRequestDTO.builder()
                                .firstname("John")
                                .lastname("Doe")
                                .email("john.doe@example.com")
                                .password("password")
                                .build();

                AuthenticationResponseDTO authResponse = AuthenticationResponseDTO.builder()
                                .accessToken("dummyAccessToken")
                                .refreshToken("dummyRefreshToken")
                                .build();

                when(authenticationService.register(registerRequest)).thenReturn(authResponse);

                ResponseEntity<AuthenticationResponseDTO> response = authenticationResource.register(registerRequest);

                assertThat(response)
                                .isNotNull()
                                .returns(HttpStatus.CREATED, ResponseEntity::getStatusCode)
                                .returns(authResponse, ResponseEntity::getBody);
        }

        @Test
        void testAuthenticate() {
                AuthenticationRequestDTO authRequest = AuthenticationRequestDTO.builder()
                                .email("john.doe@example.com")
                                .password("password")
                                .build();

                AuthenticationResponseDTO authResponse = AuthenticationResponseDTO.builder()
                                .accessToken("dummyAccessToken")
                                .refreshToken("dummyRefreshToken")
                                .build();

                when(authenticationService.authenticate(authRequest)).thenReturn(authResponse);

                ResponseEntity<AuthenticationResponseDTO> response = authenticationResource.authenticate(authRequest);

                assertThat(response)
                                .isNotNull()
                                .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
                                .returns(authResponse, ResponseEntity::getBody);
        }

        @Test
        void testRefreshToken() throws IOException {
                HttpServletRequest request = mock(HttpServletRequest.class);
                HttpServletResponse response = mock(HttpServletResponse.class);

                doNothing().when(authenticationService).refreshToken(request, response);

                ResponseEntity<Void> respEntity = authenticationResource.refreshToken(request, response);

                verify(authenticationService).refreshToken(request, response);

                assertThat(respEntity)
                                .isNotNull()
                                .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
                                .returns(null, ResponseEntity::getBody);
        }
}