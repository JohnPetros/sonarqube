package br.edu.fatecsjc.lgnspringapi.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.entity.GroupEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class GroupConverterTests {
    private GroupConverter groupConverter;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        groupConverter = new GroupConverter(modelMapper);
    }

    @Test
    void testConvertToEntity() {
        GroupDTO dto = new GroupDTO();
        dto.setId(123L);
        dto.setName("Test Group");
        GroupEntity entity = groupConverter.convertToEntity(dto);

        assertNotNull(entity, "A entidade convertida não deve ser nula");
        assertNull(entity.getId(), "O id deve ser nulo, pois foi configurado para ser ignorado");
        assertEquals("Test Group", entity.getName(), "O nome deve ter sido mapeado corretamente");
    }

    @Test
    void testConvertToEntityWithExistingEntity() {
        GroupDTO dto = new GroupDTO();
        dto.setName("Updated Group");

        GroupEntity existingEntity = new GroupEntity();
        existingEntity.setId(999L);
        existingEntity.setName("Old Group");

        GroupEntity result = groupConverter.convertToEntity(dto, existingEntity);

        assertSame(existingEntity, result, "A entidade retornada deve ser a mesma instância fornecida");
        assertEquals(999L, result.getId(), "O id existente não deve ser alterado");
        assertEquals("Updated Group", result.getName(), "O nome deve ter sido atualizado para o novo valor");
    }

    @Test
    void testConvertToDto() {
        GroupEntity entity = new GroupEntity();
        entity.setId(111L);
        entity.setName("Entity Group");

        GroupDTO dto = groupConverter.convertToDto(entity);

        assertNotNull(dto, "O DTO convertido não deve ser nulo");
        assertEquals("Entity Group", dto.getName(), "O nome deve ter sido mapeado corretamente");
        assertEquals(111L, dto.getId(), "O id deve ter sido mapeado corretamente");
    }

    @Test
    void testConvertToEntityListEmpty() {
        List<GroupDTO> dtos = Collections.emptyList();
        List<GroupEntity> entities = groupConverter.convertToEntity(dtos);

        assertNotNull(entities, "A lista convertida não deve ser nula");
        assertTrue(entities.isEmpty(), "A lista convertida deve estar vazia");
    }

    @Test
    void testConvertToEntityListNonEmpty() {
        GroupDTO dto1 = new GroupDTO();
        dto1.setName("Group 1");
        GroupDTO dto2 = new GroupDTO();
        dto2.setName("Group 2");

        List<GroupDTO> dtos = Arrays.asList(dto1, dto2);
        List<GroupEntity> entities = groupConverter.convertToEntity(dtos);

        assertNotNull(entities, "A lista de entidades não deve ser nula");
        assertEquals(2, entities.size(), "A lista deve conter dois elementos");
    }

    @Test
    void testConvertToDtoList() {
        GroupEntity entity1 = new GroupEntity();
        entity1.setId(1L);
        entity1.setName("Entity 1");

        GroupEntity entity2 = new GroupEntity();
        entity2.setId(2L);
        entity2.setName("Entity 2");

        List<GroupEntity> entities = Arrays.asList(entity1, entity2);
        List<GroupDTO> dtos = groupConverter.convertToDto(entities);

        assertNotNull(dtos, "A lista de DTOs não deve ser nula");
        assertEquals(2, dtos.size(), "A lista de DTOs deve conter dois elementos");

        assertEquals("Entity 1", dtos.get(0).getName(),
                "O primeiro DTO deve ter mapeado corretamente o nome da entidade");
        assertEquals(1L, dtos.get(0).getId(), "O primeiro DTO deve ter mapeado corretamente o id da entidade");

        assertEquals("Entity 2", dtos.get(1).getName(),
                "O segundo DTO deve ter mapeado corretamente o nome da entidade");
        assertEquals(2L, dtos.get(1).getId(), "O segundo DTO deve ter mapeado corretamente o id da entidade");
    }
}