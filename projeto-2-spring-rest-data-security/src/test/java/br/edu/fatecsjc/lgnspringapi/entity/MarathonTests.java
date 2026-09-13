package br.edu.fatecsjc.lgnspringapi.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MarathonTests {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void testNoArgsConstructorAndSetters() {
        Marathon marathon = new Marathon();
        marathon.setId(1L);
        marathon.setPeso("70kg");
        marathon.setScore("100");
        Member member = Member.builder()
                .id(10L)
                .name("Atleta A")
                .age(30)
                .build();
        marathon.setMember(member);
        assertEquals(1L, marathon.getId());
        assertEquals("70kg", marathon.getPeso());
        assertEquals("100", marathon.getScore());
        assertNotNull(marathon.getMember());
        assertEquals(10L, marathon.getMember().getId());
        assertEquals("Atleta A", marathon.getMember().getName());
    }

    @Test
    void testAllArgsConstructorAndBuilder() {
        Member member = Member.builder()
                .id(11L)
                .name("Atleta B")
                .age(25)
                .build();
        Marathon marathon1 = new Marathon(2L, "80kg", "150", member);
        Marathon marathon2 = Marathon.builder()
                .id(2L)
                .peso("80kg")
                .score("150")
                .member(member)
                .build();
        assertEquals(marathon1, marathon2);
        assertEquals(marathon1.hashCode(), marathon2.hashCode());
    }

    @Test
    void testJsonSerializationAndDeserialization() throws Exception {
        Member member = Member.builder()
                .id(12L)
                .name("Atleta C")
                .age(28)
                .build();
        Marathon marathon = Marathon.builder()
                .id(3L)
                .peso("75kg")
                .score("200")
                .member(member)
                .build();
        String json = mapper.writeValueAsString(marathon);
        assertEquals(true, json.contains("\"id\":3"), "O JSON deve conter \"id\":3");
        assertEquals(true, json.contains("\"peso\":\"75kg\""), "O JSON deve conter 'peso':'75kg'");
        assertEquals(true, json.contains("\"score\":\"200\""), "O JSON deve conter 'score':'200'");
        assertEquals(false, json.contains("member"), "O JSON não deve conter 'member'");
        Marathon deserialized = mapper.readValue(json, Marathon.class);
        assertEquals(marathon.getId(), deserialized.getId());
        assertEquals(marathon.getPeso(), deserialized.getPeso());
        assertEquals(marathon.getScore(), deserialized.getScore());
        assertNull(deserialized.getMember(), "Após desserialização, 'member' deve ser null");
    }

    @Test
    void testEqualsAndHashCode() {
        Member member = Member.builder()
                .id(13L)
                .name("Atleta D")
                .age(32)
                .build();
        Marathon m1 = Marathon.builder()
                .id(4L)
                .peso("65kg")
                .score("120")
                .member(member)
                .build();
        Marathon m2 = Marathon.builder()
                .id(4L)
                .peso("65kg")
                .score("120")
                .member(member)
                .build();
        assertEquals(m1, m2, "Objetos com mesmos valores devem ser iguais");
        assertEquals(m1.hashCode(), m2.hashCode(), "Hashcodes devem ser iguais para objetos iguais");
        m2.setScore("130");
        assertNotEquals(m1, m2, "Objetos devem ser diferentes se algum campo divergir");
    }

    @Test
    void testEqualsWithNullFields() {
        Marathon m1 = new Marathon();
        Marathon m2 = new Marathon();
        assertEquals(m1, m2, "Dois objetos com campos nulos devem ser iguais");
        assertEquals(m1.hashCode(), m2.hashCode(), "Hashcodes devem ser iguais quando os campos são nulos");
    }

    @Test
    void testNotEqualsDifferentType() {
        Marathon m = Marathon.builder()
                .id(5L)
                .peso("90kg")
                .score("180")
                .build();
        assertNotEquals("Alguma String", m, "Marathon não deve ser igual a um objeto de outro tipo");
    }

    @Test
    void testToString() {
        Marathon m = Marathon.builder()
                .id(6L)
                .peso("55kg")
                .score("90")
                .build();
        String s = m.toString();
        assertNotNull(s, "toString() não deve retornar null");
        assertEquals(true, s.contains("6"), "toString deve conter o id");
        assertEquals(true, s.contains("55kg"), "toString deve conter o valor de peso");
        assertEquals(true, s.contains("90"), "toString deve conter o score");
    }

    @Test
    void testDefaultNoArgsValues() {
        Marathon m = new Marathon();
        assertNull(m.getId(), "Por padrão, id deve ser null");
        assertNull(m.getPeso(), "Por padrão, peso deve ser null");
        assertNull(m.getScore(), "Por padrão, score deve ser null");
        assertNull(m.getMember(), "Por padrão, member deve ser null");
        String s = m.toString();
        assertNotNull(s, "toString() não deve retornar null mesmo com campos nulos");
    }

    @Test
    void testHashCodeStability() {
        Marathon m = Marathon.builder()
                .id(7L)
                .peso("100kg")
                .score("250")
                .build();
        int h1 = m.hashCode();
        int h2 = m.hashCode();
        assertEquals(h1, h2, "hashCode deve ser estável entre chamadas");
    }

    @Test
    void testCanEqual() {
        Marathon m = Marathon.builder()
                .id(8L)
                .peso("85kg")
                .score("210")
                .build();
        assertEquals(true, m.canEqual(m), "Um objeto deve poder comparar consigo mesmo via canEqual()");
        assertEquals(false, m.canEqual("Outro Tipo"), "canEqual deve retornar false para objeto de tipo diferente");
        FakeMarathon fake = new FakeMarathon();
        assertEquals(false, m.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
    }

    @Test
    void testEqualsSameInstance() {
        Marathon m = Marathon.builder()
                .id(9L)
                .peso("95kg")
                .score("300")
                .build();
        assertEquals(true, m.equals(m), "Um objeto deve ser igual a si mesmo");
    }

    @Test
    void testEqualsNull() {
        Marathon m = Marathon.builder()
                .id(9L)
                .peso("95kg")
                .score("300")
                .build();
        assertEquals(false, m.equals(null), "Um objeto não deve ser igual a null");
    }

    @Test
    void testCanEqualDifferentInstances() {
        Marathon m1 = Marathon.builder()
                .id(10L)
                .peso("70kg")
                .score("150")
                .build();
        Marathon m2 = Marathon.builder()
                .id(10L)
                .peso("70kg")
                .score("150")
                .build();
        assertEquals(true, m1.canEqual(m2), "Objetos do mesmo tipo devem retornar true em canEqual()");
    }

    static class FakeMarathon extends Marathon {
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }
}