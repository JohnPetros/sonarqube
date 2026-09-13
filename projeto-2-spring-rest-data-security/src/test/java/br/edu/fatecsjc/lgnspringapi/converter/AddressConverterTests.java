package br.edu.fatecsjc.lgnspringapi.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.fatecsjc.lgnspringapi.dto.AddressDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class AddressConverterTests {
    private AddressConverter addressConverter;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        addressConverter = new AddressConverter(modelMapper);
    }

    @Test
    void testConvertToEntity() {
        AddressDTO dto = new AddressDTO();
        dto.setId(100L);
        dto.setLogradouro("Rua A");
        dto.setNumero("123");
        dto.setBairro("Bairro X");
        dto.setCep("12345-678");
        dto.setMunicipio("Cidade Y");
        dto.setEstado("Estado Z");

        Address entity = addressConverter.convertToEntity(dto);

        assertNotNull(entity, "A entidade convertida não deve ser nula");
        assertNull(entity.getId(), "O id deve ser nulo, pois o mapper está configurado para ignorá-lo");
        assertEquals(dto.getLogradouro(), entity.getLogradouro());
        assertEquals(dto.getNumero(), entity.getNumero());
        assertEquals(dto.getBairro(), entity.getBairro());
        assertEquals(dto.getCep(), entity.getCep());
        assertEquals(dto.getMunicipio(), entity.getMunicipio());
        assertEquals(dto.getEstado(), entity.getEstado());
    }

    @Test
    void testConvertToEntityWithExistingEntity() {
        AddressDTO dto = new AddressDTO();
        dto.setLogradouro("Rua Nova");
        dto.setNumero("456");
        dto.setBairro("Bairro Novo");
        dto.setCep("87654-321");
        dto.setMunicipio("Cidade Nova");
        dto.setEstado("Estado Nova");

        Address existingEntity = new Address();
        existingEntity.setId(200L);
        existingEntity.setLogradouro("Rua Antiga");
        existingEntity.setNumero("111");
        existingEntity.setBairro("Bairro Antigo");
        existingEntity.setCep("00000-000");
        existingEntity.setMunicipio("Cidade Antiga");
        existingEntity.setEstado("Estado Antigo");

        Address result = addressConverter.convertToEntity(dto, existingEntity);

        assertSame(existingEntity, result, "A entidade retornada deve ser a mesma instância fornecida");
        assertEquals(200L, result.getId(), "O id existente não deve ser alterado");
        assertEquals("Rua Nova", result.getLogradouro());
        assertEquals("456", result.getNumero());
        assertEquals("Bairro Novo", result.getBairro());
        assertEquals("87654-321", result.getCep());
        assertEquals("Cidade Nova", result.getMunicipio());
        assertEquals("Estado Nova", result.getEstado());
    }

    @Test
    void testConvertToDto() {
        Address entity = new Address();
        entity.setId(300L);
        entity.setLogradouro("Rua Convertida");
        entity.setNumero("789");
        entity.setBairro("Bairro Convertido");
        entity.setCep("22222-222");
        entity.setMunicipio("Municipio Convertido");
        entity.setEstado("Estado Convertido");

        AddressDTO dto = addressConverter.convertToDto(entity);

        assertNotNull(dto, "O DTO convertido não deve ser nulo");
        assertEquals(300L, dto.getId());
        assertEquals(entity.getLogradouro(), dto.getLogradouro());
        assertEquals(entity.getNumero(), dto.getNumero());
        assertEquals(entity.getBairro(), dto.getBairro());
        assertEquals(entity.getCep(), dto.getCep());
        assertEquals(entity.getMunicipio(), dto.getMunicipio());
        assertEquals(entity.getEstado(), dto.getEstado());
    }

    @Test
    void testConvertToEntityListEmpty() {
        List<AddressDTO> dtos = Collections.emptyList();
        List<Address> entities = addressConverter.convertToEntity(dtos);

        assertNotNull(entities, "A lista convertida não deve ser nula");
        assertTrue(entities.isEmpty(), "A lista convertida deve estar vazia");
    }

    @Test
    void testConvertToEntityListNonEmpty() {
        AddressDTO dto1 = new AddressDTO();
        dto1.setLogradouro("Rua 1");
        dto1.setNumero("1");
        dto1.setBairro("Bairro 1");
        dto1.setCep("11111-111");
        dto1.setMunicipio("Municipio 1");
        dto1.setEstado("Estado 1");

        AddressDTO dto2 = new AddressDTO();
        dto2.setLogradouro("Rua 2");
        dto2.setNumero("2");
        dto2.setBairro("Bairro 2");
        dto2.setCep("22222-222");
        dto2.setMunicipio("Municipio 2");
        dto2.setEstado("Estado 2");

        List<AddressDTO> dtos = Arrays.asList(dto1, dto2);
        List<Address> entities = addressConverter.convertToEntity(dtos);

        assertNotNull(entities, "A lista de entidades não deve ser nula");
        assertEquals(2, entities.size(), "A lista deve conter dois elementos");

        Address entity1 = entities.get(0);
        assertNull(entity1.getId(), "O id deve ser nulo por questão do mapeamento");
        assertEquals("Rua 1", entity1.getLogradouro());
        assertEquals("1", entity1.getNumero());

        Address entity2 = entities.get(1);
        assertNull(entity2.getId(), "O id deve ser nulo por questão do mapeamento");
        assertEquals("Rua 2", entity2.getLogradouro());
        assertEquals("2", entity2.getNumero());
    }

    @Test
    void testConvertToDtoList() {
        Address entity1 = new Address();
        entity1.setId(400L);
        entity1.setLogradouro("Logradouro 1");
        entity1.setNumero("10");
        entity1.setBairro("Bairro 10");
        entity1.setCep("33333-333");
        entity1.setMunicipio("Municipio 1");
        entity1.setEstado("Estado 1");

        Address entity2 = new Address();
        entity2.setId(500L);
        entity2.setLogradouro("Logradouro 2");
        entity2.setNumero("20");
        entity2.setBairro("Bairro 20");
        entity2.setCep("44444-444");
        entity2.setMunicipio("Municipio 2");
        entity2.setEstado("Estado 2");

        List<Address> entities = Arrays.asList(entity1, entity2);
        List<AddressDTO> dtos = addressConverter.convertToDto(entities);

        assertNotNull(dtos, "A lista de DTOs não deve ser nula");
        assertEquals(2, dtos.size(), "A lista deve conter dois elementos");

        AddressDTO dto1 = dtos.get(0);
        assertEquals(400L, dto1.getId());
        assertEquals("Logradouro 1", dto1.getLogradouro());
        assertEquals("10", dto1.getNumero());

        AddressDTO dto2 = dtos.get(1);
        assertEquals(500L, dto2.getId());
        assertEquals("Logradouro 2", dto2.getLogradouro());
        assertEquals("20", dto2.getNumero());
    }
}