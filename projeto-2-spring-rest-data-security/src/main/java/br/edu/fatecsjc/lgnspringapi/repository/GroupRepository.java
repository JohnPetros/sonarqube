package br.edu.fatecsjc.lgnspringapi.repository;

import br.edu.fatecsjc.lgnspringapi.entity.GroupEntity;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE GroupEntity g SET g.organization = null WHERE g.organization.id = :organizationId")
    void nullifyOrganizationId(@Param("organizationId") Long organizationId);

    List<GroupEntity> findByOrganizationId(Long organizationId);
}