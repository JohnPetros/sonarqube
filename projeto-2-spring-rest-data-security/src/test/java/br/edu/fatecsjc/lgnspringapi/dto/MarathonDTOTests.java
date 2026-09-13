package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MarathonDTOTests {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void testNoArgsConstructorAndSetters() {
        MarathonDTO dto = new MarathonDTO();
        dto.setId(1L);
        dto.setPeso("70kg");
        dto.setScore("100");
        assertEquals(1L, dto.getId());
        assertEquals("70kg", dto.getPeso());
        assertEquals("100", dto.getScore());
    }

    @Test
    void testAllArgsConstructor() {
        MarathonDTO dto = new MarathonDTO(2L, "80kg", "150");
        assertEquals(2L, dto.getId());
        assertEquals("80kg", dto.getPeso());
        assertEquals("150", dto.getScore());
    }

    @Test
    void testBuilder() {
        MarathonDTO dto = MarathonDTO.builder()
                .id(3L)
                .peso("75kg")
                .score("200")
                .build();
        assertEquals(3L, dto.getId());
        assertEquals("75kg", dto.getPeso());
        assertEquals("200", dto.getScore());
    }

    @Test
    void testJsonSerializationAndDeserialization() throws Exception {
        MarathonDTO dto = MarathonDTO.builder()
                .id(4L)
                .peso("85kg")
                .score("250")
                .build();
        String json = mapper.writeValueAsString(dto);
        assertEquals(true, json.contains("\"id\":4"), "O JSON deve conter \"id\":4");
        assertEquals(true, json.contains("\"peso\":\"85kg\""), "O JSON deve conter \"peso\":\"85kg\"");
        assertEquals(true, json.contains("\"score\":\"250\""), "O JSON deve conter \"score\":\"250\"");
        MarathonDTO deserialized = mapper.readValue(json, MarathonDTO.class);
        assertEquals(dto, deserialized, "O objeto desserializado deve ser igual ao original");
    }

    @Test
    void testJsonIncludeExcludesNullProperties() throws Exception {
        MarathonDTO dto = MarathonDTO.builder()
                .peso("90kg")
                .build();
        String json = mapper.writeValueAsString(dto);
        assertEquals(true, json.contains("\"peso\":\"90kg\""), "O JSON deve conter o peso");
        assertEquals(false, json.contains("id"), "JSON não deve conter 'id' se for null");
        assertEquals(false, json.contains("score"), "JSON não deve conter 'score' se for null");
    }

    @Test
    void testEqualsAndHashCode() {
        MarathonDTO dto1 = MarathonDTO.builder()
                .id(5L)
                .peso("95kg")
                .score("300")
                .build();
        MarathonDTO dto2 = MarathonDTO.builder()
                .id(5L)
                .peso("95kg")
                .score("300")
                .build();
        assertEquals(dto1, dto2, "Objetos com os mesmos valores devem ser iguais");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Hashcodes de objetos iguais devem ser iguais");
        dto2.setScore("310");
        assertNotEquals(dto1, dto2, "Objetos devem ser diferentes se algum campo divergir");
    }

    @Test
    void testEqualsWithNullFields() {
        MarathonDTO dto1 = new MarathonDTO();
        MarathonDTO dto2 = new MarathonDTO();
        assertEquals(dto1, dto2, "Dois objetos com campos nulos devem ser iguais");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Hashcodes devem ser iguais quando os campos são nulos");
    }

    @Test
    void testNotEqualsDifferentType() {
        MarathonDTO dto = MarathonDTO.builder()
                .id(6L)
                .peso("100kg")
                .score("350")
                .build();
        assertNotEquals("Uma String", dto, "MarathonDTO não deve ser igual a um objeto de outro tipo");
    }

    @Test
    void testToString() {
        MarathonDTO dto = MarathonDTO.builder()
                .id(7L)
                .peso("105kg")
                .score("400")
                .build();
        String str = dto.toString();
        assertNotNull(str, "toString() não deve retornar null");
        assertEquals(true, str.contains("7"), "toString deve conter o id");
        assertEquals(true, str.contains("105kg"), "toString deve conter o peso");
        assertEquals(true, str.contains("400"), "toString deve conter o score");
    }

    @Test
    void testDefaultNoArgsValues() {
        MarathonDTO dto = new MarathonDTO();
        assertNull(dto.getId(), "Por padrão, id deve ser null");
        assertNull(dto.getPeso(), "Por padrão, peso deve ser null");
        assertNull(dto.getScore(), "Por padrão, score deve ser null");
        String str = dto.toString();
        assertNotNull(str, "toString() não deve retornar null mesmo com campos nulos");
    }

    @Test
    void testHashCodeStability() {
        MarathonDTO dto = MarathonDTO.builder()
                .id(8L)
                .peso("110kg")
                .score("450")
                .build();
        int hash1 = dto.hashCode();
        int hash2 = dto.hashCode();
        assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
    }

    @Test
    void testCanEqual() {
        MarathonDTO dto = MarathonDTO.builder()
                .id(9L)
                .peso("115kg")
                .score("500")
                .build();
        assertEquals(true, dto.canEqual(dto), "Um objeto deve poder comparar consigo mesmo via canEqual()");
        assertEquals(false, dto.canEqual("Outro Tipo"), "canEqual deve retornar false para objeto de tipo diferente");
        FakeMarathonDTO fake = new FakeMarathonDTO();
        assertEquals(false, dto.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
    }

    @Test
    void testEqualsSameInstance() {
        MarathonDTO dto = MarathonDTO.builder()
                .id(10L)
                .peso("120kg")
                .score("550")
                .build();
        assertEquals(true, dto.equals(dto), "Um objeto deve ser igual a si mesmo");
    }

    @Test
    void testEqualsNull() {
        MarathonDTO dto = MarathonDTO.builder()
                .id(10L)
                .peso("120kg")
                .score("550")
                .build();
        assertEquals(false, dto.equals(null), "Um objeto não deve ser igual a null");
    }

    @Test
    void testCanEqualDifferentInstances() {
        MarathonDTO dto1 = MarathonDTO.builder()
                .id(11L)
                .peso("130kg")
                .score("600")
                .build();
        MarathonDTO dto2 = MarathonDTO.builder()
                .id(11L)
                .peso("130kg")
                .score("600")
                .build();
        assertEquals(true, dto1.canEqual(dto2), "Objetos do mesmo tipo devem retornar true em canEqual()");
    }

    static class FakeMarathonDTO extends MarathonDTO {
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }
}