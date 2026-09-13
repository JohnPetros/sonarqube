package br.edu.fatecsjc.lgnspringapi.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class ConverterTests {
    private SimpleConverter converter;

    @BeforeEach
    void setup() {
        converter = new SimpleConverter();
    }

    @Test
    void testConvertToEntity() {
        SimpleDTO dto = new SimpleDTO("test");
        SimpleEntity entity = converter.convertToEntity(dto);
        assertNotNull(entity, "A entidade não deve ser nula");
        assertEquals("test", entity.getValue(), "O valor da entidade deve vir do DTO");
    }

    @Test
    void testConvertToEntityWithExistingEntity() {
        SimpleDTO dto = new SimpleDTO("updated");
        SimpleEntity entity = new SimpleEntity("original");
        SimpleEntity updatedEntity = converter.convertToEntity(dto, entity);
        assertNotNull(updatedEntity, "A entidade atualizada não deve ser nula");
        assertEquals("updated", updatedEntity.getValue(), "O valor deve ser atualizado a partir do DTO");
    }

    @Test
    void testConvertToDto() {
        SimpleEntity entity = new SimpleEntity("entityValue");
        SimpleDTO dto = converter.convertToDto(entity);
        assertNotNull(dto, "O DTO não deve ser nulo");
        assertEquals("entityValue", dto.getValue(), "O valor do DTO deve vir da entidade");
    }

    @Test
    void testConvertToEntityList() {
        List<SimpleDTO> dtos = Arrays.asList(new SimpleDTO("1"), new SimpleDTO("2"), new SimpleDTO("3"));
        List<SimpleEntity> entities = converter.convertToEntity(dtos);
        assertNotNull(entities, "A lista de entidades não deve ser nula");
        assertEquals(3, entities.size(), "A lista de entidades deve conter 3 elementos");
        assertEquals("1", entities.get(0).getValue());
        assertEquals("2", entities.get(1).getValue());
        assertEquals("3", entities.get(2).getValue());
    }

    @Test
    void testConvertToDtoList() {
        List<SimpleEntity> entities = Arrays.asList(new SimpleEntity("A"), new SimpleEntity("B"),
                new SimpleEntity("C"));
        List<SimpleDTO> dtos = converter.convertToDto(entities);
        assertNotNull(dtos, "A lista de DTOs não deve ser nula");
        assertEquals(3, dtos.size(), "A lista de DTOs deve conter 3 elementos");
        assertEquals("A", dtos.get(0).getValue());
        assertEquals("B", dtos.get(1).getValue());
        assertEquals("C", dtos.get(2).getValue());
    }
}