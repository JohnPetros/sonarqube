package br.edu.fatecsjc.lgnspringapi.enums;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class RoleTests {
    @Test
     void testUserGetAuthorities() {
        List<SimpleGrantedAuthority> authorities = Role.USER.getAuthorities();
        assertEquals(1, authorities.size(), "Role.USER deve ter somente 1 autoridade.");

        SimpleGrantedAuthority roleAuthority = authorities.get(0);
        assertEquals("ROLE_USER", roleAuthority.getAuthority(), "A autoridade deve ser 'ROLE_USER'");
    }

    @Test
     void testAdminGetAuthorities() {
        List<SimpleGrantedAuthority> authorities = Role.ADMIN.getAuthorities();
        assertEquals(3, authorities.size(), "Role.ADMIN deve ter 3 autoridades (2 permissões + ROLE_ADMIN).");

        boolean hasAdminCreate = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("admin:create"));
        boolean hasAdminUpdate = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("admin:update"));
        boolean hasRoleAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        assertTrue(hasAdminCreate, "Deve existir a autoridade 'admin:create'.");
        assertTrue(hasAdminUpdate, "Deve existir a autoridade 'admin:update'.");
        assertTrue(hasRoleAdmin, "Deve existir a autoridade 'ROLE_ADMIN'.");
    }

    @Test
     void testAdminGetRoleAuthorities() {
        List<SimpleGrantedAuthority> roleAuthorities = Role.ADMIN.getRoleAuthorities();
        assertEquals(4, roleAuthorities.size(), "Role.ADMIN deve ter 4 autoridades após getRoleAuthorities().");

        long countRoleAdmin = roleAuthorities.stream()
                .filter(auth -> auth.getAuthority().equals("ROLE_ADMIN"))
                .count();
        assertEquals(2, countRoleAdmin, "Deve haver 2 ocorrências de 'ROLE_ADMIN'.");

        boolean hasAdminCreate = roleAuthorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("admin:create"));
        boolean hasAdminUpdate = roleAuthorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("admin:update"));
        assertTrue(hasAdminCreate, "Deve existir a autoridade 'admin:create'.");
        assertTrue(hasAdminUpdate, "Deve existir a autoridade 'admin:update'.");
    }
}