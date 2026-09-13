package br.edu.fatecsjc.lgnspringapi.repository;

import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.entity.Marathon;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarathonRepository extends JpaRepository<Marathon, Long> {
    void deleteByMember(Member member);

    List<Marathon> findByMemberId(Long memberId);
}