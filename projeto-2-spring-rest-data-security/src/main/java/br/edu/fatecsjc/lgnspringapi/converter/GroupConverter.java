package br.edu.fatecsjc.lgnspringapi.converter;

import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.entity.GroupEntity;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupConverter implements Converter<GroupEntity, GroupDTO> {
    private final ModelMapper modelMapper;
    private TypeMap<GroupDTO, GroupEntity> propertyMapperDto;
    private TypeMap<GroupEntity, GroupDTO> propertyMapperEntity;

    public GroupConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        initializeMapper();
    }

    private void initializeMapper() {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(GroupDTO.class, GroupEntity.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(GroupEntity::setId));
        }

        if (propertyMapperEntity == null) {
            propertyMapperEntity = modelMapper.createTypeMap(GroupEntity.class, GroupDTO.class);
        }
    }

    @Override
    public GroupEntity convertToEntity(GroupDTO dto) {
        initializeMapper();
        return modelMapper.map(dto, GroupEntity.class);
    }

    @Override
    public GroupEntity convertToEntity(GroupDTO dto, GroupEntity entity) {
        initializeMapper();
        propertyMapperDto.setProvider(p -> entity);
        return modelMapper.map(dto, GroupEntity.class);
    }

    @Override
    public GroupDTO convertToDto(GroupEntity entity) {
        initializeMapper();
        return modelMapper.map(entity, GroupDTO.class);
    }

    @Override
    public List<GroupEntity> convertToEntity(List<GroupDTO> dtos) {
        initializeMapper();
        return modelMapper.map(dtos, new TypeToken<List<Member>>() {
        }.getType());
    }

    @Override
    public List<GroupDTO> convertToDto(List<GroupEntity> entities) {
        initializeMapper();
        return entities.stream().map(this::convertToDto).toList();
    }
}