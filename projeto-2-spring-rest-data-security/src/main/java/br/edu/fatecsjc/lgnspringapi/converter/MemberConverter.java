package br.edu.fatecsjc.lgnspringapi.converter;

import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberConverter implements Converter<Member, MemberDTO> {
    private final ModelMapper modelMapper;
    private TypeMap<MemberDTO, Member> propertyMapperDto;
    private TypeMap<Member, MemberDTO> propertyMapperEntity;

    public MemberConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        initializeMapper();
    }

    private void initializeMapper() {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(MemberDTO.class, Member.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Member::setId));
        }

        if (propertyMapperEntity == null) {
            propertyMapperEntity = modelMapper.createTypeMap(Member.class, MemberDTO.class);
        }
    }

    @Override
    public Member convertToEntity(MemberDTO dto) {
        initializeMapper();
        return modelMapper.map(dto, Member.class);
    }

    @Override
    public Member convertToEntity(MemberDTO dto, Member entity) {
        initializeMapper();
        propertyMapperDto.setProvider(p -> entity);
        return modelMapper.map(dto, Member.class);
    }

    @Override
    public MemberDTO convertToDto(Member entity) {
        initializeMapper();
        return modelMapper.map(entity, MemberDTO.class);
    }

    @Override
    public List<Member> convertToEntity(List<MemberDTO> dtos) {
        initializeMapper();
        return modelMapper.map(dtos, new TypeToken<List<Member>>() {
        }.getType());
    }

    @Override
    public List<MemberDTO> convertToDto(List<Member> entities) {
        initializeMapper();
        return modelMapper.map(entities, new TypeToken<List<MemberDTO>>() {
        }.getType());
    }
}