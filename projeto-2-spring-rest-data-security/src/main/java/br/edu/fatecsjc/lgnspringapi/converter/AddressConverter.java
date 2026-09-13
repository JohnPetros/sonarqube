package br.edu.fatecsjc.lgnspringapi.converter;

import br.edu.fatecsjc.lgnspringapi.dto.AddressDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Address;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressConverter implements Converter<Address, AddressDTO> {
    private final ModelMapper modelMapper;
    private TypeMap<AddressDTO, Address> propertyMapperDto;
    private TypeMap<Address, AddressDTO> propertyMapperEntity;

    public AddressConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        initializeMapper();
    }

    private void initializeMapper() {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(AddressDTO.class, Address.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Address::setId));
        }

        if (propertyMapperEntity == null) {
            propertyMapperEntity = modelMapper.createTypeMap(Address.class, AddressDTO.class);
        }
    }

    @Override
    public Address convertToEntity(AddressDTO dto) {
        initializeMapper();
        return modelMapper.map(dto, Address.class);
    }

    @Override
    public Address convertToEntity(AddressDTO dto, Address entity) {
        initializeMapper();
        propertyMapperDto.setProvider(p -> entity);
        return modelMapper.map(dto, Address.class);
    }

    @Override
    public AddressDTO convertToDto(Address entity) {
        initializeMapper();
        return modelMapper.map(entity, AddressDTO.class);
    }

    @Override
    public List<Address> convertToEntity(List<AddressDTO> dtos) {
        initializeMapper();
        return modelMapper.map(dtos, new TypeToken<List<Address>>() {
        }.getType());
    }

    @Override
    public List<AddressDTO> convertToDto(List<Address> entities) {
        initializeMapper();
        return modelMapper.map(entities, new TypeToken<List<AddressDTO>>() {
        }.getType());
    }
}