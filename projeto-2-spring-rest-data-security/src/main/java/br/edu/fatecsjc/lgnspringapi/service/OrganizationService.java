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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private static final String ORGANIZATION_NOT_FOUND = "Organização não encontrada";
    private static final String ORGANIZATION_NOT_FOUND_BY_ID = "Organização com o id %d não encontrada";
    private static final String GROUP_NOT_FOUND_AFTER_SAVE = "Grupo não encontrado após salvar";
    private static final String GROUP_NOT_FOUND_WITH_ID = "Grupo não encontrado com o id: ";

    private final OrganizationRepository organizationRepository;
    private final GroupRepository groupRepository;
    private final AddressRepository addressRepository;
    private final OrganizationConverter organizationConverter;
    private final GroupConverter groupConverter;
    private final AddressConverter addressConverter;
    private final GroupService groupService;

    public List<OrganizationDTO> findAll() {
        List<Organization> organizations = organizationRepository.findAll();
        return organizations.stream()
                .map(org -> {
                    OrganizationDTO dto = organizationConverter.convertToDto(org);
                    dto.setEndereco(addressConverter.convertToDto(addressRepository.findByOrganizationId(org.getId())));
                    dto.setGrupos(groupConverter.convertToDto(groupRepository.findByOrganizationId(org.getId())));
                    return dto;
                })
                .toList();
    }

    public OrganizationDTO findById(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ORGANIZATION_NOT_FOUND));
        OrganizationDTO organizationDTO = organizationConverter.convertToDto(organization);
        organizationDTO.setEndereco(addressConverter.convertToDto(addressRepository.findByOrganizationId(id)));
        organizationDTO.setGrupos(groupConverter.convertToDto(groupRepository.findByOrganizationId(id)));
        return organizationDTO;
    }

    @Transactional
    public OrganizationDTO save(Long id, OrganizationDTO dto) {
        Organization entity = organizationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ORGANIZATION_NOT_FOUND));
        groupRepository.nullifyOrganizationId(id);
        Organization updatedOrganization = organizationConverter.convertToEntity(dto, entity);
        Organization savedOrganization = organizationRepository.save(updatedOrganization);
        return finalizeSave(savedOrganization, dto);
    }

    public OrganizationDTO save(OrganizationDTO dto) {
        Organization organizationToSave = organizationConverter.convertToEntity(dto);
        Organization savedOrganization = organizationRepository.save(organizationToSave);
        return finalizeSave(savedOrganization, dto);
    }

    private OrganizationDTO finalizeSave(Organization savedOrganization, OrganizationDTO dto) {
        List<GroupEntity> allGroups = processGroups(savedOrganization, dto);
        groupRepository.saveAll(allGroups);
        Address address = addressConverter.convertToEntity(dto.getEndereco());
        address.setOrganization(savedOrganization);
        addressRepository.save(address);
        OrganizationDTO returnedDTO = organizationConverter.convertToDto(savedOrganization);
        returnedDTO.setEndereco(dto.getEndereco());
        returnedDTO.setGrupos(groupConverter.convertToDto(allGroups));
        return returnedDTO;
    }

    private List<GroupEntity> processGroups(Organization savedOrganization, OrganizationDTO dto) {
        List<GroupEntity> newGroups = dto.getGrupos().stream()
                .map(groupDTO -> {
                    GroupDTO savedGroup = groupService.save(groupDTO);
                    GroupEntity groupEntity = groupRepository.findById(savedGroup.getId())
                            .orElseThrow(() -> new IllegalArgumentException(GROUP_NOT_FOUND_AFTER_SAVE));
                    groupEntity.setOrganization(savedOrganization);
                    return groupEntity;
                })
                .toList();
        List<GroupEntity> existingGroups = dto.getGruposId().stream()
                .map(groupId -> {
                    GroupEntity group = groupRepository.findById(groupId)
                            .orElseThrow(() -> new IllegalArgumentException(GROUP_NOT_FOUND_WITH_ID + groupId));
                    group.setOrganization(savedOrganization);
                    return group;
                })
                .toList();
        List<GroupEntity> allGroups = new ArrayList<>();
        allGroups.addAll(newGroups);
        allGroups.addAll(existingGroups);
        return allGroups;
    }

    @Transactional
    public void deleteById(Long id) {
        organizationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ORGANIZATION_NOT_FOUND_BY_ID, id)));
        groupRepository.nullifyOrganizationId(id);
        Address address = addressRepository.findByOrganizationId(id);
        if (address != null) {
            addressRepository.delete(address);
        }
        organizationRepository.deleteById(id);
    }
}