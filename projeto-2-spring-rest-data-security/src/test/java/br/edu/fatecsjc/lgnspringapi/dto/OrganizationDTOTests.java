package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrganizationDTOTests {
        private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        @Test
        void testNoArgsConstructorAndSetters() {
                OrganizationDTO org = new OrganizationDTO();
                org.setId(1L);
                org.setNome("Org A");
                org.setInstituicaoEnsino("Institution A");
                org.setPaisSede("Brazil");

                AddressDTO address = AddressDTO.builder()
                                .id(10L)
                                .logradouro("Rua Teste")
                                .numero("123")
                                .bairro("Bairro X")
                                .cep("11111-111")
                                .municipio("Cidade Y")
                                .estado("Estado Z")
                                .build();
                org.setEndereco(address);

                GroupDTO group = GroupDTO.builder()
                                .id(20L)
                                .name("Group 1")
                                .build();
                org.setGrupos(Collections.singletonList(group));
                org.setGruposId(Collections.singletonList(20L));

                assertEquals(1L, org.getId());
                assertEquals("Org A", org.getNome());
                assertEquals("Institution A", org.getInstituicaoEnsino());
                assertEquals("Brazil", org.getPaisSede());
                assertNotNull(org.getEndereco());
                assertEquals("Rua Teste", org.getEndereco().getLogradouro());
                assertNotNull(org.getGrupos());
                assertEquals(1, org.getGrupos().size());
                assertEquals("Group 1", org.getGrupos().get(0).getName());
                assertNotNull(org.getGruposId());
                assertEquals(1, org.getGruposId().size());
                assertEquals(20L, org.getGruposId().get(0));
        }

        @Test
        void testAllArgsConstructor() {
                AddressDTO address = AddressDTO.builder()
                                .id(11L)
                                .logradouro("Rua All")
                                .numero("456")
                                .bairro("Bairro All")
                                .cep("22222-222")
                                .municipio("Cidade All")
                                .estado("Estado All")
                                .build();
                GroupDTO group = GroupDTO.builder()
                                .id(21L)
                                .name("Group All")
                                .build();
                List<GroupDTO> groups = Collections.singletonList(group);
                List<Long> groupsId = Collections.singletonList(21L);
                OrganizationDTO org = new OrganizationDTO(2L, "Org All", "Institution All", "USA", address, groups,
                                groupsId);

                assertEquals(2L, org.getId());
                assertEquals("Org All", org.getNome());
                assertEquals("Institution All", org.getInstituicaoEnsino());
                assertEquals("USA", org.getPaisSede());
                assertNotNull(org.getEndereco());
                assertEquals("Rua All", org.getEndereco().getLogradouro());
                assertNotNull(org.getGrupos());
                assertEquals(1, org.getGrupos().size());
                assertEquals("Group All", org.getGrupos().get(0).getName());
                assertNotNull(org.getGruposId());
                assertEquals(1, org.getGruposId().size());
                assertEquals(21L, org.getGruposId().get(0));
        }

        @Test
        void testBuilder() {
                AddressDTO address = AddressDTO.builder()
                                .id(12L)
                                .logradouro("Rua Builder")
                                .numero("789")
                                .bairro("Bairro Builder")
                                .cep("33333-333")
                                .municipio("Cidade Builder")
                                .estado("Estado Builder")
                                .build();
                GroupDTO group = GroupDTO.builder()
                                .id(22L)
                                .name("Group Builder")
                                .build();
                OrganizationDTO org = OrganizationDTO.builder()
                                .id(3L)
                                .nome("Org Builder")
                                .instituicaoEnsino("Institution Builder")
                                .paisSede("UK")
                                .endereco(address)
                                .grupos(Collections.singletonList(group))
                                .gruposId(Collections.singletonList(22L))
                                .build();

                assertEquals(3L, org.getId());
                assertEquals("Org Builder", org.getNome());
                assertEquals("Institution Builder", org.getInstituicaoEnsino());
                assertEquals("UK", org.getPaisSede());
                assertNotNull(org.getEndereco());
                assertEquals("Rua Builder", org.getEndereco().getLogradouro());
                assertNotNull(org.getGrupos());
                assertEquals(1, org.getGrupos().size());
                assertEquals("Group Builder", org.getGrupos().get(0).getName());
                assertNotNull(org.getGruposId());
                assertEquals(1, org.getGruposId().size());
                assertEquals(22L, org.getGruposId().get(0));
        }

        @Test
        void testJsonSerializationAndDeserialization() throws Exception {
                AddressDTO address = AddressDTO.builder()
                                .id(13L)
                                .logradouro("Rua JSON")
                                .numero("101")
                                .bairro("Bairro JSON")
                                .cep("44444-444")
                                .municipio("Cidade JSON")
                                .estado("Estado JSON")
                                .build();
                GroupDTO group = GroupDTO.builder()
                                .id(23L)
                                .name("Group JSON")
                                .build();
                OrganizationDTO org = OrganizationDTO.builder()
                                .id(4L)
                                .nome("Org JSON")
                                .instituicaoEnsino("Institution JSON")
                                .paisSede("Canada")
                                .endereco(address)
                                .grupos(Collections.singletonList(group))
                                .gruposId(Collections.singletonList(23L))
                                .build();

                String json = mapper.writeValueAsString(org);
                assertEquals(true, json.contains("\"id\":4"), "JSON should contain \"id\":4");
                assertEquals(true, json.contains("\"nome\":\"Org JSON\""), "JSON should contain the nome");
                assertEquals(true, json.contains("\"instituicaoEnsino\":\"Institution JSON\""),
                                "JSON should contain instituicaoEnsino");
                assertEquals(true, json.contains("\"paisSede\":\"Canada\""), "JSON should contain paisSede");
                assertEquals(true, json.contains("\"endereco\":"), "JSON should contain endereco");
                assertEquals(true, json.contains("\"grupos\":"), "JSON should contain grupos");
                assertEquals(true, json.contains("\"gruposId\":"), "JSON should contain gruposId");

                OrganizationDTO deserialized = mapper.readValue(json, OrganizationDTO.class);
                assertEquals(org, deserialized, "Deserialized object should equal the original");
        }

        @Test
        void testJsonIncludeExcludesNullProperties() throws Exception {
                OrganizationDTO org = OrganizationDTO.builder()
                                .id(5L)
                                .nome("Org Null")
                                .instituicaoEnsino("Institution Null")
                                .paisSede("Germany")
                                .build();
                String json = mapper.writeValueAsString(org);
                assertEquals(true, json.contains("\"id\":5"), "JSON should contain id when set");
                assertEquals(true, json.contains("\"nome\":\"Org Null\""), "JSON should contain nome");
                assertEquals(true, json.contains("\"instituicaoEnsino\":\"Institution Null\""),
                                "JSON should contain instituicaoEnsino");
                assertEquals(true, json.contains("\"paisSede\":\"Germany\""), "JSON should contain paisSede");
                assertEquals(false, json.contains("endereco"), "JSON should not contain endereco if it is null");
                assertEquals(false, json.contains("grupos"), "JSON should not contain grupos if it is null");
                assertEquals(false, json.contains("gruposId"), "JSON should not contain gruposId if it is null");
        }

        @Test
        void testEqualsAndHashCode() {
                AddressDTO address = AddressDTO.builder()
                                .id(14L)
                                .logradouro("Rua Equals")
                                .numero("202")
                                .bairro("Bairro Equals")
                                .cep("55555-555")
                                .municipio("Cidade Equals")
                                .estado("Estado Equals")
                                .build();
                GroupDTO group = GroupDTO.builder()
                                .id(24L)
                                .name("Group Equals")
                                .build();
                OrganizationDTO org1 = OrganizationDTO.builder()
                                .id(6L)
                                .nome("Org Equals")
                                .instituicaoEnsino("Institution Equals")
                                .paisSede("France")
                                .endereco(address)
                                .grupos(Collections.singletonList(group))
                                .gruposId(Collections.singletonList(24L))
                                .build();
                OrganizationDTO org2 = OrganizationDTO.builder()
                                .id(6L)
                                .nome("Org Equals")
                                .instituicaoEnsino("Institution Equals")
                                .paisSede("France")
                                .endereco(address)
                                .grupos(Collections.singletonList(group))
                                .gruposId(Collections.singletonList(24L))
                                .build();

                assertEquals(org1, org2, "Organizations with identical values should be equal");
                assertEquals(org1.hashCode(), org2.hashCode(), "Equal organizations must have identical hashcodes");

                org2.setNome("Different Org");
                assertNotEquals(org1, org2, "Organizations should differ if a property diverges");
        }

        @Test
        void testEqualsWithNullFields() {
                OrganizationDTO org1 = new OrganizationDTO();
                OrganizationDTO org2 = new OrganizationDTO();
                assertEquals(org1, org2, "Two organizations with null fields should be equal");
                assertEquals(org1.hashCode(), org2.hashCode(), "Hashcodes should be equal when fields are null");
        }

        @Test
        void testNotEqualsDifferentType() {
                OrganizationDTO org = OrganizationDTO.builder()
                                .id(7L)
                                .nome("Org Diff")
                                .build();
                assertNotEquals("Some String", org, "OrganizationDTO should not equal an object of a different type");
        }

        @Test
        void testToString() {
                OrganizationDTO org = OrganizationDTO.builder()
                                .id(8L)
                                .nome("Org ToString")
                                .build();
                String str = org.toString();
                assertNotNull(str, "toString() should not return null");
                assertEquals(true, str.contains("8"), "toString should contain the id");
                assertEquals(true, str.contains("Org ToString"), "toString should contain the nome");
        }

        @Test
        void testDefaultNoArgsValues() {
                OrganizationDTO org = new OrganizationDTO();
                assertNull(org.getId(), "By default, id should be null");
                assertNull(org.getNome(), "By default, nome should be null");
                assertNull(org.getInstituicaoEnsino(), "By default, instituicaoEnsino should be null");
                assertNull(org.getPaisSede(), "By default, paisSede should be null");
                assertNull(org.getEndereco(), "By default, endereco should be null");
                assertNull(org.getGrupos(), "By default, grupos should be null");
                assertNull(org.getGruposId(), "By default, gruposId should be null");
                String str = org.toString();
                assertNotNull(str, "toString() should not return null even with null fields");
        }

        @Test
        void testHashCodeStability() {
                OrganizationDTO org = OrganizationDTO.builder()
                                .id(9L)
                                .nome("Stable Org")
                                .build();
                int hash1 = org.hashCode();
                int hash2 = org.hashCode();
                assertEquals(hash1, hash2, "hashCode should remain stable between calls");
        }

        @Test
        void testCanEqual() {
                OrganizationDTO org = OrganizationDTO.builder().id(10L).nome("Org I").build();
                assertEquals(true, org.canEqual(org), "An object should be able to compare with itself via canEqual()");
                assertEquals(false, org.canEqual("Not an OrganizationDTO"),
                                "canEqual should return false for a different type");

                FakeOrganizationDTO fake = new FakeOrganizationDTO();
                assertEquals(false, org.equals(fake), "equals should return false if fake.canEqual() returns false");
        }

        @Test
        void testEqualsSameInstance() {
                OrganizationDTO org = OrganizationDTO.builder().id(11L).nome("Org Same").build();
                assertEquals(org, org, "An object should be equal to itself");
        }

        @Test
        void testEqualsNull() {
                OrganizationDTO org = OrganizationDTO.builder().id(11L).nome("Org Null").build();
                assertNotEquals(null, org, "An object should not be equal to null");
        }

        @Test
        void testCanEqualDifferentInstances() {
                OrganizationDTO org1 = OrganizationDTO.builder().id(12L).nome("Org J").build();
                OrganizationDTO org2 = OrganizationDTO.builder().id(12L).nome("Org J").build();
                assertEquals(true, org1.canEqual(org2), "Objects of the same type should return true for canEqual()");
        }

        static class FakeOrganizationDTO extends OrganizationDTO {
                @Override
                public boolean canEqual(Object other) {
                        return false;
                }
        }
}