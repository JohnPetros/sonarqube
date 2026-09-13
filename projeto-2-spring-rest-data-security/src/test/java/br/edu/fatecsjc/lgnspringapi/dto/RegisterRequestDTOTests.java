package br.edu.fatecsjc.lgnspringapi.dto;

import br.edu.fatecsjc.lgnspringapi.enums.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RegisterRequestDTOTests {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void testNoArgsConstructorAndSetters() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setFirstname("João");
        dto.setLastname("Silva");
        dto.setEmail("joao.silva@example.com");
        dto.setPassword("senha123");
        dto.setRole(Role.USER);

        assertEquals("João", dto.getFirstname());
        assertEquals("Silva", dto.getLastname());
        assertEquals("joao.silva@example.com", dto.getEmail());
        assertEquals("senha123", dto.getPassword());
        assertEquals(Role.USER, dto.getRole());
    }

    @Test
    void testAllArgsConstructor() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Maria",
                "Oliveira",
                "maria.oliveira@example.com",
                "senha456",
                Role.ADMIN);
        assertEquals("Maria", dto.getFirstname());
        assertEquals("Oliveira", dto.getLastname());
        assertEquals("maria.oliveira@example.com", dto.getEmail());
        assertEquals("senha456", dto.getPassword());
        assertEquals(Role.ADMIN, dto.getRole());
    }

    @Test
    void testBuilder() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("Pedro")
                .lastname("Costa")
                .email("pedro.costa@example.com")
                .password("senha789")
                .role(Role.USER)
                .build();
        assertEquals("Pedro", dto.getFirstname());
        assertEquals("Costa", dto.getLastname());
        assertEquals("pedro.costa@example.com", dto.getEmail());
        assertEquals("senha789", dto.getPassword());
        assertEquals(Role.USER, dto.getRole());
    }

    @Test
    void testJsonSerializationAndDeserialization() throws Exception {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("Ana")
                .lastname("Souza")
                .email("ana.souza@example.com")
                .password("minhasenha")
                .role(Role.ADMIN)
                .build();
        String json = mapper.writeValueAsString(dto);
        assertEquals(true, json.contains("Ana"), "O JSON deve conter o primeiro nome");
        assertEquals(true, json.contains("Souza"), "O JSON deve conter o sobrenome");
        assertEquals(true, json.contains("ana.souza@example.com"), "O JSON deve conter o email");
        assertEquals(true, json.contains("minhasenha"), "O JSON deve conter a senha");
        assertEquals(true, json.contains("ADMIN") || json.contains("admin"), "O JSON deve conter o role");

        RegisterRequestDTO deserialized = mapper.readValue(json, RegisterRequestDTO.class);
        assertEquals(dto, deserialized, "O objeto desserializado deve ser igual ao original");
    }

    @Test
    void testEqualsAndHashCode() {
        RegisterRequestDTO dto1 = RegisterRequestDTO.builder()
                .firstname("Carlos")
                .lastname("Pereira")
                .email("carlos.pereira@example.com")
                .password("pass123")
                .role(Role.USER)
                .build();
        RegisterRequestDTO dto2 = RegisterRequestDTO.builder()
                .firstname("Carlos")
                .lastname("Pereira")
                .email("carlos.pereira@example.com")
                .password("pass123")
                .role(Role.USER)
                .build();
        assertEquals(dto1, dto2, "Objetos com mesmos valores devem ser iguais");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Hashcodes de objetos iguais devem ser iguais");

        dto2.setPassword("diferente");
        assertNotEquals(dto1, dto2, "Objetos devem ser diferentes quando algum campo divergir");
    }

    @Test
    void testEqualsWithNullFields() {
        RegisterRequestDTO dto1 = new RegisterRequestDTO();
        RegisterRequestDTO dto2 = new RegisterRequestDTO();
        assertEquals(dto1, dto2, "Dois objetos com campos nulos devem ser iguais");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Hashcodes devem ser iguais quando os campos são nulos");
    }

    @Test
    void testNotEqualsDifferentType() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("Roberto")
                .lastname("Dias")
                .email("roberto.dias@example.com")
                .password("senha")
                .role(Role.ADMIN)
                .build();
        assertNotEquals("Alguma String", dto, "RegisterRequestDTO não deve ser igual a um objeto de outro tipo");
    }

    @Test
    void testToString() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("Marcela")
                .lastname("Lima")
                .email("marcela.lima@example.com")
                .password("senha123")
                .role(Role.ADMIN)
                .build();
        String str = dto.toString();
        assertNotNull(str, "toString() não deve retornar null");
        assertEquals(true, str.contains("Marcela"), "toString deve conter o firstname");
        assertEquals(true, str.contains("Lima"), "toString deve conter o lastname");
        assertEquals(true, str.contains("marcela.lima@example.com"), "toString deve conter o email");
        assertEquals(true, str.contains("senha123"), "toString deve conter a password");
        assertEquals(true, str.contains("ADMIN") || str.contains("admin"), "toString deve conter o role");
    }

    @Test
    void testDefaultNoArgsValues() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        assertNull(dto.getFirstname(), "Por padrão, firstname deve ser null");
        assertNull(dto.getLastname(), "Por padrão, lastname deve ser null");
        assertNull(dto.getEmail(), "Por padrão, email deve ser null");
        assertNull(dto.getPassword(), "Por padrão, password deve ser null");
        assertNull(dto.getRole(), "Por padrão, role deve ser null");

        String str = dto.toString();
        assertNotNull(str, "toString() não deve retornar null mesmo com campos nulos");
    }

    @Test
    void testHashCodeStability() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("Lucas")
                .lastname("Mendes")
                .email("lucas.mendes@example.com")
                .password("minhaSenha")
                .role(Role.USER)
                .build();
        int hash1 = dto.hashCode();
        int hash2 = dto.hashCode();
        assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
    }

    @Test
    void testCanEqual() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("Fabiana")
                .lastname("Rocha")
                .email("fabiana.rocha@example.com")
                .password("senhaFab")
                .role(Role.USER)
                .build();
        assertEquals(true, dto.canEqual(dto), "Um objeto deve poder comparar consigo mesmo via canEqual()");
        assertEquals(false, dto.canEqual("Não é RegisterRequestDTO"),
                "canEqual deve retornar false para objeto de tipo diferente");

        FakeRegisterRequestDTO fake = new FakeRegisterRequestDTO();
        assertEquals(false, dto.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
    }

    @Test
    void testEqualsSameInstance() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("Igor")
                .lastname("Ferreira")
                .email("igor.ferreira@example.com")
                .password("senhaIgor")
                .role(Role.ADMIN)
                .build();
        assertEquals(dto, dto, "Um objeto deve ser igual a si mesmo");
    }

    @Test
    void testEqualsNull() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("Juliana")
                .lastname("Costa")
                .email("juliana.costa@example.com")
                .password("senhaJuliana")
                .role(Role.USER)
                .build();
        assertNotEquals(null, dto, "Um objeto não deve ser igual a null");
    }

    @Test
    void testCanEqualDifferentInstances() {
        RegisterRequestDTO dto1 = RegisterRequestDTO.builder()
                .firstname("Mariana")
                .lastname("Araujo")
                .email("mariana.araujo@example.com")
                .password("senhaMariana")
                .role(Role.USER)
                .build();
        RegisterRequestDTO dto2 = RegisterRequestDTO.builder()
                .firstname("Mariana")
                .lastname("Araujo")
                .email("mariana.araujo@example.com")
                .password("senhaMariana")
                .role(Role.USER)
                .build();
        assertEquals(true, dto1.canEqual(dto2), "Objetos do mesmo tipo devem retornar true em canEqual()");
    }

    static class FakeRegisterRequestDTO extends RegisterRequestDTO {
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }
}