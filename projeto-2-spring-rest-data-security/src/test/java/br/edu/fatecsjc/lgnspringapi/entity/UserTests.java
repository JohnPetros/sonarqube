package br.edu.fatecsjc.lgnspringapi.entity;

import br.edu.fatecsjc.lgnspringapi.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserTests {
        @Test
        void testNoArgsConstructorAndSetters() {
                User user = new User();
                user.setId(1L);
                user.setFirstName("Bruno");
                user.setLastName("Serpa");
                user.setEmail("bruno@example.com");
                user.setPassword("senha123");
                user.setRole(Role.USER);

                assertEquals(1L, user.getId());
                assertEquals("Bruno", user.getFirstName());
                assertEquals("Serpa", user.getLastName());
                assertEquals("bruno@example.com", user.getEmail());
                assertEquals("senha123", user.getPassword());
                assertEquals(Role.USER, user.getRole());
        }

        @Test
        void testAllArgsConstructor() {
                User user = new User(2L, "Alice", "Silva", "alice@example.com", "senha456", Role.ADMIN);
                assertEquals(2L, user.getId());
                assertEquals("Alice", user.getFirstName());
                assertEquals("Silva", user.getLastName());
                assertEquals("alice@example.com", user.getEmail());
                assertEquals("senha456", user.getPassword());
                assertEquals(Role.ADMIN, user.getRole());
        }

        @Test
        void testBuilder() {
                User user = User.builder()
                                .id(3L)
                                .firstName("Carlos")
                                .lastName("Oliveira")
                                .email("carlos@example.com")
                                .password("senha789")
                                .role(Role.USER)
                                .build();
                assertEquals(3L, user.getId());
                assertEquals("Carlos", user.getFirstName());
                assertEquals("Oliveira", user.getLastName());
                assertEquals("carlos@example.com", user.getEmail());
                assertEquals("senha789", user.getPassword());
                assertEquals(Role.USER, user.getRole());
        }

        @Test
        void testUserDetailsMethods() {
                User user = User.builder()
                                .id(4L)
                                .firstName("Diana")
                                .lastName("Costa")
                                .email("diana@example.com")
                                .password("senhaDiana")
                                .role(Role.ADMIN)
                                .build();
                assertEquals("diana@example.com", user.getUsername());
                assertEquals(true, user.isAccountNonExpired(), "isAccountNonExpired() deve retornar true");
                assertEquals(true, user.isAccountNonLocked(), "isAccountNonLocked() deve retornar true");
                assertEquals(true, user.isCredentialsNonExpired(), "isCredentialsNonExpired() deve retornar true");
                assertEquals(true, user.isEnabled(), "isEnabled() deve retornar true");
                Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
                assertEquals(user.getRole().getAuthorities(), authorities,
                                "As authorities retornadas devem ser as da role");
        }

        @Test
        void testEqualsAndHashCode() {
                User user1 = User.builder()
                                .id(6L)
                                .firstName("Fabio")
                                .lastName("Lima")
                                .email("fabio@example.com")
                                .password("senhaFabio")
                                .role(Role.ADMIN)
                                .build();
                User user2 = User.builder()
                                .id(6L)
                                .firstName("Fabio")
                                .lastName("Lima")
                                .email("fabio@example.com")
                                .password("senhaFabio")
                                .role(Role.ADMIN)
                                .build();
                assertEquals(user1, user2, "Objetos com os mesmos valores devem ser iguais");
                assertEquals(user1.hashCode(), user2.hashCode(), "Hashcodes de objetos iguais devem ser iguais");
                user2.setPassword("outrasenha");
                assertNotEquals(user1, user2, "Objetos devem ser diferentes se algum campo divergir");
        }

        @Test
        void testEqualsWithNullFields() {
                User user1 = new User();
                User user2 = new User();
                assertEquals(user1, user2, "Dois objetos com campos nulos devem ser iguais");
                assertEquals(user1.hashCode(), user2.hashCode(), "Hashcodes devem ser iguais com campos nulos");
        }

        @Test
        void testNotEqualsDifferentType() {
                User user = User.builder()
                                .id(7L)
                                .firstName("Gabriel")
                                .lastName("Ribeiro")
                                .email("gabriel@example.com")
                                .password("senhaGabriel")
                                .role(Role.USER)
                                .build();
                assertNotEquals("Uma String", user, "User não deve ser igual a um objeto de outro tipo");
        }

        @Test
        void testToString() {
                User user = User.builder()
                                .id(8L)
                                .firstName("Helena")
                                .lastName("Martins")
                                .email("helena@example.com")
                                .password("senhaHelena")
                                .role(Role.ADMIN)
                                .build();
                String str = user.toString();
                assertNotNull(str, "toString() não deve retornar null");
                assertEquals(true, str.contains("Helena"), "toString deve conter o primeiro nome");
                assertEquals(true, str.contains("Martins"), "toString deve conter o sobrenome");
                assertEquals(true, str.contains("helena@example.com"), "toString deve conter o email");
                assertEquals(true, str.contains("senhaHelena"), "toString deve conter a senha");
                assertEquals(true, str.contains("ADMIN") || str.contains("admin"), "toString deve conter o role");
        }

        @Test
        void testDefaultNoArgsValues() {
                User user = new User();
                assertNull(user.getId(), "Por padrão, id deve ser null");
                assertNull(user.getFirstName(), "Por padrão, firstName deve ser null");
                assertNull(user.getLastName(), "Por padrão, lastName deve ser null");
                assertNull(user.getEmail(), "Por padrão, email deve ser null");
                assertNull(user.getPassword(), "Por padrão, password deve ser null");
                assertNull(user.getRole(), "Por padrão, role deve ser null");
                String str = user.toString();
                assertNotNull(str, "toString() não deve retornar null mesmo com campos nulos");
        }

        @Test
        void testHashCodeStability() {
                User user = User.builder()
                                .id(9L)
                                .firstName("Isabela")
                                .lastName("Silva")
                                .email("isabela@example.com")
                                .password("senhaIsabela")
                                .role(Role.USER)
                                .build();
                int hash1 = user.hashCode();
                int hash2 = user.hashCode();
                assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
        }

        @Test
        void testCanEqual() {
                User user = User.builder()
                                .id(10L)
                                .firstName("Joana")
                                .lastName("Souza")
                                .email("joana@example.com")
                                .password("senhaJoana")
                                .role(Role.ADMIN)
                                .build();
                assertEquals(true, user.canEqual(user), "Um objeto deve poder comparar consigo mesmo via canEqual()");
                assertEquals(false, user.canEqual("Outro Tipo"),
                                "canEqual deve retornar false para objeto de tipo diferente");
                FakeUser fake = new FakeUser();
                assertEquals(false, user.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
        }

        @Test
        void testEqualsSameInstance() {
                User user = User.builder()
                                .id(11L)
                                .firstName("Kleber")
                                .lastName("Pires")
                                .email("kleber@example.com")
                                .password("senhaKleber")
                                .role(Role.USER)
                                .build();
                assertEquals(true, user.equals(user), "Um objeto deve ser igual a si mesmo");
        }

        @Test
        void testEqualsNull() {
                User user = User.builder()
                                .id(12L)
                                .firstName("Larissa")
                                .lastName("Melo")
                                .email("larissa@example.com")
                                .password("senhaLarissa")
                                .role(Role.ADMIN)
                                .build();
                assertEquals(false, user.equals(null), "Um objeto não deve ser igual a null");
        }

        @Test
        void testCanEqualDifferentInstances() {
                User user1 = User.builder()
                                .id(13L)
                                .firstName("Marcos")
                                .lastName("Ferreira")
                                .email("marcos@example.com")
                                .password("senhaMarcos")
                                .role(Role.USER)
                                .build();
                User user2 = User.builder()
                                .id(13L)
                                .firstName("Marcos")
                                .lastName("Ferreira")
                                .email("marcos@example.com")
                                .password("senhaMarcos")
                                .role(Role.USER)
                                .build();
                assertEquals(true, user1.canEqual(user2), "Objetos do mesmo tipo devem retornar true em canEqual()");
        }

        static class FakeUser extends User {
                @Override
                public boolean canEqual(Object other) {
                        return false;
                }
        }
}