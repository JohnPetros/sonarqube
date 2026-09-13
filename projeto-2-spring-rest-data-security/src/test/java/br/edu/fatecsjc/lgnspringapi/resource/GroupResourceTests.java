package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupResourceTests {
    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupResource groupResource;

    private GroupDTO group1;
    private GroupDTO group2;

    @BeforeEach
    void setUp() {
        group1 = new GroupDTO();
        group1.setId(1L);
        group1.setName("Group A");

        group2 = new GroupDTO();
        group2.setId(2L);
        group2.setName("Group B");
    }

    @Test
    void testGetAllGroups() {
        List<GroupDTO> allGroups = List.of(group1, group2);
        when(groupService.getAll()).thenReturn(allGroups);

        ResponseEntity<List<GroupDTO>> response = groupResource.getAllGroups();

        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(200), ResponseEntity::getStatusCode)
                .satisfies(res -> assertThat(res.getBody()).containsExactly(group1, group2));
    }

    @Test
    void testGetGroupById() {
        when(groupService.findById(5L)).thenReturn(group1);
        ResponseEntity<GroupDTO> response = groupResource.getGroupById(5L);

        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(200), ResponseEntity::getStatusCode)
                .returns(group1, ResponseEntity::getBody);
    }

    @Test
    void testUpdate() {
        when(groupService.save(3L, group1)).thenReturn(group1);
        ResponseEntity<GroupDTO> response = groupResource.update(3L, group1);

        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(201), ResponseEntity::getStatusCode)
                .returns(group1, ResponseEntity::getBody);
    }

    @Test
    void testRegister() {
        when(groupService.save(group2)).thenReturn(group2);
        ResponseEntity<GroupDTO> response = groupResource.register(group2);

        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(201), ResponseEntity::getStatusCode)
                .returns(group2, ResponseEntity::getBody);
    }

    @Test
    void testDelete() {
        ResponseEntity<Void> response = groupResource.update(10L);

        verify(groupService).delete(10L);
        assertThat(response)
                .isNotNull()
                .returns(HttpStatusCode.valueOf(204), ResponseEntity::getStatusCode)
                .returns(null, ResponseEntity::getBody);
    }
}