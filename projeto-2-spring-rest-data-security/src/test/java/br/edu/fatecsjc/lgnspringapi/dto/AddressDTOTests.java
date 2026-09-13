package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AddressDTOTests {
    @Test
    void testNoArgsConstructorAndSetters() {
        AddressDTO address = new AddressDTO();
        address.setId(1L);
        address.setLogradouro("Rua A");
        address.setNumero("123");
        address.setBairro("Bairro X");
        address.setCep("11111-111");
        address.setMunicipio("Cidade Y");
        address.setEstado("Estado Z");
        assertEquals(1L, address.getId());
        assertEquals("Rua A", address.getLogradouro());
        assertEquals("123", address.getNumero());
        assertEquals("Bairro X", address.getBairro());
        assertEquals("11111-111", address.getCep());
        assertEquals("Cidade Y", address.getMunicipio());
        assertEquals("Estado Z", address.getEstado());
    }

    @Test
    void testAllArgsConstructor() {
        AddressDTO address = new AddressDTO(2L, "Rua B", "456", "Bairro Y", "22222-222", "Cidade Z", "Estado W");
        assertEquals(2L, address.getId());
        assertEquals("Rua B", address.getLogradouro());
        assertEquals("456", address.getNumero());
        assertEquals("Bairro Y", address.getBairro());
        assertEquals("22222-222", address.getCep());
        assertEquals("Cidade Z", address.getMunicipio());
        assertEquals("Estado W", address.getEstado());
    }

    @Test
    void testBuilder() {
        AddressDTO address = AddressDTO.builder()
                .id(3L)
                .logradouro("Rua C")
                .numero("789")
                .bairro("Bairro Z")
                .cep("33333-333")
                .municipio("Cidade X")
                .estado("Estado Y")
                .build();
        assertEquals(3L, address.getId());
        assertEquals("Rua C", address.getLogradouro());
        assertEquals("789", address.getNumero());
        assertEquals("Bairro Z", address.getBairro());
        assertEquals("33333-333", address.getCep());
        assertEquals("Cidade X", address.getMunicipio());
        assertEquals("Estado Y", address.getEstado());
    }

    @Test
    void testJsonIncludeExcludesNullProperties() throws Exception {
        AddressDTO address = AddressDTO.builder()
                .logradouro("Rua D")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(address);
        assertEquals(true, json.contains("Rua D"), "JSON deve conter o logradouro configurado");
        assertEquals(false, json.contains("id"));
        assertEquals(false, json.contains("numero"));
        assertEquals(false, json.contains("bairro"));
        assertEquals(false, json.contains("cep"));
        assertEquals(false, json.contains("municipio"));
        assertEquals(false, json.contains("estado"));
    }

    @Test
    void testJsonDeserialization() throws Exception {
        String json = "{\"id\":10, \"logradouro\":\"Rua Deserializada\", \"numero\":\"101\", \"bairro\":\"Bairro D\", \"cep\":\"10101-101\", \"municipio\":\"Cidade D\", \"estado\":\"Estado D\"}";
        ObjectMapper mapper = new ObjectMapper();
        AddressDTO address = mapper.readValue(json, AddressDTO.class);
        assertEquals(10L, address.getId());
        assertEquals("Rua Deserializada", address.getLogradouro());
        assertEquals("101", address.getNumero());
        assertEquals("Bairro D", address.getBairro());
        assertEquals("10101-101", address.getCep());
        assertEquals("Cidade D", address.getMunicipio());
        assertEquals("Estado D", address.getEstado());
    }

    @Test
    void testEqualsAndHashCode() {
        AddressDTO address1 = AddressDTO.builder()
                .id(10L)
                .logradouro("Rua Teste")
                .numero("10")
                .bairro("Bairro Teste")
                .cep("00000-000")
                .municipio("Cidade Teste")
                .estado("Estado Teste")
                .build();
        AddressDTO address2 = AddressDTO.builder()
                .id(10L)
                .logradouro("Rua Teste")
                .numero("10")
                .bairro("Bairro Teste")
                .cep("00000-000")
                .municipio("Cidade Teste")
                .estado("Estado Teste")
                .build();
        assertEquals(address1, address2, "Objetos com dados idênticos devem ser iguais");
        assertEquals(address1.hashCode(), address2.hashCode(), "Hashcodes de objetos iguais devem ser iguais");
        address2.setNumero("20");
        assertNotEquals(address1, address2, "Objetos não devem ser iguais se alguma propriedade divergir");
    }

    @Test
    void testEqualsWithNullFields() {
        AddressDTO address1 = new AddressDTO();
        AddressDTO address2 = new AddressDTO();
        assertEquals(address1, address2, "Dois objetos com campos nulos devem ser iguais");
        assertEquals(address1.hashCode(), address2.hashCode(),
                "Hashcodes devem ser iguais quando todos os campos são nulos");
    }

    @Test
    void testEqualsWhenOneFieldIsNull() {
        AddressDTO address1 = AddressDTO.builder()
                .id(1L)
                .logradouro("Rua A")
                .build();
        AddressDTO address2 = AddressDTO.builder()
                .id(1L)
                .logradouro(null)
                .build();
        assertNotEquals(address1, address2, "Objetos não devem ser iguais se um campo diferir (null vs não-null)");
    }

    @Test
    void testEqualsSymmetryAndTransitivity() {
        AddressDTO a = AddressDTO.builder().id(1L).logradouro("Rua A").numero("100").build();
        AddressDTO b = AddressDTO.builder().id(1L).logradouro("Rua A").numero("100").build();
        AddressDTO c = AddressDTO.builder().id(1L).logradouro("Rua A").numero("100").build();
        assertEquals(true, a.equals(b) && b.equals(a), "equals deve ser simétrico");
        assertEquals(true, a.equals(b) && b.equals(c) && a.equals(c), "equals deve ser transitivo");
    }

    @Test
    void testNotEqualsDifferentType() {
        AddressDTO address = AddressDTO.builder()
                .id(5L)
                .logradouro("Rua Diferente")
                .build();
        assertNotEquals("Alguma String", address, "AddressDTO não deve ser igual a um objeto de outro tipo");
    }

    @Test
    void testToString() {
        AddressDTO address = AddressDTO.builder()
                .id(15L)
                .logradouro("Rua ABC")
                .numero("99")
                .bairro("Centro")
                .cep("12345-678")
                .municipio("Cidade XYZ")
                .estado("Estado LM")
                .build();
        String result = address.toString();
        assertNotNull(result, "toString() não deve retornar null");
        assertEquals(true, result.contains("15"), "toString deve conter o id");
        assertEquals(true, result.contains("Rua ABC"), "toString deve conter o logradouro");
        assertEquals(true, result.contains("99"), "toString deve conter o número");
        assertEquals(true, result.contains("Centro"), "toString deve conter o bairro");
        assertEquals(true, result.contains("12345-678"), "toString deve conter o cep");
        assertEquals(true, result.contains("Cidade XYZ"), "toString deve conter o município");
        assertEquals(true, result.contains("Estado LM"), "toString deve conter o estado");
    }

    @Test
    void testDefaultNoArgsValues() {
        AddressDTO address = new AddressDTO();
        assertNull(address.getId(), "Id deve ser null por padrão");
        assertNull(address.getLogradouro(), "Logradouro deve ser null por padrão");
        assertNull(address.getNumero(), "Numero deve ser null por padrão");
        assertNull(address.getBairro(), "Bairro deve ser null por padrão");
        assertNull(address.getCep(), "Cep deve ser null por padrão");
        assertNull(address.getMunicipio(), "Municipio deve ser null por padrão");
        assertNull(address.getEstado(), "Estado deve ser null por padrão");
        String str = address.toString();
        assertNotNull(str, "toString não deve retornar null mesmo com valores nulos");
    }

    @Test
    void testHashCodeStability() {
        AddressDTO address = AddressDTO.builder()
                .id(20L)
                .logradouro("Rua Hash")
                .build();
        int hash1 = address.hashCode();
        int hash2 = address.hashCode();
        assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
    }

    @Test
    void testCanEqual() {
        AddressDTO address = AddressDTO.builder().id(100L).build();
        assertEquals(true, address.canEqual(address), "Um objeto deve poder comparar consigo mesmo");
        assertEquals(false, address.canEqual("Não é AddressDTO"), "canEqual deve retornar false para objeto de tipo diferente");
        FakeAddressDTO fake = new FakeAddressDTO();
        assertEquals(false, address.equals(fake), "equals deve retornar false se canEqual retornar false");
    }

    @Test
    void testEqualsSameInstance() {
        AddressDTO address = AddressDTO.builder().id(50L).build();
        assertEquals(address, address, "Um objeto deve ser igual a si mesmo");
    }

    @Test
    void testEqualsNull() {
        AddressDTO address = AddressDTO.builder().id(50L).build();
        assertNotEquals(null, address, "Um objeto não deve ser igual a null");
    }

    @Test
    void testCanEqualDifferentInstances() {
        AddressDTO address1 = AddressDTO.builder().id(30L).build();
        AddressDTO address2 = AddressDTO.builder().id(30L).build();
        assertEquals(true, address1.canEqual(address2), "Objetos do mesmo tipo devem retornar true em canEqual");
    }

    static class FakeAddressDTO extends AddressDTO {
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }
}