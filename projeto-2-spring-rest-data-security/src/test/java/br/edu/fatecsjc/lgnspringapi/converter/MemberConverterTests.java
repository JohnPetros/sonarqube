package br.edu.fatecsjc.lgnspringapi.converter;

import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
class MemberConverterTests {
    private MemberConverter memberConverter;

    @BeforeEach
    void setUp() {
        memberConverter = new MemberConverter(new ModelMapper());
    }

    @Test
    @DisplayName("Should convert MemberDTO to Member entity")
    void testConvertToEntity() {
        MemberDTO dto = new MemberDTO(1L, "Alice", 25, null);
        Member entity = memberConverter.convertToEntity(dto);

        assertNotNull(entity);
        assertEquals("Alice", entity.getName());
        assertEquals(25, entity.getAge());
        assertNull(entity.getId(), "ID deve ser nulo ao converter DTO para Entity");
    }

    @Test
    @DisplayName("Should convert Member entity to MemberDTO")
    void testConvertToDto() {
        Member entity = Member.builder()
                .id(1L)
                .name("Bob")
                .age(30)
                .build();

        MemberDTO dto = memberConverter.convertToDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Bob", dto.getName());
        assertEquals(30, dto.getAge());
    }

    @Test
    @DisplayName("Should convert list of MemberDTOs to list of Member entities")
    void testConvertToEntityList() {
        MemberDTO dto1 = new MemberDTO(1L, "Alice", 25, null);
        MemberDTO dto2 = new MemberDTO(2L, "Charlie", 40, null);

        List<Member> entities = memberConverter.convertToEntity(List.of(dto1, dto2));

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals("Alice", entities.get(0).getName());
        assertEquals("Charlie", entities.get(1).getName());
    }

    @Test
    @DisplayName("Should convert list of Member entities to list of MemberDTOs")
    void testConvertToDtoList() {
        Member entity1 = Member.builder().id(1L).name("Alice").age(25).build();
        Member entity2 = Member.builder().id(2L).name("Charlie").age(40).build();

        List<MemberDTO> dtos = memberConverter.convertToDto(List.of(entity1, entity2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("Alice", dtos.get(0).getName());
        assertEquals("Charlie", dtos.get(1).getName());
    }

    @Test
    @DisplayName("Should update existing Member entity from MemberDTO without changing ID")
    void testUpdateExistingEntity() {
        Member existingEntity = Member.builder()
                .id(10L)
                .name("Old Name")
                .age(45)
                .build();

        MemberDTO updatedDTO = new MemberDTO(99L, "Updated Name", 50, null);
        Member updatedEntity = memberConverter.convertToEntity(updatedDTO, existingEntity);

        assertNotNull(updatedEntity);
        assertEquals(10L, updatedEntity.getId(), "ID original deve ser preservado");
        assertEquals("Updated Name", updatedEntity.getName(), "Nome deve ser atualizado");
        assertEquals(50, updatedEntity.getAge(), "Idade deve ser atualizada");
    }
}