package br.edu.fatecsjc.lgnspringapi.converter;

import br.edu.fatecsjc.lgnspringapi.dto.MarathonDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Marathon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MarathonConverterTests {
    private MarathonConverter marathonConverter;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        marathonConverter = new MarathonConverter(modelMapper);
    }

    @Test
    void testConvertToEntity() {
        MarathonDTO dto = MarathonDTO.builder()
                .id(100L)
                .peso("75kg")
                .score("200")
                .build();

        Marathon entity = marathonConverter.convertToEntity(dto);

        assertNotNull(entity, "A entidade convertida não deve ser nula");
        assertNull(entity.getId(), "O id deve ser nulo, pois foi configurado para ser ignorado");
        assertEquals("75kg", entity.getPeso(), "O campo peso deve ser mapeado corretamente");
        assertEquals("200", entity.getScore(), "O campo score deve ser mapeado corretamente");
    }

    @Test
    void testConvertToEntityWithExistingEntity() {
        MarathonDTO dto = MarathonDTO.builder()
                .peso("80kg")
                .score("250")
                .build();

        Marathon existingEntity = Marathon.builder()
                .id(999L)
                .peso("70kg")
                .score("200")
                .build();

        Marathon result = marathonConverter.convertToEntity(dto, existingEntity);

        assertSame(existingEntity, result, "A entidade retornada deve ser a mesma instância fornecida");
        assertEquals(999L, result.getId(), "O id existente não deve ser alterado");
        assertEquals("80kg", result.getPeso(), "O campo peso deve ser atualizado");
        assertEquals("250", result.getScore(), "O campo score deve ser atualizado");
    }

    @Test
    void testConvertToDto() {
        Marathon entity = Marathon.builder()
                .id(101L)
                .peso("85kg")
                .score("300")
                .build();

        MarathonDTO dto = marathonConverter.convertToDto(entity);

        assertNotNull(dto, "O DTO convertido não deve ser nulo");
        assertEquals(101L, dto.getId(), "O id deve ter sido mapeado corretamente");
        assertEquals("85kg", dto.getPeso(), "O campo peso deve ter sido mapeado corretamente");
        assertEquals("300", dto.getScore(), "O campo score deve ter sido mapeado corretamente");
    }

    @Test
    void testConvertToEntityListEmpty() {
        List<MarathonDTO> dtos = Collections.emptyList();
        List<Marathon> entities = marathonConverter.convertToEntity(dtos);

        assertNotNull(entities, "A lista convertida não deve ser nula");
        assertTrue(entities.isEmpty(), "A lista convertida deve estar vazia");
    }

    @Test
    void testConvertToEntityListNonEmpty() {
        MarathonDTO dto1 = MarathonDTO.builder()
                .peso("90kg")
                .score("350")
                .build();
        MarathonDTO dto2 = MarathonDTO.builder()
                .peso("95kg")
                .score("400")
                .build();

        List<MarathonDTO> dtos = Arrays.asList(dto1, dto2);
        List<Marathon> entities = marathonConverter.convertToEntity(dtos);

        assertNotNull(entities, "A lista de entidades não deve ser nula");
        assertEquals(2, entities.size(), "A lista deve conter dois elementos");
    }

    @Test
    void testConvertToDtoList() {
        Marathon entity1 = Marathon.builder()
                .id(1L)
                .peso("100kg")
                .score("450")
                .build();
        Marathon entity2 = Marathon.builder()
                .id(2L)
                .peso("110kg")
                .score("500")
                .build();

        List<Marathon> entities = Arrays.asList(entity1, entity2);
        List<MarathonDTO> dtos = marathonConverter.convertToDto(entities);

        assertNotNull(dtos, "A lista de DTOs não deve ser nula");
        assertEquals(2, dtos.size(), "A lista de DTOs deve conter dois elementos");

        assertEquals(1L, dtos.get(0).getId(), "O primeiro DTO deve ter mapeado corretamente o id da entidade");
        assertEquals("100kg", dtos.get(0).getPeso(), "O primeiro DTO deve ter mapeado corretamente o peso");

        assertEquals(2L, dtos.get(1).getId(), "O segundo DTO deve ter mapeado corretamente o id da entidade");
        assertEquals("110kg", dtos.get(1).getPeso(), "O segundo DTO deve ter mapeado corretamente o peso");
    }
}