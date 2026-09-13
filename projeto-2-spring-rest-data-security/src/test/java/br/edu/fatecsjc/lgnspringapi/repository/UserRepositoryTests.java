package br.edu.fatecsjc.lgnspringapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import br.edu.fatecsjc.lgnspringapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTests {
        @Autowired
        private UserRepository userRepositoryTests;

        @Test
        void testFindByEmail_whenUserExists() {
                User user = User.builder()
                                .firstName("Alice")
                                .lastName("Smith")
                                .email("alice@example.com")
                                .password("password")
                                .build();
                userRepositoryTests.save(user);

                Optional<User> foundUser = userRepositoryTests.findByEmail("alice@example.com");
                assertThat(foundUser)
                                .as("Verifica se o usuário foi encontrado pelo email")
                                .isPresent();
                assertThat(foundUser.get().getEmail()).isEqualTo("alice@example.com");
        }

        @Test
        void testFindByEmail_whenUserDoesNotExist() {
                Optional<User> foundUser = userRepositoryTests.findByEmail("nonexistent@example.com");
                assertThat(foundUser)
                                .as("Verifica que não há usuário para o email inexistente")
                                .isNotPresent();
        }
}