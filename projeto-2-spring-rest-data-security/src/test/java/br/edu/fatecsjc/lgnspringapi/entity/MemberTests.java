package br.edu.fatecsjc.lgnspringapi.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberTests {
        private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        @Test
        void testNoArgsConstructorAndSetters() {
                Member member = new Member();
                member.setId(1L);
                member.setName("João da Silva");
                member.setAge(30);
                GroupEntity group = new GroupEntity();
                group.setId(100L);
                group.setName("Grupo Teste");
                member.setGroup(group);
                assertEquals(1L, member.getId());
                assertEquals("João da Silva", member.getName());
                assertEquals(30, member.getAge());
                assertNotNull(member.getGroup());
                assertEquals(100L, member.getGroup().getId());
                assertEquals("Grupo Teste", member.getGroup().getName());
        }

        @Test
        void testAllArgsConstructorAndBuilder() {
                GroupEntity group = GroupEntity.builder()
                                .id(101L)
                                .name("Grupo AllArgs")
                                .build();
                Member member1 = new Member(2L, "Maria", 25, group);
                Member member2 = Member.builder()
                                .id(2L)
                                .name("Maria")
                                .age(25)
                                .group(group)
                                .build();
                assertEquals(member1, member2);
                assertEquals(member1.hashCode(), member2.hashCode());
        }

        @Test
        void testJsonSerializationAndDeserialization() throws Exception {
                GroupEntity group = GroupEntity.builder()
                                .id(102L)
                                .name("Grupo JSON")
                                .build();
                Member member = Member.builder()
                                .id(3L)
                                .name("Carlos")
                                .age(40)
                                .group(group)
                                .build();
                String json = mapper.writeValueAsString(member);
                assertEquals(true, json.contains("\"id\":3"), "O JSON deve conter \"id\":3");
                assertEquals(true, json.contains("\"name\":\"Carlos\""), "O JSON deve conter o nome");
                assertEquals(true, json.contains("\"age\":40"), "O JSON deve conter a idade");
                assertEquals(false, json.contains("group"),
                                "O JSON não deve conter o campo group devido ao @JsonBackReference");
                Member deserialized = mapper.readValue(json, Member.class);
                assertEquals(member.getId(), deserialized.getId());
                assertEquals(member.getName(), deserialized.getName());
                assertEquals(member.getAge(), deserialized.getAge());
                assertNull(deserialized.getGroup(), "Após desserialização, group deve ser null");
        }

        @Test
        void testEqualsAndHashCode() {
                GroupEntity group = GroupEntity.builder()
                                .id(103L)
                                .name("Grupo Equals")
                                .build();
                Member member1 = Member.builder()
                                .id(4L)
                                .name("Ana")
                                .age(35)
                                .group(group)
                                .build();
                Member member2 = Member.builder()
                                .id(4L)
                                .name("Ana")
                                .age(35)
                                .group(group)
                                .build();
                assertEquals(member1, member2, "Objetos com valores idênticos devem ser iguais");
                assertEquals(member1.hashCode(), member2.hashCode(), "Hashcodes devem ser iguais para objetos iguais");
                member2.setAge(36);
                assertNotEquals(member1, member2, "Objetos devem diferir se algum campo divergir");
        }

        @Test
        void testEqualsWithNullFields() {
                Member member1 = new Member();
                Member member2 = new Member();
                assertEquals(member1, member2, "Dois objetos com campos nulos devem ser iguais");
                assertEquals(member1.hashCode(), member2.hashCode(), "Hashcodes devem ser iguais com campos nulos");
        }

        @Test
        void testNotEqualsDifferentType() {
                Member member = Member.builder()
                                .id(5L)
                                .name("Bruno")
                                .age(28)
                                .build();
                assertNotEquals("Uma String", member, "Member não deve ser igual a um objeto de outro tipo");
        }

        @Test
        void testToString() {
                Member member = Member.builder()
                                .id(6L)
                                .name("Fernanda")
                                .age(32)
                                .build();
                String str = member.toString();
                assertNotNull(str, "toString() não deve retornar null");
                assertEquals(true, str.contains("6"), "toString deve conter o id");
                assertEquals(true, str.contains("Fernanda"), "toString deve conter o nome");
                assertEquals(true, str.contains("32"), "toString deve conter a idade");
        }

        @Test
        void testDefaultNoArgsValues() {
                Member member = new Member();
                assertNull(member.getId(), "Por padrão, id deve ser null");
                assertNull(member.getName(), "Por padrão, name deve ser null");
                assertNull(member.getAge(), "Por padrão, age deve ser null");
                assertNull(member.getGroup(), "Por padrão, group deve ser null");
                String str = member.toString();
                assertNotNull(str, "toString() não deve retornar null mesmo com campos nulos");
        }

        @Test
        void testHashCodeStability() {
                Member member = Member.builder()
                                .id(7L)
                                .name("Gabriel")
                                .age(29)
                                .build();
                int hash1 = member.hashCode();
                int hash2 = member.hashCode();
                assertEquals(hash1, hash2, "hashCode deve ser estável entre chamadas");
        }

        @Test
        void testCanEqual() {
                Member member = Member.builder()
                                .id(8L)
                                .name("Helena")
                                .age(27)
                                .build();
                assertEquals(true, member.canEqual(member),
                                "Um objeto deve poder comparar consigo mesmo via canEqual()");
                assertEquals(false, member.canEqual("Outro Tipo"),
                                "canEqual deve retornar false para objeto de tipo diferente");
                FakeMember fake = new FakeMember();
                assertEquals(false, member.equals(fake),
                                "equals deve retornar false se fake.canEqual() retornar false");
        }

        @Test
        void testEqualsSameInstance() {
                Member member = Member.builder()
                                .id(9L)
                                .name("Igor")
                                .age(33)
                                .build();
                assertEquals(true, member.equals(member), "Um objeto deve ser igual a si mesmo");
        }

        @Test
        void testEqualsNull() {
                Member member = Member.builder()
                                .id(10L)
                                .name("Juliana")
                                .age(31)
                                .build();
                assertEquals(false, member.equals(null), "Um objeto não deve ser igual a null");
        }

        @Test
        void testCanEqualDifferentInstances() {
                Member member1 = Member.builder()
                                .id(11L)
                                .name("Karen")
                                .age(26)
                                .build();
                Member member2 = Member.builder()
                                .id(11L)
                                .name("Karen")
                                .age(26)
                                .build();
                assertEquals(true, member1.canEqual(member2),
                                "Objetos do mesmo tipo devem retornar true em canEqual()");
        }

        static class FakeMember extends Member {
                @Override
                public boolean canEqual(Object other) {
                        return false;
                }
        }
}