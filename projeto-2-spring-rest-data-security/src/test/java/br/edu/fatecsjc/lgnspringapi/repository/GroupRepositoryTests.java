package br.edu.fatecsjc.lgnspringapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import br.edu.fatecsjc.lgnspringapi.entity.GroupEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class GroupRepositoryTests {
        @Autowired
        private GroupRepository groupRepository;

        @Test
        void testSaveAndFindById() {
                GroupEntity group = GroupEntity.builder()
                                .name("Group A")
                                .build();

                GroupEntity savedGroup = groupRepository.save(group);
                assertThat(savedGroup.getId()).isNotNull();
                assertThat(savedGroup.getName()).isEqualTo("Group A");

                GroupEntity foundGroup = groupRepository.findById(savedGroup.getId()).orElse(null);
                assertThat(foundGroup).isNotNull();
                assertThat(foundGroup.getId()).isEqualTo(savedGroup.getId());
                assertThat(foundGroup.getName()).isEqualTo("Group A");
        }

        @Test
        void testDeleteGroup() {
                GroupEntity group = GroupEntity.builder()
                                .name("Group B")
                                .build();
                GroupEntity savedGroup = groupRepository.save(group);
                Long id = savedGroup.getId();
                assertThat(id).isNotNull();

                groupRepository.delete(savedGroup);
                GroupEntity deletedGroup = groupRepository.findById(id).orElse(null);
                assertThat(deletedGroup).isNull();
        }

        @Test
        void testFindAll() {
                GroupEntity group1 = GroupEntity.builder()
                                .name("Group C")
                                .build();
                GroupEntity group2 = GroupEntity.builder()
                                .name("Group D")
                                .build();
                groupRepository.save(group1);
                groupRepository.save(group2);

                assertThat(groupRepository.findAll()).hasSize(2);
        }
}