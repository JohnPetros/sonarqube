package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.MemberConverter;
import br.edu.fatecsjc.lgnspringapi.converter.MarathonConverter;
import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.dto.MarathonDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.entity.Marathon;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MarathonRepository;
import jakarta.transaction.Transactional;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarathonServiceTests {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MarathonRepository marathonRepository;

    @Mock
    private MemberConverter memberConverter;

    @Mock
    private MarathonConverter marathonConverter;

    @InjectMocks
    private MarathonService marathonService;

    @Test
    void testGetAll() {
        Member member1 = new Member();
        member1.setId(1L);
        Member member2 = new Member();
        member2.setId(2L);
        List<Member> memberList = List.of(member1, member2);
        when(memberRepository.findAll()).thenReturn(memberList);
        MemberDTO dto1 = new MemberDTO();
        dto1.setId(1L);
        MemberDTO dto2 = new MemberDTO();
        dto2.setId(2L);
        List<MemberDTO> dtoList = List.of(dto1, dto2);
        when(memberConverter.convertToDto(memberList)).thenReturn(dtoList);
        List<Marathon> emptyMarathons = new ArrayList<>();
        when(marathonRepository.findByMemberId(1L)).thenReturn(emptyMarathons);
        when(marathonRepository.findByMemberId(2L)).thenReturn(emptyMarathons);
        when(marathonConverter.convertToDto(emptyMarathons)).thenReturn(new ArrayList<>());
        List<MemberDTO> result = marathonService.getAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getMarathons()).isEmpty();
        assertThat(result.get(1).getMarathons()).isEmpty();
        verify(memberRepository).findAll();
        verify(memberConverter).convertToDto(memberList);
    }

    @Test
    void testFindByIdFound() {
        Member member = new Member();
        member.setId(42L);
        when(memberRepository.findById(42L)).thenReturn(Optional.of(member));

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(42L);
        when(memberConverter.convertToDto(member)).thenReturn(memberDTO);

        List<Marathon> marathons = List.of(new Marathon(), new Marathon());
        when(marathonRepository.findByMemberId(42L)).thenReturn(marathons);
        List<MarathonDTO> marathonDTOs = List.of(new MarathonDTO(), new MarathonDTO());
        when(marathonConverter.convertToDto(marathons)).thenReturn(marathonDTOs);

        MemberDTO result = marathonService.findById(42L);
        assertThat(result.getId()).isEqualTo(42L);
        assertThat(result.getMarathons()).isEqualTo(marathonDTOs);
        verify(memberRepository).findById(42L);
    }

    @Test
    void testFindByIdNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> marathonService.findById(1L));
        assertThat(ex).hasMessage("Membro não encontrado");
    }

    @Test
    @Transactional
    void testUpdateMarathons() {
        Member member = new Member();
        member.setId(5L);
        when(memberRepository.findById(5L)).thenReturn(Optional.of(member));
        MarathonDTO mDto1 = new MarathonDTO();
        MarathonDTO mDto2 = new MarathonDTO();
        List<MarathonDTO> dtoList = List.of(mDto1, mDto2);
        Marathon mEntity1 = new Marathon();
        Marathon mEntity2 = new Marathon();
        when(marathonConverter.convertToEntity(mDto1)).thenReturn(mEntity1);
        when(marathonConverter.convertToEntity(mDto2)).thenReturn(mEntity2);
        List<Marathon> marathonsToSave = List.of(mEntity1, mEntity2);
        when(marathonRepository.saveAll(anyList())).thenReturn(marathonsToSave);
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(5L);
        when(memberConverter.convertToDto(member)).thenReturn(memberDTO);
        List<MarathonDTO> convertedMarathonDTOs = List.of(new MarathonDTO(), new MarathonDTO());
        when(marathonConverter.convertToDto(anyList())).thenReturn(convertedMarathonDTOs);
        MemberDTO result = marathonService.updateMarathons(5L, dtoList);
        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getMarathons()).isEqualTo(convertedMarathonDTOs);
        verify(memberRepository).findById(5L);
        verify(marathonRepository).deleteByMember(member);
        verify(marathonRepository).saveAll(anyList());
    }

    @Test
    @Transactional
    void testAddMarathons() {
        Member member = new Member();
        member.setId(7L);
        when(memberRepository.findById(7L)).thenReturn(Optional.of(member));
        MarathonDTO mDto = new MarathonDTO();
        List<MarathonDTO> dtoList = List.of(mDto);
        Marathon mEntity = new Marathon();
        when(marathonConverter.convertToEntity(mDto)).thenReturn(mEntity);
        List<Marathon> savedMarathons = List.of(mEntity);
        when(marathonRepository.saveAll(anyList())).thenReturn(savedMarathons);
        when(marathonRepository.findByMemberId(7L)).thenReturn(savedMarathons);
        List<MarathonDTO> convertedMarathonDTOs = List.of(new MarathonDTO());
        when(marathonConverter.convertToDto(savedMarathons)).thenReturn(convertedMarathonDTOs);
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(7L);
        when(memberConverter.convertToDto(member)).thenReturn(memberDTO);
        MemberDTO result = marathonService.addMarathons(7L, dtoList);
        assertThat(result.getId()).isEqualTo(7L);
        assertThat(result.getMarathons()).isEqualTo(convertedMarathonDTOs);
        verify(memberRepository).findById(7L);
        verify(marathonRepository).saveAll(anyList());
        verify(marathonRepository).findByMemberId(7L);
    }

    @Test
    @Transactional
    void testDelete() {
        Member member = new Member();
        member.setId(9L);
        when(memberRepository.findById(9L)).thenReturn(Optional.of(member));
        doNothing().when(marathonRepository).deleteByMember(member);
        doNothing().when(memberRepository).deleteById(9L);
        marathonService.delete(9L);
        verify(memberRepository).findById(9L);
        verify(marathonRepository).deleteByMember(member);
        verify(memberRepository).deleteById(9L);
    }
}