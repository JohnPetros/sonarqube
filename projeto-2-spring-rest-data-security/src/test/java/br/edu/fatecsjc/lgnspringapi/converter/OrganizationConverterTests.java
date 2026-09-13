package br.edu.fatecsjc.lgnspringapi.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class OrganizationConverterTests {
    private OrganizationConverter organizationConverter;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        organizationConverter = new OrganizationConverter(modelMapper);
    }

    @Test
    void testConvertToEntity() {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setId(101L);
        dto.setNome("Organization Test");
        dto.setInstituicaoEnsino("Test College");
        dto.setPaisSede("Test Country");

        Organization org = organizationConverter.convertToEntity(dto);

        assertNotNull(org, "A organização convertida não deve ser nula");
        assertNull(org.getId(), "O id deve ser nulo, pois foi configurado para ser ignorado");
        assertNull(org.getName(), "O campo 'name' não será mapeado, pois não há conversão de 'nome'");
        assertEquals("Test College", org.getInstituicaoEnsino(),
                "O campo 'instituicaoEnsino' deve ser mapeado corretamente");
        assertEquals("Test Country", org.getPaisSede(),
                "O campo 'paisSede' deve ser mapeado corretamente");
    }

    @Test
    void testConvertToEntityWithExistingEntity() {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setNome("New Organization");
        dto.setInstituicaoEnsino("New College");
        dto.setPaisSede("New Country");

        Organization existing = new Organization();
        existing.setId(500L);
        existing.setName("Old Organization");
        existing.setInstituicaoEnsino("Old College");
        existing.setPaisSede("Old Country");

        Organization result = organizationConverter.convertToEntity(dto, existing);

        assertSame(existing, result, "A instância retornada deve ser a mesma da entidade existente");
        assertEquals(500L, result.getId(), "O id existente não deve ser alterado");
        assertEquals("Old Organization", result.getName(),
                "O campo 'name' não foi atualizado, pois não há mapeamento de 'nome'");
        assertEquals("New College", result.getInstituicaoEnsino(),
                "O campo 'instituicaoEnsino' deve ser atualizado para o novo valor");
        assertEquals("New Country", result.getPaisSede(),
                "O campo 'paisSede' deve ser atualizado para o novo valor");
    }

    @Test
    void testConvertToDto() {
        Organization org = new Organization();
        org.setId(300L);
        org.setName("Organization DTO");
        org.setInstituicaoEnsino("Institute of Testing");
        org.setPaisSede("Country Testing");

        OrganizationDTO dto = organizationConverter.convertToDto(org);

        assertNotNull(dto, "O DTO convertido não deve ser nulo");
        assertEquals(300L, dto.getId(), "O id deve ter sido mapeado corretamente");
        assertNull(dto.getNome(), "O campo 'nome' permanecerá nulo pois não há conversão de 'name'");
        assertEquals("Institute of Testing", dto.getInstituicaoEnsino(),
                "O campo 'instituicaoEnsino' deve ter sido mapeado corretamente");
        assertEquals("Country Testing", dto.getPaisSede(),
                "O campo 'paisSede' deve ter sido mapeado corretamente");
    }

    @Test
    void testConvertToEntityListEmpty() {
        List<OrganizationDTO> dtoList = Collections.emptyList();
        List<Organization> orgList = organizationConverter.convertToEntity(dtoList);

        assertNotNull(orgList, "A lista convertida não deve ser nula");
        assertTrue(orgList.isEmpty(), "A lista convertida deve estar vazia");
    }

    @Test
    void testConvertToEntityListNonEmpty() {
        OrganizationDTO dto1 = new OrganizationDTO();
        dto1.setNome("Org 1");
        dto1.setInstituicaoEnsino("College 1");
        dto1.setPaisSede("Country 1");

        OrganizationDTO dto2 = new OrganizationDTO();
        dto2.setNome("Org 2");
        dto2.setInstituicaoEnsino("College 2");
        dto2.setPaisSede("Country 2");

        List<OrganizationDTO> dtoList = Arrays.asList(dto1, dto2);
        List<Organization> orgList = organizationConverter.convertToEntity(dtoList);

        assertNotNull(orgList, "A lista de entidades não deve ser nula");
        assertEquals(2, orgList.size(), "A lista deve conter dois elementos");

        Organization o1 = orgList.get(0);
        assertNull(o1.getId(), "O id deve ser nulo devido à configuração do mapper");
        assertNull(o1.getName(), "O campo 'name' não é mapeado a partir de 'nome'");
        assertEquals("College 1", o1.getInstituicaoEnsino());
        assertEquals("Country 1", o1.getPaisSede());

        Organization o2 = orgList.get(1);
        assertNull(o2.getId(), "O id deve ser nulo devido à configuração do mapper");
        assertNull(o2.getName(), "O campo 'name' não é mapeado a partir de 'nome'");
        assertEquals("College 2", o2.getInstituicaoEnsino());
        assertEquals("Country 2", o2.getPaisSede());
    }

    @Test
    void testConvertToDtoListEmpty() {
        List<Organization> orgList = Collections.emptyList();
        List<OrganizationDTO> dtoList = organizationConverter.convertToDto(orgList);

        assertNotNull(dtoList, "A lista convertida não deve ser nula");
        assertTrue(dtoList.isEmpty(), "A lista convertida deve estar vazia");
    }

    @Test
    void testConvertToDtoListNonEmpty() {
        Organization org1 = new Organization();
        org1.setId(1L);
        org1.setName("Org 1");
        org1.setInstituicaoEnsino("College 1");
        org1.setPaisSede("Country 1");

        Organization org2 = new Organization();
        org2.setId(2L);
        org2.setName("Org 2");
        org2.setInstituicaoEnsino("College 2");
        org2.setPaisSede("Country 2");

        List<Organization> orgList = Arrays.asList(org1, org2);
        List<OrganizationDTO> dtoList = organizationConverter.convertToDto(orgList);

        assertNotNull(dtoList, "A lista de DTOs não deve ser nula");
        assertEquals(2, dtoList.size(), "A lista deve conter dois elementos");

        OrganizationDTO dto1 = dtoList.get(0);
        assertEquals(1L, dto1.getId());
        assertNull(dto1.getNome(), "O campo 'nome' não foi mapeado a partir de 'name'");
        assertEquals("College 1", dto1.getInstituicaoEnsino());
        assertEquals("Country 1", dto1.getPaisSede());

        OrganizationDTO dto2 = dtoList.get(1);
        assertEquals(2L, dto2.getId());
        assertNull(dto2.getNome(), "O campo 'nome' não foi mapeado a partir de 'name'");
        assertEquals("College 2", dto2.getInstituicaoEnsino());
        assertEquals("Country 2", dto2.getPaisSede());
    }
}