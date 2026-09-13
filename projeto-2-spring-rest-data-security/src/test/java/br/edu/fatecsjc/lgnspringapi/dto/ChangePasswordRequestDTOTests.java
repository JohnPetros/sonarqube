package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ChangePasswordRequestDTOTests {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void testNoArgsConstructorAndSetters() {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO(null, null, null);
        dto.setCurrentPassword("oldPass");
        dto.setNewPassword("newPass");
        dto.setConfirmationPassword("confirmPass");

        assertEquals("oldPass", dto.getCurrentPassword());
        assertEquals("newPass", dto.getNewPassword());
        assertEquals("confirmPass", dto.getConfirmationPassword());
    }

    @Test
    void testBuilder() {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("builderOld")
                .newPassword("builderNew")
                .confirmationPassword("builderConfirm")
                .build();

        assertEquals("builderOld", dto.getCurrentPassword());
        assertEquals("builderNew", dto.getNewPassword());
        assertEquals("builderConfirm", dto.getConfirmationPassword());
    }

    @Test
    void testJsonSerializationAndDeserialization() throws Exception {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("jsonOld")
                .newPassword("jsonNew")
                .confirmationPassword("jsonConfirm")
                .build();
        String json = mapper.writeValueAsString(dto);
        assertEquals(true, json.contains("currentPassword"), "JSON deve conter 'currentPassword'");
        assertEquals(true, json.contains("newPassword"), "JSON deve conter 'newPassword'");
        assertEquals(true, json.contains("confirmationPassword"), "JSON deve conter 'confirmationPassword'");

        ChangePasswordRequestDTO deserialized = mapper.readValue(json, ChangePasswordRequestDTO.class);
        assertEquals(dto, deserialized);
    }

    @Test
    void testEqualsAndHashCode() {
        ChangePasswordRequestDTO dto1 = ChangePasswordRequestDTO.builder()
                .currentPassword("pass")
                .newPassword("new")
                .confirmationPassword("confirm")
                .build();
        ChangePasswordRequestDTO dto2 = ChangePasswordRequestDTO.builder()
                .currentPassword("pass")
                .newPassword("new")
                .confirmationPassword("confirm")
                .build();
        assertEquals(dto1, dto2, "Objetos com valores idênticos devem ser iguais");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Hashcodes de objetos iguais devem ser iguais");
        dto2.setNewPassword("different");
        assertNotEquals(dto1, dto2, "Objetos devem ser diferentes se algum campo divergir");
    }

    @Test
    void testEqualsWithNullFields() {
        ChangePasswordRequestDTO dto1 = new ChangePasswordRequestDTO(null, null, null);
        ChangePasswordRequestDTO dto2 = new ChangePasswordRequestDTO(null, null, null);
        assertEquals(dto1, dto2, "Dois objetos com campos nulos devem ser iguais");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Hashcodes devem ser iguais quando os campos são nulos");
    }

    @Test
    void testNotEqualsDifferentType() {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("old")
                .newPassword("new")
                .confirmationPassword("confirm")
                .build();
        assertNotEquals("Some String", dto, "ChangePasswordRequestDTO não deve ser igual a um objeto de outro tipo");
    }

    @Test
    void testToString() {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("current")
                .newPassword("new")
                .confirmationPassword("confirm")
                .build();
        String str = dto.toString();
        assertNotNull(str, "toString() não deve retornar null");
        assertEquals(true, str.contains("current"), "toString deve conter o valor de currentPassword");
        assertEquals(true, str.contains("new"), "toString deve conter o valor de newPassword");
        assertEquals(true, str.contains("confirm"), "toString deve conter o valor de confirmationPassword");
    }

    @Test
    void testDefaultNoArgsValues() {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO(null, null, null);
        assertNull(dto.getCurrentPassword(), "Por padrão, currentPassword deve ser null");
        assertNull(dto.getNewPassword(), "Por padrão, newPassword deve ser null");
        assertNull(dto.getConfirmationPassword(), "Por padrão, confirmationPassword deve ser null");
        String str = dto.toString();
        assertNotNull(str, "toString() não deve retornar null mesmo com valores nulos");
    }

    @Test
    void testHashCodeStability() {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("stable")
                .newPassword("stable")
                .confirmationPassword("stable")
                .build();
        int hash1 = dto.hashCode();
        int hash2 = dto.hashCode();
        assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
    }

    @Test
    void testCanEqual() {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("x")
                .newPassword("y")
                .confirmationPassword("z")
                .build();
        assertEquals(true, dto.canEqual(dto), "Um objeto deve poder comparar consigo mesmo via canEqual()");
        assertEquals(false, dto.canEqual("Not a ChangePasswordRequestDTO"),
                "canEqual deve retornar false para objeto de tipo diferente");
        FakeChangePasswordRequestDTO fake = new FakeChangePasswordRequestDTO(null, null, null);
        assertEquals(false, dto.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
    }

    @Test
    void testEqualsSameInstance() {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("self")
                .newPassword("selfNew")
                .confirmationPassword("selfConfirm")
                .build();
        assertEquals(dto, dto, "Um objeto deve ser igual a si mesmo");
    }

    @Test
    void testEqualsNull() {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("nonNull")
                .newPassword("nonNull")
                .confirmationPassword("nonNull")
                .build();
        assertNotEquals(null, dto, "Um objeto não deve ser igual a null");
    }

    @Test
    void testCanEqualDifferentInstances() {
        ChangePasswordRequestDTO dto1 = ChangePasswordRequestDTO.builder()
                .currentPassword("A")
                .newPassword("B")
                .confirmationPassword("C")
                .build();
        ChangePasswordRequestDTO dto2 = ChangePasswordRequestDTO.builder()
                .currentPassword("A")
                .newPassword("B")
                .confirmationPassword("C")
                .build();
        assertEquals(true, dto1.canEqual(dto2), "Objetos do mesmo tipo devem retornar true em canEqual()");
    }

    static class FakeChangePasswordRequestDTO extends ChangePasswordRequestDTO {
        FakeChangePasswordRequestDTO(String currentPassword, String newPassword, String confirmationPassword) {
            super(currentPassword, newPassword, confirmationPassword);
        }

        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }
}
