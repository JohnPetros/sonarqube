package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.dto.MarathonDTO;
import br.edu.fatecsjc.lgnspringapi.service.MarathonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarathonResourceTests {
    @Mock
    private MarathonService marathonService;

    @InjectMocks
    private MarathonResource marathonResource;

    private MemberDTO member;
    private List<MarathonDTO> marathonList;

    @BeforeEach
    void setUp() {
        member = new MemberDTO();
        member.setId(1L);
        member.setName("Member A");
        MarathonDTO m1 = MarathonDTO.builder().id(10L).peso("75kg").score("200").build();
        MarathonDTO m2 = MarathonDTO.builder().id(11L).peso("80kg").score("210").build();
        marathonList = Arrays.asList(m1, m2);
    }

    @Test
    void testGetAllMembers() {
        List<MemberDTO> allMembers = List.of(member);
        when(marathonService.getAll()).thenReturn(allMembers);
        ResponseEntity<List<MemberDTO>> response = marathonResource.getAllMembers();
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(200), ResponseEntity::getStatusCode)
                .satisfies(res -> assertThat(res.getBody()).containsExactly(member));
        verify(marathonService).getAll();
    }

    @Test
    void testGetMemberById() {
        when(marathonService.findById(5L)).thenReturn(member);
        ResponseEntity<MemberDTO> response = marathonResource.getMemberById(5L);
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(200), ResponseEntity::getStatusCode)
                .returns(member, ResponseEntity::getBody);
        verify(marathonService).findById(5L);
    }

    @Test
    void testUpdateMarathons() {
        when(marathonService.updateMarathons(3L, marathonList)).thenReturn(member);
        ResponseEntity<MemberDTO> response = marathonResource.updateMarathons(3L, marathonList);
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(200), ResponseEntity::getStatusCode)
                .returns(member, ResponseEntity::getBody);
        verify(marathonService).updateMarathons(3L, marathonList);
    }

    @Test
    void testAddMarathons() {
        when(marathonService.addMarathons(2L, marathonList)).thenReturn(member);
        ResponseEntity<MemberDTO> response = marathonResource.addMarathons(2L, marathonList);
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(201), ResponseEntity::getStatusCode)
                .returns(member, ResponseEntity::getBody);
        verify(marathonService).addMarathons(2L, marathonList);
    }

    @Test
    void testDeleteMember() {
        ResponseEntity<Void> response = marathonResource.deleteMember(10L);
        verify(marathonService).delete(10L);
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(204), ResponseEntity::getStatusCode)
                .returns(null, ResponseEntity::getBody);
    }
}