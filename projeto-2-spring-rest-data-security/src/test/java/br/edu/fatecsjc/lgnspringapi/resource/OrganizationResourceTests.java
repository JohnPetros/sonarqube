package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.service.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationResourceTests {
    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OrganizationResource organizationResource;

    private OrganizationDTO org1;
    private OrganizationDTO org2;

    @BeforeEach
    void setUp() {
        org1 = OrganizationDTO.builder()
                .id(1L)
                .nome("Organization A")
                .instituicaoEnsino("Instituição A")
                .paisSede("Brasil")
                .build();
        org2 = OrganizationDTO.builder()
                .id(2L)
                .nome("Organization B")
                .instituicaoEnsino("Instituição B")
                .paisSede("Argentina")
                .build();
    }

    @Test
    void testGetAllOrganizations() {
        List<OrganizationDTO> allOrgs = List.of(org1, org2);
        when(organizationService.findAll()).thenReturn(allOrgs);
        ResponseEntity<List<OrganizationDTO>> response = organizationResource.getAllOrganizations();
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(200), ResponseEntity::getStatusCode)
                .satisfies(res -> assertThat(res.getBody()).containsExactly(org1, org2));
        verify(organizationService).findAll();
    }

    @Test
    void testGetOrganizationById() {
        when(organizationService.findById(5L)).thenReturn(org1);
        ResponseEntity<OrganizationDTO> response = organizationResource.getGroupById(5L);
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(200), ResponseEntity::getStatusCode)
                .returns(org1, ResponseEntity::getBody);
        verify(organizationService).findById(5L);
    }

    @Test
    void testUpdate() {
        when(organizationService.save(3L, org1)).thenReturn(org1);
        ResponseEntity<OrganizationDTO> response = organizationResource.update(3L, org1);
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(201), ResponseEntity::getStatusCode)
                .returns(org1, ResponseEntity::getBody);
        verify(organizationService).save(3L, org1);
    }

    @Test
    void testRegister() {
        when(organizationService.save(org2)).thenReturn(org2);
        ResponseEntity<OrganizationDTO> response = organizationResource.register(org2);
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(201), ResponseEntity::getStatusCode)
                .returns(org2, ResponseEntity::getBody);
        verify(organizationService).save(org2);
    }

    @Test
    void testDelete() {
        ResponseEntity<Void> response = organizationResource.delete(10L);
        verify(organizationService).deleteById(10L);
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(204), ResponseEntity::getStatusCode)
                .returns(null, ResponseEntity::getBody);
    }
}