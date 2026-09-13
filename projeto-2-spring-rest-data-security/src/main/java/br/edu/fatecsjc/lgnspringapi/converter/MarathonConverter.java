package br.edu.fatecsjc.lgnspringapi.converter;

import br.edu.fatecsjc.lgnspringapi.dto.MarathonDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Marathon;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarathonConverter implements Converter<Marathon, MarathonDTO> {
    private final ModelMapper modelMapper;
    private TypeMap<MarathonDTO, Marathon> propertyMapperDto;
    private TypeMap<Marathon, MarathonDTO> propertyMapperEntity;

    public MarathonConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        initializeMapper();
    }

    private void initializeMapper() {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(MarathonDTO.class, Marathon.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Marathon::setId));
        }

        if (propertyMapperEntity == null) {
            propertyMapperEntity = modelMapper.createTypeMap(Marathon.class, MarathonDTO.class);
        }
    }

    @Override
    public Marathon convertToEntity(MarathonDTO dto) {
        initializeMapper();
        return modelMapper.map(dto, Marathon.class);
    }

    @Override
    public Marathon convertToEntity(MarathonDTO dto, Marathon entity) {
        initializeMapper();
        propertyMapperDto.setProvider(p -> entity);
        return modelMapper.map(dto, Marathon.class);
    }

    @Override
    public MarathonDTO convertToDto(Marathon entity) {
        initializeMapper();
        return modelMapper.map(entity, MarathonDTO.class);
    }

    @Override
    public List<Marathon> convertToEntity(List<MarathonDTO> dtos) {
        initializeMapper();
        return modelMapper.map(dtos, new TypeToken<List<Marathon>>() {
        }.getType());
    }

    @Override
    public List<MarathonDTO> convertToDto(List<Marathon> entities) {
        initializeMapper();
        return modelMapper.map(entities, new TypeToken<List<MarathonDTO>>() {
        }.getType());
    }
}