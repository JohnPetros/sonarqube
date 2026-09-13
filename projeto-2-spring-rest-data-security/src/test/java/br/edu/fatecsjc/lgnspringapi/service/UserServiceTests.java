package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.ChangePasswordRequestDTO;
import br.edu.fatecsjc.lgnspringapi.entity.User;
import br.edu.fatecsjc.lgnspringapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    private User user;
    private ChangePasswordRequestDTO request;
    private Principal principal;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setPassword("encodedOldPassword");
        // Inicializa o request utilizando o builder gerado pelo Lombok
        request = ChangePasswordRequestDTO.builder().build();
    }

    @Test
    void testChangePasswordWrongCurrentPassword() {
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword");
        request.setConfirmationPassword("newPassword");

        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);
        principal = new UsernamePasswordAuthenticationToken(user, null);

        assertThatThrownBy(() -> userService.changePassword(request, principal))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Wrong password");

        verify(repository, never()).save(any());
    }

    @Test
    void testChangePasswordNewAndConfirmationMismatch() {
        // Configura um cenário onde a senha atual bate, mas nova senha e confirmação são diferentes
        request.setCurrentPassword("correctPassword");
        request.setNewPassword("newPassword");
        request.setConfirmationPassword("differentPassword");

        when(passwordEncoder.matches("correctPassword", user.getPassword())).thenReturn(true);
        principal = new UsernamePasswordAuthenticationToken(user, null);

        assertThatThrownBy(() -> userService.changePassword(request, principal))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Password are not the same");

        verify(repository, never()).save(any());
    }

    @Test
    void testChangePasswordSuccess() {
        // Configura um cenário de sucesso
        request.setCurrentPassword("correctPassword");
        request.setNewPassword("newPassword");
        request.setConfirmationPassword("newPassword");

        when(passwordEncoder.matches("correctPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        principal = new UsernamePasswordAuthenticationToken(user, null);

        assertDoesNotThrow(() -> userService.changePassword(request, principal));

        verify(passwordEncoder).encode("newPassword");
        verify(repository).save(user);
        assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
    }
}
