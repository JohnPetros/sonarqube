package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.GroupConverter;
import br.edu.fatecsjc.lgnspringapi.converter.MemberConverter;
import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.GroupEntity;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTests {
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GroupConverter groupConverter;

    @Mock
    private MemberConverter memberConverter;

    @InjectMocks
    private GroupService groupService;

    @Test
    void testGetAll() {
        GroupEntity group1 = new GroupEntity();
        group1.setId(1L);
        group1.setName("Group A");

        GroupEntity group2 = new GroupEntity();
        group2.setId(2L);
        group2.setName("Group B");

        List<GroupEntity> groups = List.of(group1, group2);
        when(groupRepository.findAll()).thenReturn(groups);

        GroupDTO dto1 = new GroupDTO();
        dto1.setId(1L);
        GroupDTO dto2 = new GroupDTO();
        dto2.setId(2L);
        List<GroupDTO> dtoList = List.of(dto1, dto2);
        when(groupConverter.convertToDto(groups)).thenReturn(dtoList);

        List<Member> members1 = List.of(new Member());
        List<Member> members2 = List.of(new Member());
        when(memberRepository.findByGroupId(1L)).thenReturn(members1);
        when(memberRepository.findByGroupId(2L)).thenReturn(members2);

        List<MemberDTO> memberDTOs1 = List.of(new MemberDTO());
        List<MemberDTO> memberDTOs2 = List.of(new MemberDTO());
        when(memberConverter.convertToDto(members1)).thenReturn(memberDTOs1);
        when(memberConverter.convertToDto(members2)).thenReturn(memberDTOs2);

        List<GroupDTO> result = groupService.getAll();

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting(GroupDTO::getId)
                .containsExactly(1L, 2L);

        assertThat(dto1.getMembers()).isEqualTo(memberDTOs1);
        assertThat(dto2.getMembers()).isEqualTo(memberDTOs2);
    }

    @Test
    void testFindByIdFound() {
        GroupEntity group = new GroupEntity();
        group.setId(1L);
        group.setName("Group A");

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        GroupDTO dto = new GroupDTO();
        dto.setId(1L);
        when(groupConverter.convertToDto(group)).thenReturn(dto);

        List<Member> members = List.of(new Member());
        when(memberRepository.findByGroupId(1L)).thenReturn(members);

        List<MemberDTO> memberDTOs = List.of(new MemberDTO());
        when(memberConverter.convertToDto(members)).thenReturn(memberDTOs);

        GroupDTO result = groupService.findById(1L);

        assertThat(result)
                .isNotNull()
                .satisfies(g -> {
                    assertThat(g.getId()).isEqualTo(1L);
                    assertThat(g.getMembers()).isEqualTo(memberDTOs);
                });
    }

    @Test
    void testFindByIdNotFound() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> groupService.findById(1L));
        assertThat(ex).hasMessage("Group not found");
    }

    @Test
    @Transactional
    void testSaveWithId() {
        GroupEntity existingGroup = new GroupEntity();
        existingGroup.setId(1L);
        existingGroup.setName("Old Group");
        when(groupRepository.findById(1L)).thenReturn(Optional.of(existingGroup));

        GroupDTO dto = new GroupDTO();
        dto.setId(1L);
        List<MemberDTO> memberDTOs = List.of(new MemberDTO());
        dto.setMembers(memberDTOs);

        GroupEntity updatedGroup = new GroupEntity();
        updatedGroup.setId(1L);
        updatedGroup.setName("New Group");
        when(groupConverter.convertToEntity(dto, existingGroup)).thenReturn(updatedGroup);

        GroupEntity savedGroup = new GroupEntity();
        savedGroup.setId(1L);
        savedGroup.setName("New Group");
        when(groupRepository.save(updatedGroup)).thenReturn(savedGroup);

        MemberDTO dummyMemberDTO = memberDTOs.get(0);
        Member convertedMember = new Member();
        when(memberConverter.convertToEntity(dummyMemberDTO)).thenReturn(convertedMember);
        when(memberRepository.saveAll(anyList())).thenReturn(List.of(convertedMember));

        GroupDTO convertedDTO = new GroupDTO();
        convertedDTO.setId(1L);
        when(groupConverter.convertToDto(savedGroup)).thenReturn(convertedDTO);

        List<MemberDTO> convertedMemberDTOs = List.of(new MemberDTO());
        when(memberConverter.convertToDto(anyList())).thenReturn(convertedMemberDTOs);

        GroupDTO result = groupService.save(1L, dto);

        assertThat(result)
                .isNotNull()
                .satisfies(g -> {
                    assertThat(g.getId()).isEqualTo(1L);
                    assertThat(g.getMembers()).isEqualTo(convertedMemberDTOs);
                });
        verify(memberRepository).deleteByGroup(existingGroup);
        verify(groupRepository).save(updatedGroup);
        verify(memberRepository).saveAll(anyList());
    }

    @Test
    void testSaveNewGroup() {
        GroupDTO dto = new GroupDTO();
        List<MemberDTO> memberDTOs = List.of(new MemberDTO());
        dto.setMembers(memberDTOs);

        GroupEntity groupToSave = new GroupEntity();
        groupToSave.setName("Group X");
        when(groupConverter.convertToEntity(dto)).thenReturn(groupToSave);

        GroupEntity savedGroup = new GroupEntity();
        savedGroup.setId(10L);
        savedGroup.setName("Group X");
        when(groupRepository.save(groupToSave)).thenReturn(savedGroup);

        MemberDTO memberDTO = memberDTOs.get(0);
        Member convertedMember = new Member();
        when(memberConverter.convertToEntity(memberDTO)).thenReturn(convertedMember);
        when(memberRepository.saveAll(anyList())).thenReturn(List.of(convertedMember));

        GroupDTO convertedDTO = new GroupDTO();
        convertedDTO.setId(10L);
        when(groupConverter.convertToDto(savedGroup)).thenReturn(convertedDTO);

        List<MemberDTO> convertedMemberDTOs = List.of(new MemberDTO());
        when(memberConverter.convertToDto(anyList())).thenReturn(convertedMemberDTOs);

        GroupDTO result = groupService.save(dto);

        assertThat(result)
                .isNotNull()
                .satisfies(g -> {
                    assertThat(g.getId()).isEqualTo(10L);
                    assertThat(g.getMembers()).isEqualTo(convertedMemberDTOs);
                });
        verify(groupRepository).save(groupToSave);
        verify(memberRepository).saveAll(anyList());
    }

    @Test
    @Transactional
    void testDeleteSuccess() {
        GroupEntity group = new GroupEntity();
        group.setId(1L);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        groupService.delete(1L);

        verify(memberRepository).deleteByGroup(group);
        verify(groupRepository).deleteById(1L);
    }

    @Test
    void testDeleteGroupNotFound() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> groupService.delete(1L));
        assertThat(ex).hasMessage("Grupo com id 1 não encontrado");
    }
}