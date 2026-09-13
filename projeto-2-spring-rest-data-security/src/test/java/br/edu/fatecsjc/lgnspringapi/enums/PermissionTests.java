package br.edu.fatecsjc.lgnspringapi.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class PermissionTests {
    @Test
     void testPermissionValues() {
        Permission[] permissions = Permission.values();
        assertEquals(2, permissions.length);

        Permission adminCreate = Permission.ADMIN_CREATE;
        assertNotNull(adminCreate);
        assertEquals("admin:create", adminCreate.getPermissionValue());

        Permission adminUpdate = Permission.ADMIN_UPDATE;
        assertNotNull(adminUpdate);
        assertEquals("admin:update", adminUpdate.getPermissionValue());
    }

    @Test
     void testValueOf() {
        Permission permission = Permission.valueOf("ADMIN_CREATE");
        assertNotNull(permission);
        assertEquals(Permission.ADMIN_CREATE, permission);
    }
}