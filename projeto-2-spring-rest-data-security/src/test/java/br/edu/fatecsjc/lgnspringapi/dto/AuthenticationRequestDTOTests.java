package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationRequestDTOTests {
        private final Validator validator;
        private final ObjectMapper mapper;

        public AuthenticationRequestDTOTests() {
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                validator = factory.getValidator();
                mapper = new ObjectMapper();
                mapper.findAndRegisterModules();
        }

        @Test
        void testNoArgsConstructorAndSetters() {
                AuthenticationRequestDTO dto = new AuthenticationRequestDTO();
                dto.setEmail("test@example.com");
                dto.setPassword("secret");
                assertEquals("test@example.com", dto.getEmail());
                assertEquals("secret", dto.getPassword());
        }

        @Test
        void testAllArgsConstructor() {
                AuthenticationRequestDTO dto = new AuthenticationRequestDTO("all@example.com", "pass123");
                assertEquals("all@example.com", dto.getEmail());
                assertEquals("pass123", dto.getPassword());
        }

        @Test
        void testBuilder() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("builder@example.com")
                                .password("builderpass")
                                .build();
                assertEquals("builder@example.com", dto.getEmail());
                assertEquals("builderpass", dto.getPassword());
        }

        @Test
        void testJsonSerializationAndDeserialization() throws Exception {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("json@example.com")
                                .password("jsonpass")
                                .build();
                String json = mapper.writeValueAsString(dto);
                assertEquals(true, json.contains("json@example.com"), "JSON deve conter o email");
                assertEquals(true, json.contains("jsonpass"), "JSON deve conter a password");
                AuthenticationRequestDTO deserialized = mapper.readValue(json, AuthenticationRequestDTO.class);
                assertEquals(dto, deserialized, "O objeto desserializado deve ser igual ao original");
        }

        @Test
        void testValidationSuccess() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("valid@example.com")
                                .password("secret")
                                .build();
                Set<ConstraintViolation<AuthenticationRequestDTO>> violations = validator.validate(dto);
                assertEquals(true, violations.isEmpty(), "Não deve haver violações quando os campos são válidos");
        }

        @Test
        void testValidationInvalidEmail() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("notanemail")
                                .password("secret")
                                .build();
                Set<ConstraintViolation<AuthenticationRequestDTO>> violations = validator.validate(dto);
                assertEquals(false, violations.isEmpty(), "Deve haver violações para email inválido");
                boolean msgFound = violations.stream()
                                .anyMatch(v -> v.getMessage().equals("Formato de email inválido"));
                assertEquals(true, msgFound, "A mensagem 'Formato de email inválido' deve aparecer");
        }

        @Test
        void testValidationNullEmail() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .password("secret")
                                .build();
                Set<ConstraintViolation<AuthenticationRequestDTO>> violations = validator.validate(dto);
                assertEquals(false, violations.isEmpty(), "Email nulo deve gerar violações");
        }

        @Test
        void testValidationNullPassword() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("user@example.com")
                                .build();
                Set<ConstraintViolation<AuthenticationRequestDTO>> violations = validator.validate(dto);
                assertEquals(false, violations.isEmpty(), "Password nula deve gerar violações");
        }

        @Test
        void testEqualsAndHashCode() {
                AuthenticationRequestDTO dto1 = AuthenticationRequestDTO.builder()
                                .email("eq@example.com")
                                .password("pass")
                                .build();
                AuthenticationRequestDTO dto2 = AuthenticationRequestDTO.builder()
                                .email("eq@example.com")
                                .password("pass")
                                .build();
                assertEquals(dto1, dto2, "Objetos com valores idênticos devem ser iguais");
                assertEquals(dto1.hashCode(), dto2.hashCode(), "Hashcodes de objetos iguais devem ser iguais");
                dto2.setPassword("different");
                assertNotEquals(dto1, dto2, "Objetos devem ser diferentes se algum campo divergir");
        }

        @Test
        void testEqualsWithNullFields() {
                AuthenticationRequestDTO dto1 = new AuthenticationRequestDTO();
                AuthenticationRequestDTO dto2 = new AuthenticationRequestDTO();
                assertEquals(dto1, dto2, "Objetos com campos nulos devem ser iguais");
                assertEquals(dto1.hashCode(), dto2.hashCode(), "Hashcodes devem ser iguais quando os campos são nulos");
        }

        @Test
        void testEqualsWhenOneFieldIsNull() {
                AuthenticationRequestDTO dto1 = AuthenticationRequestDTO.builder()
                                .email("test@example.com")
                                .password("pass")
                                .build();
                AuthenticationRequestDTO dto2 = AuthenticationRequestDTO.builder()
                                .email(null)
                                .password("pass")
                                .build();
                assertNotEquals(dto1, dto2, "Objetos devem ser diferentes se um campo (email) divergir");
        }

        @Test
        void testNotEqualsDifferentType() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("type@example.com")
                                .password("pass")
                                .build();
                assertNotEquals("Some String", dto, "Não deve ser igual a objeto de outro tipo");
        }

        @Test
        void testToString() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("str@example.com")
                                .password("strpass")
                                .build();
                String str = dto.toString();
                assertNotNull(str, "toString() não deve retornar null");
                assertEquals(true, str.contains("str@example.com"), "toString deve conter o email");
                assertEquals(true, str.contains("strpass"), "toString deve conter a password");
        }

        @Test
        void testDefaultNoArgsValues() {
                AuthenticationRequestDTO dto = new AuthenticationRequestDTO();
                assertNull(dto.getEmail(), "Por padrão, email deve ser null");
                assertNull(dto.getPassword(), "Por padrão, password deve ser null");
                String str = dto.toString();
                assertNotNull(str, "toString() não deve retornar null mesmo com valores nulos");
        }

        @Test
        void testHashCodeStability() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("hash@example.com")
                                .password("stable")
                                .build();
                int hash1 = dto.hashCode();
                int hash2 = dto.hashCode();
                assertEquals(hash1, hash2, "O hashCode deve ser estável entre chamadas");
        }

        @Test
        void testCanEqual() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("can@example.com")
                                .password("secret")
                                .build();
                assertEquals(true, dto.canEqual(dto), "Um objeto deve poder comparar consigo mesmo via canEqual()");
                assertEquals(false, dto.canEqual("Not an AuthenticationRequestDTO"),
                                "canEqual deve retornar false ao comparar com objeto de tipo diferente");

                FakeAuthenticationRequestDTO fake = new FakeAuthenticationRequestDTO();
                assertEquals(false, dto.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
        }

        @Test
        void testEqualsSameInstance() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("same@example.com")
                                .password("password")
                                .build();
                assertEquals(true, dto.equals(dto), "Um objeto deve ser igual a si mesmo");
        }

        @Test
        void testEqualsNull() {
                AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                                .email("null@example.com")
                                .password("password")
                                .build();
                assertEquals(false, dto.equals(null), "Um objeto não deve ser igual a null");
        }

        @Test
        void testCanEqualDifferentInstances() {
                AuthenticationRequestDTO dto1 = AuthenticationRequestDTO.builder()
                                .email("diff@example.com")
                                .password("pass")
                                .build();
                AuthenticationRequestDTO dto2 = AuthenticationRequestDTO.builder()
                                .email("diff@example.com")
                                .password("pass")
                                .build();
                assertEquals(true, dto1.canEqual(dto2), "Objetos do mesmo tipo devem retornar true em canEqual()");
        }

        static class FakeAuthenticationRequestDTO extends AuthenticationRequestDTO {
                @Override
                public boolean canEqual(Object other) {
                        return false;
                }
        }
}