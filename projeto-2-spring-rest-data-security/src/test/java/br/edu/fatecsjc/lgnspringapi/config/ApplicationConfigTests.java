package br.edu.fatecsjc.lgnspringapi.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import br.edu.fatecsjc.lgnspringapi.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationConfigTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        assertNotNull(passwordEncoder);
        assertNotNull(authenticationManager);

        String rawPassword = "abc123";
        String encoded = passwordEncoder.encode(rawPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encoded));
    }

    @Test
    void testUserNotFoundThrowsException() {
        String email = "nonexistent@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email),
                "Esperava que o método lançasse UsernameNotFoundException quando o usuário não for encontrado.");

        assertEquals("User not found", exception.getMessage());
    }
}