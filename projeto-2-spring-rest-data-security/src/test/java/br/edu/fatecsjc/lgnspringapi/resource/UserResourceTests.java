package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.ChangePasswordRequestDTO;
import br.edu.fatecsjc.lgnspringapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserResourceTests {
        @Mock
        private UserService service;

        @InjectMocks
        private UserResource userResource;

        @Mock
        private Principal principal;

        @Test
        void testChangePasswordSuccess() {
                ChangePasswordRequestDTO request = ChangePasswordRequestDTO.builder()
                                .currentPassword("oldPass")
                                .newPassword("newPass")
                                .confirmationPassword("newPass")
                                .build();

                ResponseEntity<Void> response = userResource.changePassword(request, principal);

                verify(service).changePassword(request, principal);

                assertThat(response)
                                .isNotNull()
                                .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
                                .returns(null, ResponseEntity::getBody);
        }

        @Test
        void testChangePasswordFailure() {
                ChangePasswordRequestDTO request = ChangePasswordRequestDTO.builder()
                                .currentPassword("oldPass")
                                .newPassword("newPass")
                                .confirmationPassword("newPass")
                                .build();

                doThrow(new IllegalStateException("Error occurred")).when(service).changePassword(request, principal);

                assertThatThrownBy(() -> userResource.changePassword(request, principal))
                                .isInstanceOf(IllegalStateException.class)
                                .hasMessageContaining("Error occurred");
        }
}