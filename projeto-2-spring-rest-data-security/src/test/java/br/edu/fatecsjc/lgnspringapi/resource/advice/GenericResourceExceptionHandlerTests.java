package br.edu.fatecsjc.lgnspringapi.resource.advice;

import br.edu.fatecsjc.lgnspringapi.dto.ApiErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class GenericResourceExceptionHandlerTests {
        @SuppressWarnings("null")
        @Test
        void testCatchExceptionReturningBadRequest() {
                GenericResourceExceptionHandler handler = new GenericResourceExceptionHandler();
                HttpServletRequest request = mock(HttpServletRequest.class);
                Exception exception = new Exception("Test exception");

                ResponseEntity<ApiErrorDTO> response = handler.catchExceptionReturningBadRequest(request, exception);

                assertThat(response.getStatusCode())
                                .isEqualTo(HttpStatus.BAD_REQUEST);

                ApiErrorDTO errorDTO = response.getBody();
                assertThat(errorDTO).isNotNull();
                assertThat(errorDTO.getMessage())
                                .as("Verifica se a mensagem padrão é definida")
                                .isEqualTo("An unknown error occurred in API processing");
                assertThat(errorDTO.getTimestamp())
                                .as("Verifica se o timestamp não é nulo")
                                .isNotNull();

                Instant now = Instant.now();
                assertThat(errorDTO.getTimestamp()).isBeforeOrEqualTo(now);
        }
}