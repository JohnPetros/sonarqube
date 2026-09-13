package br.edu.fatecsjc.lgnspringapi.converter;

import java.util.List;

public class SimpleConverter implements Converter<SimpleEntity, SimpleDTO> {
    @Override
    public SimpleEntity convertToEntity(SimpleDTO dto) {
        if (dto == null)
            return null;
        return new SimpleEntity(dto.getValue());
    }

    @Override
    public SimpleEntity convertToEntity(SimpleDTO dto, SimpleEntity entity) {
        if (dto == null || entity == null)
            return null;
        entity.setValue(dto.getValue());
        return entity;
    }

    @Override
    public SimpleDTO convertToDto(SimpleEntity entity) {
        if (entity == null)
            return null;
        return new SimpleDTO(entity.getValue());
    }

    @Override
    public List<SimpleEntity> convertToEntity(List<SimpleDTO> dtos) {
        if (dtos == null)
            return null;
        return dtos.stream()
                .map(this::convertToEntity)
                .toList();
    }

    @Override
    public List<SimpleDTO> convertToDto(List<SimpleEntity> entities) {
        if (entities == null)
            return null;
        return entities.stream()
                .map(this::convertToDto)
                .toList();
    }
}
