package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationResponseDTOTests {
        private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        @Test
        void testNoArgsConstructorAndSetters() {
                AuthenticationResponseDTO response = new AuthenticationResponseDTO();
                response.setAccessToken("access123");
                response.setRefreshToken("refresh123");

                assertEquals("access123", response.getAccessToken());
                assertEquals("refresh123", response.getRefreshToken());
        }

        @Test
        void testAllArgsConstructor() {
                AuthenticationResponseDTO response = new AuthenticationResponseDTO("accessAll", "refreshAll");
                assertEquals("accessAll", response.getAccessToken());
                assertEquals("refreshAll", response.getRefreshToken());
        }

        @Test
        void testBuilder() {
                AuthenticationResponseDTO response = AuthenticationResponseDTO.builder()
                                .accessToken("accessBuilder")
                                .refreshToken("refreshBuilder")
                                .build();
                assertEquals("accessBuilder", response.getAccessToken());
                assertEquals("refreshBuilder", response.getRefreshToken());
        }

        @Test
        void testJsonSerializationAndDeserialization() throws Exception {
                AuthenticationResponseDTO response = AuthenticationResponseDTO.builder()
                                .accessToken("token123")
                                .refreshToken("refToken123")
                                .build();
                String json = mapper.writeValueAsString(response);
                assertEquals(true, json.contains("\"access_token\":\"token123\""),
                                "JSON deve conter \"access_token\":\"token123\"");
                assertEquals(true, json.contains("\"refresh_token\":\"refToken123\""),
                                "JSON deve conter \"refresh_token\":\"refToken123\"");

                AuthenticationResponseDTO deserialized = mapper.readValue(json, AuthenticationResponseDTO.class);
                assertEquals(response, deserialized, "O objeto desserializado deve ser igual ao original");
        }

        @Test
        void testEqualsAndHashCode() {
                AuthenticationResponseDTO res1 = AuthenticationResponseDTO.builder()
                                .accessToken("token")
                                .refreshToken("refresh")
                                .build();
                AuthenticationResponseDTO res2 = AuthenticationResponseDTO.builder()
                                .accessToken("token")
                                .refreshToken("refresh")
                                .build();
                assertEquals(res1, res2, "Objetos com valores idênticos devem ser iguais");
                assertEquals(res1.hashCode(), res2.hashCode(), "Hashcodes de objetos iguais devem ser iguais");

                res2.setRefreshToken("different");
                assertNotEquals(res1, res2, "Objetos devem ser diferentes se algum campo divergir");
        }

        @Test
        void testEqualsWithNullFields() {
                AuthenticationResponseDTO res1 = new AuthenticationResponseDTO();
                AuthenticationResponseDTO res2 = new AuthenticationResponseDTO();
                assertEquals(res1, res2, "Dois objetos com campos nulos devem ser iguais");
                assertEquals(res1.hashCode(), res2.hashCode(), "Hashcodes devem ser iguais quando os campos são nulos");
        }

        @Test
        void testNotEqualsDifferentType() {
                AuthenticationResponseDTO res = AuthenticationResponseDTO.builder()
                                .accessToken("token")
                                .refreshToken("refresh")
                                .build();
                assertNotEquals("Some String", res,
                                "AuthenticationResponseDTO não deve ser igual a um objeto de outro tipo");
        }

        @Test
        void testToString() {
                AuthenticationResponseDTO res = AuthenticationResponseDTO.builder()
                                .accessToken("accessToString")
                                .refreshToken("refreshToString")
                                .build();
                String str = res.toString();
                assertNotNull(str, "toString() não deve retornar null");
                assertEquals(true, str.contains("accessToString"), "toString deve conter o accessToken");
                assertEquals(true, str.contains("refreshToString"), "toString deve conter o refreshToken");
        }

        @Test
        void testDefaultNoArgsValues() {
                AuthenticationResponseDTO res = new AuthenticationResponseDTO();
                assertNull(res.getAccessToken(), "Por padrão, accessToken deve ser null");
                assertNull(res.getRefreshToken(), "Por padrão, refreshToken deve ser null");
                String str = res.toString();
                assertNotNull(str, "toString() não deve retornar null mesmo com valores nulos");
        }

        @Test
        void testHashCodeStability() {
                AuthenticationResponseDTO res = AuthenticationResponseDTO.builder()
                                .accessToken("stable")
                                .refreshToken("stable")
                                .build();
                int hash1 = res.hashCode();
                int hash2 = res.hashCode();
                assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
        }

        @Test
        void testCanEqual() {
                AuthenticationResponseDTO res = AuthenticationResponseDTO.builder()
                                .accessToken("canEqual")
                                .refreshToken("test")
                                .build();
                assertEquals(true, res.canEqual(res), "Um objeto deve poder comparar consigo mesmo via canEqual()");
                assertEquals(false, res.canEqual("Not an AuthenticationResponseDTO"),
                                "canEqual deve retornar false ao comparar com objeto de tipo diferente");

                FakeAuthenticationResponseDTO fake = new FakeAuthenticationResponseDTO();
                assertEquals(false, res.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
        }

        @Test
        void testEqualsSameInstance() {
                AuthenticationResponseDTO res = AuthenticationResponseDTO.builder()
                                .accessToken("same")
                                .refreshToken("same")
                                .build();
                assertEquals(true, res.equals(res), "Um objeto deve ser igual a si mesmo");
        }

        @Test
        void testEqualsNull() {
                AuthenticationResponseDTO res = AuthenticationResponseDTO.builder()
                                .accessToken("nullTest")
                                .refreshToken("nullTest")
                                .build();
                assertEquals(false, res.equals(null), "Um objeto não deve ser igual a null");
        }

        @Test
        void testCanEqualDifferentInstances() {
                AuthenticationResponseDTO res1 = AuthenticationResponseDTO.builder()
                                .accessToken("diff")
                                .refreshToken("diff")
                                .build();
                AuthenticationResponseDTO res2 = AuthenticationResponseDTO.builder()
                                .accessToken("diff")
                                .refreshToken("diff")
                                .build();
                assertEquals(true, res1.canEqual(res2), "Objetos do mesmo tipo devem retornar true em canEqual()");
        }

        static class FakeAuthenticationResponseDTO extends AuthenticationResponseDTO {
                @Override
                public boolean canEqual(Object other) {
                        return false;
                }
        }
}