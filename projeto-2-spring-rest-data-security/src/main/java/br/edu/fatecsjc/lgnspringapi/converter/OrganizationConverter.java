package br.edu.fatecsjc.lgnspringapi.converter;

import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrganizationConverter implements Converter<Organization, OrganizationDTO> {
    private final ModelMapper modelMapper;
    private TypeMap<OrganizationDTO, Organization> propertyMapperDto;
    private TypeMap<Organization, OrganizationDTO> propertyMapperEntity;

    public OrganizationConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        initializeMapper();
    }

    private void initializeMapper() {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(OrganizationDTO.class, Organization.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Organization::setId));
        }

        if (propertyMapperEntity == null) {
            propertyMapperEntity = modelMapper.createTypeMap(Organization.class, OrganizationDTO.class);
        }
    }

    @Override
    public Organization convertToEntity(OrganizationDTO dto) {
        initializeMapper();
        return modelMapper.map(dto, Organization.class);
    }

    @Override
    public Organization convertToEntity(OrganizationDTO dto, Organization entity) {
        initializeMapper();
        propertyMapperDto.setProvider(p -> entity);
        return modelMapper.map(dto, Organization.class);
    }

    @Override
    public OrganizationDTO convertToDto(Organization entity) {
        initializeMapper();
        return modelMapper.map(entity, OrganizationDTO.class);
    }

    @Override
    public List<Organization> convertToEntity(List<OrganizationDTO> dtos) {
        initializeMapper();
        return modelMapper.map(dtos, new TypeToken<List<Organization>>() {
        }.getType());
    }

    @Override
    public List<OrganizationDTO> convertToDto(List<Organization> entities) {
        initializeMapper();
        return modelMapper.map(entities, new TypeToken<List<OrganizationDTO>>() {
        }.getType());
    }
}