package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberDTOTests {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void testNoArgsConstructorAndSetters() {
        MemberDTO member = new MemberDTO();
        member.setId(1L);
        member.setName("John Doe");
        member.setAge(30);
        assertEquals(1L, member.getId());
        assertEquals("John Doe", member.getName());
        assertEquals(30, member.getAge());
    }

    @Test
    void testAllArgsConstructor() {
        MemberDTO member = new MemberDTO(2L, "Jane Doe", 25, null);
        assertEquals(2L, member.getId());
        assertEquals("Jane Doe", member.getName());
        assertEquals(25, member.getAge());
    }

    @Test
    void testBuilder() {
        MemberDTO member = MemberDTO.builder()
                .id(3L)
                .name("Alice")
                .age(28)
                .build();
        assertEquals(3L, member.getId());
        assertEquals("Alice", member.getName());
        assertEquals(28, member.getAge());
    }

    @Test
    void testJsonSerializationAndDeserialization() throws Exception {
        MemberDTO member = MemberDTO.builder()
                .id(4L)
                .name("Bob")
                .age(35)
                .build();
        String json = mapper.writeValueAsString(member);
        assertEquals(true, json.contains("\"id\":4"), "JSON should contain \"id\":4");
        assertEquals(true, json.contains("\"name\":\"Bob\""), "JSON should contain \"name\":\"Bob\"");
        assertEquals(true, json.contains("\"age\":35"), "JSON should contain \"age\":35");
        MemberDTO deserialized = mapper.readValue(json, MemberDTO.class);
        assertEquals(member, deserialized, "Deserialized object should equal the original");
    }

    @Test
    void testJsonIncludeExcludesNullProperties() throws Exception {
        MemberDTO member = MemberDTO.builder()
                .name("Charlie")
                .build();
        String json = mapper.writeValueAsString(member);
        assertEquals(true, json.contains("\"name\":\"Charlie\""), "JSON should contain 'name' property");
        assertEquals(false, json.contains("id"), "JSON should not contain 'id' if it is null");
        assertEquals(false, json.contains("age"), "JSON should not contain 'age' if it is null");
    }

    @Test
    void testEqualsAndHashCode() {
        MemberDTO member1 = MemberDTO.builder()
                .id(5L)
                .name("David")
                .age(40)
                .build();
        MemberDTO member2 = MemberDTO.builder()
                .id(5L)
                .name("David")
                .age(40)
                .build();
        assertEquals(member1, member2, "Objects with identical values must be equal");
        assertEquals(member1.hashCode(), member2.hashCode(), "Equal objects must have identical hashcodes");
        member2.setAge(41);
        assertNotEquals(member1, member2, "Objects must not be equal if any field differs");
    }

    @Test
    void testEqualsWithNullFields() {
        MemberDTO member1 = new MemberDTO();
        MemberDTO member2 = new MemberDTO();
        assertEquals(member1, member2, "Two objects with null fields should be equal");
        assertEquals(member1.hashCode(), member2.hashCode(), "Hashcodes should be equal when fields are null");
    }

    @Test
    void testNotEqualsDifferentType() {
        MemberDTO member = MemberDTO.builder()
                .id(6L)
                .name("Eve")
                .age(32)
                .build();
        assertNotEquals("A String", member, "MemberDTO should not be equal to an object of another type");
    }

    @Test
    void testToString() {
        MemberDTO member = MemberDTO.builder()
                .id(7L)
                .name("Frank")
                .age(45)
                .build();
        String str = member.toString();
        assertNotNull(str, "toString() should not return null");
        assertEquals(true, str.contains("7"), "toString should contain the id");
        assertEquals(true, str.contains("Frank"), "toString should contain the name");
        assertEquals(true, str.contains("45"), "toString should contain the age");
    }

    @Test
    void testDefaultNoArgsValues() {
        MemberDTO member = new MemberDTO();
        assertNull(member.getId(), "By default, id should be null");
        assertNull(member.getName(), "By default, name should be null");
        assertNull(member.getAge(), "By default, age should be null");
        String str = member.toString();
        assertNotNull(str, "toString() should not return null even with null values");
    }

    @Test
    void testHashCodeStability() {
        MemberDTO member = MemberDTO.builder()
                .id(8L)
                .name("Grace")
                .age(50)
                .build();
        int hash1 = member.hashCode();
        int hash2 = member.hashCode();
        assertEquals(hash1, hash2, "hashCode should remain stable between calls");
    }

    @Test
    void testCanEqual() {
        MemberDTO member = MemberDTO.builder()
                .id(9L)
                .name("Helen")
                .age(55)
                .build();
        assertEquals(true, member.canEqual(member), "An object should be able to compare with itself via canEqual()");
        assertEquals(false, member.canEqual("Not a MemberDTO"), "canEqual should return false for a different type");
        FakeMemberDTO fake = new FakeMemberDTO();
        assertEquals(false, member.equals(fake), "equals should return false if fake.canEqual() returns false");
    }

    @Test
    void testEqualsSameInstance() {
        MemberDTO member = MemberDTO.builder()
                .id(10L)
                .name("Ivy")
                .age(60)
                .build();
        assertEquals(member, member, "An object must be equal to itself");
    }

    @Test
    void testEqualsNull() {
        MemberDTO member = MemberDTO.builder()
                .id(11L)
                .name("Jack")
                .age(65)
                .build();
        assertNotEquals(null, member, "An object should not be equal to null");
    }

    @Test
    void testCanEqualDifferentInstances() {
        MemberDTO member1 = MemberDTO.builder()
                .id(12L)
                .name("Kate")
                .age(30)
                .build();
        MemberDTO member2 = MemberDTO.builder()
                .id(12L)
                .name("Kate")
                .age(30)
                .build();
        assertEquals(true, member1.canEqual(member2), "Objects of the same type should return true for canEqual()");
    }

    static class FakeMemberDTO extends MemberDTO {
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }
}