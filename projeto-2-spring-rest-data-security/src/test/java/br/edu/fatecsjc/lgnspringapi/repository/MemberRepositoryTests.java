package br.edu.fatecsjc.lgnspringapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import br.edu.fatecsjc.lgnspringapi.entity.GroupEntity;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTests {
        @Autowired
        private MemberRepository memberRepository;

        @Autowired
        private GroupRepository groupRepository;

        @Test
        void testFindByGroupId() {
                GroupEntity group = GroupEntity.builder()
                                .name("Group X")
                                .build();
                GroupEntity savedGroup = groupRepository.save(group);

                Member member1 = Member.builder()
                                .name("Member One")
                                .age(30)
                                .group(savedGroup)
                                .build();
                Member member2 = Member.builder()
                                .name("Member Two")
                                .age(25)
                                .group(savedGroup)
                                .build();
                memberRepository.save(member1);
                memberRepository.save(member2);

                List<Member> members = memberRepository.findByGroupId(savedGroup.getId());
                assertThat(members).hasSize(2);
                members.forEach(member -> assertThat(member.getGroup()).isEqualTo(savedGroup));
        }

        @Test
        void testDeleteByGroup() {
                GroupEntity group = GroupEntity.builder()
                                .name("Group Y")
                                .build();
                GroupEntity savedGroup = groupRepository.save(group);

                Member member1 = Member.builder()
                                .name("Member A")
                                .age(40)
                                .group(savedGroup)
                                .build();
                Member member2 = Member.builder()
                                .name("Member B")
                                .age(35)
                                .group(savedGroup)
                                .build();
                memberRepository.save(member1);
                memberRepository.save(member2);

                List<Member> membersBefore = memberRepository.findByGroupId(savedGroup.getId());
                assertThat(membersBefore).hasSize(2);

                memberRepository.deleteByGroup(savedGroup);

                List<Member> membersAfter = memberRepository.findByGroupId(savedGroup.getId());
                assertThat(membersAfter).isEmpty();
        }
}