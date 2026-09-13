package br.edu.fatecsjc.lgnspringapi.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AddressTests {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void testNoArgsConstructorAndSetters() {
        Address address = new Address();
        address.setId(1L);
        address.setLogradouro("Rua A");
        address.setNumero("123");
        address.setBairro("Bairro X");
        address.setCep("11111-111");
        address.setMunicipio("Cidade Y");
        address.setEstado("Estado Z");
        Organization org = Organization.builder()
                .id(100L)
                .name("Org Teste")
                .instituicaoEnsino("Instituição Teste")
                .paisSede("Brasil")
                .build();
        address.setOrganization(org);
        assertEquals(1L, address.getId());
        assertEquals("Rua A", address.getLogradouro());
        assertEquals("123", address.getNumero());
        assertEquals("Bairro X", address.getBairro());
        assertEquals("11111-111", address.getCep());
        assertEquals("Cidade Y", address.getMunicipio());
        assertEquals("Estado Z", address.getEstado());
        assertNotNull(address.getOrganization());
        assertEquals(100L, address.getOrganization().getId());
        assertEquals("Org Teste", address.getOrganization().getName());
    }

    @Test
    void testAllArgsConstructorAndBuilder() {
        Organization org = Organization.builder()
                .id(101L)
                .name("Org AllArgs")
                .instituicaoEnsino("Instituição AllArgs")
                .paisSede("Argentina")
                .build();
        Address address1 = new Address(2L, "Rua B", "456", "Bairro Y", "22222-222", "Cidade Z", "Estado W", org);
        Address address2 = Address.builder()
                .id(2L)
                .logradouro("Rua B")
                .numero("456")
                .bairro("Bairro Y")
                .cep("22222-222")
                .municipio("Cidade Z")
                .estado("Estado W")
                .organization(org)
                .build();
        assertEquals(address1, address2, "Objetos criados com AllArgsConstructor e builder devem ser iguais");
        assertEquals(address1.hashCode(), address2.hashCode(), "Hashcodes devem ser iguais para objetos iguais");
    }

    @Test
    void testJsonSerialization() throws Exception {
        Organization org = Organization.builder()
                .id(102L)
                .name("Org JSON")
                .instituicaoEnsino("Instituição JSON")
                .paisSede("Chile")
                .build();
        Address address = Address.builder()
                .id(3L)
                .logradouro("Rua JSON")
                .numero("789")
                .bairro("Bairro JSON")
                .cep("33333-333")
                .municipio("Cidade JSON")
                .estado("Estado JSON")
                .organization(org)
                .build();
        String json = mapper.writeValueAsString(address);
        assertEquals(true, json.contains("\"id\":3"), "O JSON deve conter o id");
        assertEquals(true, json.contains("\"logradouro\":\"Rua JSON\""), "O JSON deve conter o logradouro");
        assertEquals(false, json.contains("organization"),
                "O campo organization não deve aparecer no JSON devido ao @JsonBackReference");
    }

    @Test
    void testEqualsAndHashCode() {
        Organization org = Organization.builder()
                .id(103L)
                .name("Org Equals")
                .instituicaoEnsino("Instituição Equals")
                .paisSede("Uruguai")
                .build();
        Address address1 = Address.builder()
                .id(4L)
                .logradouro("Rua Equals")
                .numero("101")
                .bairro("Bairro Equals")
                .cep("44444-444")
                .municipio("Cidade Equals")
                .estado("Estado Equals")
                .organization(org)
                .build();
        Address address2 = Address.builder()
                .id(4L)
                .logradouro("Rua Equals")
                .numero("101")
                .bairro("Bairro Equals")
                .cep("44444-444")
                .municipio("Cidade Equals")
                .estado("Estado Equals")
                .organization(org)
                .build();
        assertEquals(address1, address2, "Objetos com os mesmos valores devem ser iguais");
        assertEquals(address1.hashCode(), address2.hashCode(), "Hashcodes devem ser iguais para objetos iguais");
        address2.setEstado("Estado Diferente");
        assertNotEquals(address1, address2, "Objetos devem diferir se algum campo for alterado");
    }

    @Test
    void testEqualsWithNullFields() {
        Address address1 = new Address();
        Address address2 = new Address();
        assertEquals(address1, address2, "Dois objetos com campos nulos devem ser iguais");
        assertEquals(address1.hashCode(), address2.hashCode(),
                "Hashcodes devem ser iguais quando os campos são nulos");
    }

    @Test
    void testNotEqualsDifferentType() {
        Address address = Address.builder()
                .id(5L)
                .logradouro("Rua Diff")
                .build();
        assertNotEquals("Uma String", address, "Address não deve ser igual a objeto de outro tipo");
    }

    @Test
    void testToString() {
        Address address = Address.builder()
                .id(6L)
                .logradouro("Rua ToString")
                .numero("202")
                .bairro("Bairro ToString")
                .cep("55555-555")
                .municipio("Cidade ToString")
                .estado("Estado ToString")
                .build();
        String str = address.toString();
        assertNotNull(str, "toString() não deve retornar null");
        assertEquals(true, str.contains("6"), "toString deve conter o id");
        assertEquals(true, str.contains("Rua ToString"), "toString deve conter o logradouro");
    }

    @Test
    void testDefaultNoArgsValues() {
        Address address = new Address();
        assertNull(address.getId(), "Por padrão, id deve ser null");
        assertNull(address.getLogradouro(), "Por padrão, logradouro deve ser null");
        assertNull(address.getNumero(), "Por padrão, numero deve ser null");
        assertNull(address.getBairro(), "Por padrão, bairro deve ser null");
        assertNull(address.getCep(), "Por padrão, cep deve ser null");
        assertNull(address.getMunicipio(), "Por padrão, municipio deve ser null");
        assertNull(address.getEstado(), "Por padrão, estado deve ser null");
        assertNull(address.getOrganization(), "Por padrão, organization deve ser null");
        String str = address.toString();
        assertNotNull(str, "toString() não deve retornar null mesmo com campos nulos");
    }

    @Test
    void testHashCodeStability() {
        Address address = Address.builder()
                .id(7L)
                .logradouro("Rua Stable")
                .build();
        int hash1 = address.hashCode();
        int hash2 = address.hashCode();
        assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
    }

    @Test
    void testCanEqual() {
        Address address = Address.builder().id(8L).logradouro("Rua CanEqual").build();
        assertEquals(true, address.canEqual(address), "Um objeto deve poder comparar com ele mesmo via canEqual()");
        assertEquals(false, address.canEqual("Outro Tipo"),
                "canEqual deve retornar false para objeto de tipo diferente");
        FakeAddress fake = new FakeAddress();
        assertEquals(false, address.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
    }

    @Test
    void testEqualsSameInstance() {
        Address address = Address.builder().id(9L).logradouro("Rua Mesmo").build();
        assertEquals(true, address.equals(address), "Um objeto deve ser igual a ele mesmo");
    }

    @Test
    void testEqualsNull() {
        Address address = Address.builder().id(9L).logradouro("Rua Nulo").build();
        assertEquals(false, address.equals(null), "Um objeto não deve ser igual a null");
    }

    @Test
    void testCanEqualDifferentInstances() {
        Address address1 = Address.builder().id(10L).logradouro("Rua Diferente").build();
        Address address2 = Address.builder().id(10L).logradouro("Rua Diferente").build();
        assertEquals(true, address1.canEqual(address2), "Objetos do mesmo tipo devem retornar true em canEqual()");
    }

    static class FakeAddress extends Address {
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }
}