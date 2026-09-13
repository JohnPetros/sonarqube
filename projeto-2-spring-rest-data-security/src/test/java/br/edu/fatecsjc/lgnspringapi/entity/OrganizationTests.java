package br.edu.fatecsjc.lgnspringapi.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrganizationTests {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void testNoArgsConstructorAndSetters() {
        Organization org = new Organization();
        org.setId(1L);
        org.setName("Org A");
        org.setInstituicaoEnsino("Instituição A");
        org.setPaisSede("Brasil");
        assertEquals(1L, org.getId());
        assertEquals("Org A", org.getName());
        assertEquals("Instituição A", org.getInstituicaoEnsino());
        assertEquals("Brasil", org.getPaisSede());
    }

    @Test
    void testAllArgsConstructor() {
        Organization org = new Organization(2L, "Org B", "Instituição B", "Argentina");
        assertEquals(2L, org.getId());
        assertEquals("Org B", org.getName());
        assertEquals("Instituição B", org.getInstituicaoEnsino());
        assertEquals("Argentina", org.getPaisSede());
    }

    @Test
    void testBuilder() {
        Organization org = Organization.builder()
                .id(3L)
                .name("Org C")
                .instituicaoEnsino("Instituição C")
                .paisSede("Chile")
                .build();
        assertEquals(3L, org.getId());
        assertEquals("Org C", org.getName());
        assertEquals("Instituição C", org.getInstituicaoEnsino());
        assertEquals("Chile", org.getPaisSede());
    }

    @Test
    void testJsonSerializationAndDeserialization() throws Exception {
        Organization org = Organization.builder()
                .id(4L)
                .name("Org JSON")
                .instituicaoEnsino("Instituição JSON")
                .paisSede("Uruguai")
                .build();
        String json = mapper.writeValueAsString(org);
        assertEquals(true, json.contains("\"id\":4"), "O JSON deve conter \"id\":4");
        assertEquals(true, json.contains("\"name\":\"Org JSON\""), "O JSON deve conter o name");
        assertEquals(true, json.contains("\"instituicaoEnsino\":\"Instituição JSON\""),
                "O JSON deve conter instituicaoEnsino");
        assertEquals(true, json.contains("\"paisSede\":\"Uruguai\""), "O JSON deve conter paisSede");
        Organization deserialized = mapper.readValue(json, Organization.class);
        assertEquals(org, deserialized, "O objeto desserializado deve ser igual ao original");
    }

    @Test
    void testEqualsAndHashCode() {
        Organization org1 = Organization.builder()
                .id(5L)
                .name("Org Equals")
                .instituicaoEnsino("Instituição Equals")
                .paisSede("Peru")
                .build();
        Organization org2 = Organization.builder()
                .id(5L)
                .name("Org Equals")
                .instituicaoEnsino("Instituição Equals")
                .paisSede("Peru")
                .build();
        assertEquals(org1, org2, "Objetos com os mesmos valores devem ser iguais");
        assertEquals(org1.hashCode(), org2.hashCode(), "Hashcodes de objetos iguais devem ser iguais");
        org2.setName("Org Diferente");
        assertNotEquals(org1, org2, "Objetos devem divergir se algum campo for diferente");
    }

    @Test
    void testEqualsWithNullFields() {
        Organization org1 = new Organization();
        Organization org2 = new Organization();
        assertEquals(org1, org2, "Dois objetos com campos nulos devem ser iguais");
        assertEquals(org1.hashCode(), org2.hashCode(), "Hashcodes devem ser iguais quando os campos são nulos");
    }

    @Test
    void testNotEqualsDifferentType() {
        Organization org = Organization.builder()
                .id(6L)
                .name("Org Diff")
                .build();
        assertNotEquals("Alguma String", org, "Organization não deve ser igual a um objeto de outro tipo");
    }

    @Test
    void testToString() {
        Organization org = Organization.builder()
                .id(7L)
                .name("Org ToString")
                .instituicaoEnsino("Instituição TS")
                .paisSede("Equador")
                .build();
        String str = org.toString();
        assertNotNull(str, "toString() não deve retornar null");
        assertEquals(true, str.contains("7"), "toString deve conter o id");
        assertEquals(true, str.contains("Org ToString"), "toString deve conter o name");
        assertEquals(true, str.contains("Instituição TS"), "toString deve conter instituicaoEnsino");
        assertEquals(true, str.contains("Equador"), "toString deve conter paisSede");
    }

    @Test
    void testDefaultNoArgsValues() {
        Organization org = new Organization();
        assertNull(org.getId(), "Por padrão, id deve ser null");
        assertNull(org.getName(), "Por padrão, name deve ser null");
        assertNull(org.getInstituicaoEnsino(), "Por padrão, instituicaoEnsino deve ser null");
        assertNull(org.getPaisSede(), "Por padrão, paisSede deve ser null");
        String str = org.toString();
        assertNotNull(str, "toString() não deve retornar null mesmo com campos nulos");
    }

    @Test
    void testHashCodeStability() {
        Organization org = Organization.builder()
                .id(8L)
                .name("Org Stable")
                .instituicaoEnsino("Instituição Stable")
                .paisSede("Bolívia")
                .build();
        int hash1 = org.hashCode();
        int hash2 = org.hashCode();
        assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
    }

    @Test
    void testCanEqual() {
        Organization org = Organization.builder()
                .id(9L)
                .name("Org CanEqual")
                .build();
        assertEquals(true, org.canEqual(org), "Um objeto deve poder utilizar canEqual consigo mesmo");
        assertEquals(false, org.canEqual("Outro Tipo"), "canEqual deve retornar false para objeto de tipo diferente");
        FakeOrganization fake = new FakeOrganization();
        assertEquals(false, org.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
    }

    @Test
    void testEqualsSameInstance() {
        Organization org = Organization.builder()
                .id(10L)
                .name("Org Mesmo")
                .build();
        assertEquals(true, org.equals(org), "Um objeto deve ser igual a si mesmo");
    }

    @Test
    void testEqualsNull() {
        Organization org = Organization.builder()
                .id(10L)
                .name("Org Null")
                .build();
        assertEquals(false, org.equals(null), "Um objeto não deve ser igual a null");
    }

    @Test
    void testCanEqualDifferentInstances() {
        Organization org1 = Organization.builder()
                .id(11L)
                .name("Org J")
                .build();
        Organization org2 = Organization.builder()
                .id(11L)
                .name("Org J")
                .build();
        assertEquals(true, org1.canEqual(org2), "Objetos do mesmo tipo devem retornar true em canEqual()");
    }

    static class FakeOrganization extends Organization {
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }
}