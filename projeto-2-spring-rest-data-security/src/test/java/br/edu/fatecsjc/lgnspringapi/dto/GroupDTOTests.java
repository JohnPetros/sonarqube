package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GroupDTOTests {
        private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        @Test
        void testNoArgsConstructorAndSetters() {
                GroupDTO group = new GroupDTO();
                group.setId(1L);
                group.setName("Group A");
                MemberDTO member = MemberDTO.builder().name("Member1").build();
                group.setMembers(Collections.singletonList(member));
                assertEquals(1L, group.getId());
                assertEquals("Group A", group.getName());
                assertNotNull(group.getMembers());
                assertEquals(1, group.getMembers().size());
                assertEquals("Member1", group.getMembers().get(0).getName());
        }

        @Test
        void testAllArgsConstructor() {
                MemberDTO member = MemberDTO.builder().name("Member2").build();
                List<MemberDTO> members = Collections.singletonList(member);
                GroupDTO group = new GroupDTO(2L, "Group B", members);
                assertEquals(2L, group.getId());
                assertEquals("Group B", group.getName());
                assertNotNull(group.getMembers());
                assertEquals(1, group.getMembers().size());
                assertEquals("Member2", group.getMembers().get(0).getName());
        }

        @Test
        void testBuilder() {
                MemberDTO member = MemberDTO.builder().name("Member3").build();
                GroupDTO group = GroupDTO.builder()
                                .id(3L)
                                .name("Group C")
                                .members(Arrays.asList(member))
                                .build();
                assertEquals(3L, group.getId());
                assertEquals("Group C", group.getName());
                assertNotNull(group.getMembers());
                assertEquals(1, group.getMembers().size());
                assertEquals("Member3", group.getMembers().get(0).getName());
        }

        @Test
        void testJsonSerializationAndDeserialization() throws Exception {
                MemberDTO member = MemberDTO.builder().name("MemberJson").build();
                GroupDTO group = GroupDTO.builder()
                                .id(4L)
                                .name("Group D")
                                .members(Collections.singletonList(member))
                                .build();
                String json = mapper.writeValueAsString(group);
                assertEquals(true, json.contains("\"id\":4"), "JSON should contain \"id\":4");
                assertEquals(true, json.contains("\"name\":\"Group D\""), "JSON should contain the group name");
                assertEquals(true, json.contains("\"members\":"), "JSON should contain the 'members' property");
                GroupDTO deserialized = mapper.readValue(json, GroupDTO.class);
                assertEquals(group, deserialized, "Deserialized object should equal the original");
        }

        @Test
        void testJsonIncludeExcludesNullProperties() throws Exception {
                GroupDTO group = GroupDTO.builder()
                                .id(5L)
                                .name("Group E")
                                .build();
                String json = mapper.writeValueAsString(group);
                assertEquals(true, json.contains("\"id\":5"), "JSON should contain id when set");
                assertEquals(true, json.contains("\"name\":\"Group E\""), "JSON should contain the name");
                assertEquals(false, json.contains("members"), "JSON should not contain 'members' if it is null");
        }

        @Test
        void testEqualsAndHashCode() {
                MemberDTO member = MemberDTO.builder().name("MemberEqual").build();
                List<MemberDTO> members = Collections.singletonList(member);

                GroupDTO group1 = GroupDTO.builder()
                                .id(6L)
                                .name("Group F")
                                .members(members)
                                .build();
                GroupDTO group2 = GroupDTO.builder()
                                .id(6L)
                                .name("Group F")
                                .members(members)
                                .build();
                assertEquals(group1, group2, "Objects with identical values should be equal");
                assertEquals(group1.hashCode(), group2.hashCode(), "Equal objects must have equal hashcodes");
                group2.setName("Different Group");
                assertNotEquals(group1, group2, "Objects should differ if a property diverges");
        }

        @Test
        void testEqualsWithNullFields() {
                GroupDTO group1 = new GroupDTO();
                GroupDTO group2 = new GroupDTO();
                assertEquals(group1, group2, "Two objects with null fields should be equal");
                assertEquals(group1.hashCode(), group2.hashCode(), "Hashcodes should be equal when fields are null");
        }

        @Test
        void testNotEqualsDifferentType() {
                GroupDTO group = GroupDTO.builder()
                                .id(7L)
                                .name("Group G")
                                .build();
                assertNotEquals("Some String", group, "GroupDTO should not equal an object of a different type");
        }

        @Test
        void testToString() {
                GroupDTO group = GroupDTO.builder()
                                .id(8L)
                                .name("Group H")
                                .build();
                String str = group.toString();
                assertNotNull(str, "toString() should not return null");
                assertEquals(true, str.contains("8"), "toString should contain the id");
                assertEquals(true, str.contains("Group H"), "toString should contain the name");
        }

        @Test
        void testDefaultNoArgsValues() {
                GroupDTO group = new GroupDTO();
                assertNull(group.getId(), "By default, id should be null");
                assertNull(group.getName(), "By default, name should be null");
                assertNull(group.getMembers(), "By default, members should be null");
                String str = group.toString();
                assertNotNull(str, "toString() should not return null even with null values");
        }

        @Test
        void testHashCodeStability() {
                GroupDTO group = GroupDTO.builder()
                                .id(9L)
                                .name("Stable Group")
                                .build();
                int hash1 = group.hashCode();
                int hash2 = group.hashCode();
                assertEquals(hash1, hash2, "hashCode should remain stable between calls");
        }

        @Test
        void testCanEqual() {
                GroupDTO group = GroupDTO.builder().id(10L).name("Group I").build();
                assertEquals(true, group.canEqual(group), "An object should be able to compare with itself via canEqual()");
                assertEquals(false, group.canEqual("Not a GroupDTO"),
                                "canEqual should return false for an object of a different type");
                FakeGroupDTO fake = new FakeGroupDTO();
                assertEquals(false, group.equals(fake), "equals should return false if fake.canEqual() returns false");
        }

        @Test
        void testEqualsSameInstance() {
                GroupDTO group = GroupDTO.builder().id(11L).name("Group Same").build();
                assertEquals(group, group, "An object should be equal to itself");
        }

        @Test
        void testEqualsNull() {
                GroupDTO group = GroupDTO.builder().id(11L).name("Group Null").build();
                assertNotEquals(null, group, "An object should not be equal to null");
        }

        @Test
        void testCanEqualDifferentInstances() {
                GroupDTO group1 = GroupDTO.builder().id(12L).name("Group J").build();
                GroupDTO group2 = GroupDTO.builder().id(12L).name("Group J").build();
                assertEquals(true, group1.canEqual(group2), "Objects of the same type should return true for canEqual()");
        }

        static class FakeGroupDTO extends GroupDTO {
                @Override
                public boolean canEqual(Object other) {
                        return false;
                }
        }
}