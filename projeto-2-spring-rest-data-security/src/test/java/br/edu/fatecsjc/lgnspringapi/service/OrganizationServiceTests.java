package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.AddressConverter;
import br.edu.fatecsjc.lgnspringapi.converter.GroupConverter;
import br.edu.fatecsjc.lgnspringapi.converter.OrganizationConverter;
import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Address;
import br.edu.fatecsjc.lgnspringapi.entity.GroupEntity;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import br.edu.fatecsjc.lgnspringapi.repository.AddressRepository;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTests {
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private AddressRepository addressRepository;

    @Mock
    private OrganizationConverter organizationConverter;
    @Mock
    private GroupConverter groupConverter;
    @Mock
    private AddressConverter addressConverter;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private OrganizationService organizationService;

    private Organization orgEntity;
    private OrganizationDTO orgDTO;
    private Address addrEntity;
    private GroupEntity groupEntityNew;
    private GroupEntity groupEntityExisting;
    private GroupDTO groupDTONew;
    private List<Long> existingGroupIds;

    @BeforeEach
    void setUp() {
        orgEntity = new Organization();
        orgEntity.setId(1L);
        orgEntity.setName("Org Test");
        orgEntity.setInstituicaoEnsino("Inst Test");
        orgEntity.setPaisSede("Brasil");

        orgDTO = OrganizationDTO.builder()
                .id(1L)
                .nome("Org Test")
                .instituicaoEnsino("Inst Test")
                .paisSede("Brasil")
                .build();
        addrEntity = new Address();
        addrEntity.setId(10L);
        addrEntity.setLogradouro("Rua Teste");
        groupDTONew = new GroupDTO();
        groupDTONew.setName("New Group");
        groupEntityNew = new GroupEntity();
        groupEntityNew.setId(100L);
        groupEntityNew.setName("New Group");
        groupEntityExisting = new GroupEntity();
        groupEntityExisting.setId(200L);
        groupEntityExisting.setName("Existing Group");
        existingGroupIds = new ArrayList<>();
        existingGroupIds.add(200L);
        orgDTO.setGrupos(List.of(groupDTONew));
        orgDTO.setGruposId(existingGroupIds);
    }

    @Test
    void testFindAll() {
        Organization orgEntity2 = new Organization();
        orgEntity2.setId(2L);
        orgEntity2.setName("Org2");
        orgEntity2.setInstituicaoEnsino("Inst2");
        orgEntity2.setPaisSede("Argentina");
        List<Organization> orgList = List.of(orgEntity, orgEntity2);
        when(organizationRepository.findAll()).thenReturn(orgList);
        OrganizationDTO dto1 = OrganizationDTO.builder().id(1L).build();
        OrganizationDTO dto2 = OrganizationDTO.builder().id(2L).build();
        when(organizationConverter.convertToDto(orgEntity)).thenReturn(dto1);
        when(organizationConverter.convertToDto(orgEntity2)).thenReturn(dto2);
        when(addressRepository.findByOrganizationId(1L)).thenReturn(addrEntity);
        when(addressConverter.convertToDto(addrEntity)).thenReturn(null);
        when(groupRepository.findByOrganizationId(1L)).thenReturn(new ArrayList<>());
        when(groupConverter.convertToDto(new ArrayList<>())).thenReturn(new ArrayList<>());
        when(addressRepository.findByOrganizationId(2L)).thenReturn(null);
        when(groupRepository.findByOrganizationId(2L)).thenReturn(new ArrayList<>());
        when(groupConverter.convertToDto(new ArrayList<>())).thenReturn(new ArrayList<>());
        List<OrganizationDTO> result = organizationService.findAll();
        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        verify(organizationRepository).findAll();
    }

    @Test
    void testFindByIdFound() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(orgEntity));
        when(organizationConverter.convertToDto(orgEntity)).thenReturn(orgDTO);
        when(addressRepository.findByOrganizationId(1L)).thenReturn(addrEntity);
        when(addressConverter.convertToDto(addrEntity)).thenReturn(null);
        when(groupRepository.findByOrganizationId(1L)).thenReturn(new ArrayList<>());
        when(groupConverter.convertToDto(new ArrayList<>())).thenReturn(new ArrayList<>());
        OrganizationDTO result = organizationService.findById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(organizationRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> organizationService.findById(1L));
        assertThat(ex).hasMessage("Organização não encontrada");
    }

    @Test
    void testSaveWithId() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(orgEntity));
        doNothing().when(groupRepository).nullifyOrganizationId(1L);
        Organization updatedOrg = new Organization();
        updatedOrg.setId(1L);
        updatedOrg.setName("Org Test Updated");
        when(organizationConverter.convertToEntity(orgDTO, orgEntity)).thenReturn(updatedOrg);
        when(organizationRepository.save(updatedOrg)).thenReturn(updatedOrg);
        GroupDTO savedGroupDTO = new GroupDTO();
        savedGroupDTO.setId(100L);
        when(groupService.save(any(GroupDTO.class))).thenReturn(savedGroupDTO);
        when(groupRepository.findById(100L)).thenReturn(Optional.of(groupEntityNew));
        when(groupRepository.findById(200L)).thenReturn(Optional.of(groupEntityExisting));
        doReturn(new ArrayList<GroupEntity>()).when(groupRepository).saveAll(anyList());
        when(addressConverter.convertToEntity(orgDTO.getEndereco())).thenReturn(addrEntity);
        addrEntity.setOrganization(updatedOrg);
        when(addressRepository.save(addrEntity)).thenReturn(addrEntity);
        OrganizationDTO returnedDTO = OrganizationDTO.builder().id(1L).build();
        when(organizationConverter.convertToDto(updatedOrg)).thenReturn(returnedDTO);
        when(groupConverter.convertToDto(anyList())).thenReturn(new ArrayList<>());
        OrganizationDTO result = organizationService.save(1L, orgDTO);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(groupRepository).nullifyOrganizationId(1L);
        verify(organizationRepository).save(updatedOrg);
        verify(groupRepository, times(1)).saveAll(anyList());
        verify(addressRepository).save(addrEntity);
    }

    @Test
    void testSaveNewOrganization() {
        Organization orgToSave = new Organization();
        when(organizationConverter.convertToEntity(orgDTO)).thenReturn(orgToSave);
        Organization savedOrg = new Organization();
        savedOrg.setId(10L);
        when(organizationRepository.save(orgToSave)).thenReturn(savedOrg);
        GroupDTO savedGroupDTO = new GroupDTO();
        savedGroupDTO.setId(100L);
        when(groupService.save(any(GroupDTO.class))).thenReturn(savedGroupDTO);
        when(groupRepository.findById(100L)).thenReturn(Optional.of(groupEntityNew));
        // Simula grupos existentes
        when(groupRepository.findById(200L)).thenReturn(Optional.of(groupEntityExisting));
        doReturn(new ArrayList<GroupEntity>()).when(groupRepository).saveAll(anyList());
        when(addressConverter.convertToEntity(orgDTO.getEndereco())).thenReturn(addrEntity);
        addrEntity.setOrganization(savedOrg);
        when(addressRepository.save(addrEntity)).thenReturn(addrEntity);
        OrganizationDTO returnedDTO = OrganizationDTO.builder().id(10L).build();
        when(organizationConverter.convertToDto(savedOrg)).thenReturn(returnedDTO);
        when(groupConverter.convertToDto(anyList())).thenReturn(new ArrayList<>());
        OrganizationDTO result = organizationService.save(orgDTO);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        verify(organizationRepository).save(orgToSave);
        verify(groupRepository).saveAll(anyList());
        verify(addressRepository).save(addrEntity);
    }

    @Test
    void testDeleteByIdSuccess() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(orgEntity));
        doNothing().when(groupRepository).nullifyOrganizationId(1L);
        when(addressRepository.findByOrganizationId(1L)).thenReturn(addrEntity);
        doNothing().when(addressRepository).delete(addrEntity);
        doNothing().when(organizationRepository).deleteById(1L);
        organizationService.deleteById(1L);
        verify(organizationRepository).findById(1L);
        verify(groupRepository).nullifyOrganizationId(1L);
        verify(addressRepository).delete(addrEntity);
        verify(organizationRepository).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> organizationService.deleteById(1L));
        assertThat(ex).hasMessage(String.format("Organização com o id %d não encontrada", 1L));
    }
}