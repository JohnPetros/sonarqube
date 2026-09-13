package br.edu.fatecsjc.lgnspringapi.repository;

import br.edu.fatecsjc.lgnspringapi.entity.GroupEntity;
import br.edu.fatecsjc.lgnspringapi.entity.Member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    void deleteByGroup(GroupEntity group);

    List<Member> findByGroupId(Long groupId);
}