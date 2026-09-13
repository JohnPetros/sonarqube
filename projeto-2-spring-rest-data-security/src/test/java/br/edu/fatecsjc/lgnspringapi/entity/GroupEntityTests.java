package br.edu.fatecsjc.lgnspringapi.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GroupEntityTests {
        private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        @Test
        void testNoArgsConstructorAndSetters() {
                GroupEntity group = new GroupEntity();
                group.setId(1L);
                group.setName("Grupo A");
                Organization org = Organization.builder()
                                .id(100L)
                                .name("Org Teste")
                                .instituicaoEnsino("Instituição Teste")
                                .paisSede("Brasil")
                                .build();
                group.setOrganization(org);
                assertEquals(1L, group.getId());
                assertEquals("Grupo A", group.getName());
                assertNotNull(group.getOrganization());
                assertEquals(100L, group.getOrganization().getId());
                assertEquals("Org Teste", group.getOrganization().getName());
        }

        @Test
        void testAllArgsConstructorAndBuilder() {
                Organization org = Organization.builder()
                                .id(101L)
                                .name("Org AllArgs")
                                .instituicaoEnsino("Instituição AllArgs")
                                .paisSede("Argentina")
                                .build();
                GroupEntity group1 = new GroupEntity(2L, "Grupo B", org);
                GroupEntity group2 = GroupEntity.builder()
                                .id(2L)
                                .name("Grupo B")
                                .organization(org)
                                .build();
                assertEquals(group1, group2, "Objetos criados pelo construtor all-args e builder devem ser iguais");
                assertEquals(group1.hashCode(), group2.hashCode(), "Hashcodes de objetos iguais devem ser iguais");
        }

        @Test
        void testJsonSerializationAndDeserialization() throws Exception {
                Organization org = Organization.builder()
                                .id(102L)
                                .name("Org JSON")
                                .instituicaoEnsino("Instituição JSON")
                                .paisSede("Uruguai")
                                .build();
                GroupEntity group = GroupEntity.builder()
                                .id(3L)
                                .name("Grupo JSON")
                                .organization(org)
                                .build();
                String json = mapper.writeValueAsString(group);
                assertEquals(true, json.contains("\"id\":3"), "O JSON deve conter \"id\":3");
                assertEquals(true, json.contains("\"name\":\"Grupo JSON\""), "O JSON deve conter o nome do grupo");
                assertEquals(false, json.contains("organization"), "O JSON não deve conter o campo organization");
                GroupEntity deserialized = mapper.readValue(json, GroupEntity.class);
                assertEquals(group.getId(), deserialized.getId());
                assertEquals(group.getName(), deserialized.getName());
                assertNull(deserialized.getOrganization(), "Após desserialização, organization deve ser null");
        }

        @Test
        void testEqualsAndHashCode() {
                Organization org = Organization.builder()
                                .id(103L)
                                .name("Org Equals")
                                .instituicaoEnsino("Instituição Equals")
                                .paisSede("Chile")
                                .build();
                GroupEntity group1 = GroupEntity.builder()
                                .id(4L)
                                .name("Grupo Igual")
                                .organization(org)
                                .build();
                GroupEntity group2 = GroupEntity.builder()
                                .id(4L)
                                .name("Grupo Igual")
                                .organization(org)
                                .build();
                assertEquals(group1, group2, "Grupos com mesmo valores devem ser iguais");
                assertEquals(group1.hashCode(), group2.hashCode(), "Hashcodes devem ser iguais para objetos iguais");
                group2.setName("Grupo Diferente");
                assertNotEquals(group1, group2, "Grupos devem diferir se algum campo for diferente");
        }

        @Test
        void testEqualsWithNullFields() {
                GroupEntity group1 = new GroupEntity();
                GroupEntity group2 = new GroupEntity();
                assertEquals(group1, group2, "Dois objetos com campos nulos devem ser iguais");
                assertEquals(group1.hashCode(), group2.hashCode(),
                                "Hashcodes devem ser iguais quando os campos são nulos");
        }

        @Test
        void testNotEqualsDifferentType() {
                GroupEntity group = GroupEntity.builder()
                                .id(5L)
                                .name("Grupo Diferente")
                                .build();
                assertNotEquals("Uma String", group, "GroupEntity não deve ser igual a um objeto de outro tipo");
        }

        @Test
        void testToString() {
                GroupEntity group = GroupEntity.builder()
                                .id(6L)
                                .name("Grupo ToString")
                                .build();
                String str = group.toString();
                assertNotNull(str, "toString() não deve retornar null");
                assertEquals(true, str.contains("6"), "toString deve conter o id");
                assertEquals(true, str.contains("Grupo ToString"), "toString deve conter o nome do grupo");
        }

        @Test
        void testDefaultNoArgsValues() {
                GroupEntity group = new GroupEntity();
                assertNull(group.getId(), "Por padrão, id deve ser null");
                assertNull(group.getName(), "Por padrão, name deve ser null");
                assertNull(group.getOrganization(), "Por padrão, organization deve ser null");
                String str = group.toString();
                assertNotNull(str, "toString() não deve retornar null mesmo com campos nulos");
        }

        @Test
        void testHashCodeStability() {
                GroupEntity group = GroupEntity.builder()
                                .id(7L)
                                .name("Grupo Stable")
                                .build();
                int hash1 = group.hashCode();
                int hash2 = group.hashCode();
                assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
        }

        @Test
        void testCanEqual() {
                GroupEntity group = GroupEntity.builder()
                                .id(8L)
                                .name("Grupo CanEqual")
                                .build();
                assertEquals(true, group.canEqual(group), "Um objeto deve poder comparar consigo mesmo via canEqual()");
                assertEquals(false, group.canEqual("Outro Tipo"),
                                "canEqual deve retornar false para objeto de tipo diferente");
                FakeGroup fake = new FakeGroup();
                assertEquals(false, group.equals(fake), "equals deve retornar false se fake.canEqual() retornar false");
        }

        @Test
        void testEqualsSameInstance() {
                GroupEntity group = GroupEntity.builder()
                                .id(9L)
                                .name("Grupo Mesmo")
                                .build();
                assertEquals(true, group.equals(group), "Um objeto deve ser igual a si mesmo");
        }

        @Test
        void testEqualsNull() {
                GroupEntity group = GroupEntity.builder()
                                .id(9L)
                                .name("Grupo Nulo")
                                .build();
                assertEquals(false, group.equals(null), "Um objeto não deve ser igual a null");
        }

        @Test
        void testCanEqualDifferentInstances() {
                GroupEntity group1 = GroupEntity.builder()
                                .id(10L)
                                .name("Grupo J")
                                .build();
                GroupEntity group2 = GroupEntity.builder()
                                .id(10L)
                                .name("Grupo J")
                                .build();
                assertEquals(true, group1.canEqual(group2),
                                "Objetos do mesmo tipo devem retornar true para canEqual()");
        }

        static class FakeGroup extends GroupEntity {
                @Override
                public boolean canEqual(Object other) {
                        return false;
                }
        }
}