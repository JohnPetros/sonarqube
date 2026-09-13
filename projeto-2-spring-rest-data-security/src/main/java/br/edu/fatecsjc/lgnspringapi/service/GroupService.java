package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.GroupConverter;
import br.edu.fatecsjc.lgnspringapi.converter.MemberConverter;
import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.entity.GroupEntity;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupConverter groupConverter;
    private final MemberConverter memberConverter;

    public GroupService(GroupRepository groupRepository,
            MemberRepository memberRepository,
            GroupConverter groupConverter,
            MemberConverter memberConverter) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.groupConverter = groupConverter;
        this.memberConverter = memberConverter;
    }

    public List<GroupDTO> getAll() {
        List<GroupEntity> groups = groupRepository.findAll();
        List<GroupDTO> groupDTOs = groupConverter.convertToDto(groups);

        groupDTOs.forEach(dto -> {
            List<Member> members = memberRepository.findByGroupId(dto.getId());
            dto.setMembers(memberConverter.convertToDto(members));
        });

        return groupDTOs;
    }

    public GroupDTO findById(Long id) {
        GroupEntity group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        GroupDTO groupDTO = groupConverter.convertToDto(group);
        List<Member> members = memberRepository.findByGroupId(id);
        groupDTO.setMembers(memberConverter.convertToDto(members));

        return groupDTO;
    }

    @Transactional
    public GroupDTO save(Long id, GroupDTO dto) {
        GroupEntity entity = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        memberRepository.deleteByGroup(entity);

        GroupEntity updatedGroup = groupConverter.convertToEntity(dto, entity);
        GroupEntity savedGroup = groupRepository.save(updatedGroup);
        List<Member> membersToSave = dto.getMembers()
                .stream()
                .map(memberDTO -> {
                    Member member = memberConverter.convertToEntity(memberDTO);
                    member.setGroup(savedGroup);
                    return member;
                })
                .toList();

        memberRepository.saveAll(membersToSave);

        GroupDTO returnedDTO = groupConverter.convertToDto(savedGroup);
        returnedDTO.setMembers(memberConverter.convertToDto(membersToSave));
        return returnedDTO;
    }

    public GroupDTO save(GroupDTO dto) {
        GroupEntity groupToSave = groupConverter.convertToEntity(dto);
        GroupEntity savedGroup = groupRepository.save(groupToSave);
        List<Member> membersToSave = dto.getMembers()
                .stream()
                .map(memberDTO -> {
                    Member member = memberConverter.convertToEntity(memberDTO);
                    member.setGroup(savedGroup);
                    return member;
                })
                .toList();

        memberRepository.saveAll(membersToSave);

        GroupDTO returnedDTO = groupConverter.convertToDto(savedGroup);
        returnedDTO.setMembers(memberConverter.convertToDto(membersToSave));
        return returnedDTO;
    }

    @Transactional
    public void delete(Long id) {
        GroupEntity group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grupo com id " + id + " não encontrado"));

        memberRepository.deleteByGroup(group);
        groupRepository.deleteById(id);
    }
}