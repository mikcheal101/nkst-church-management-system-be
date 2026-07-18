package com.mikkytrionze.nkst.member.domain.repository;

import com.mikkytrionze.nkst.member.domain.model.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findAllByChurchId(Long churchId, Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.church.id = :churchId AND m.userType = 'CHURCH_ADMIN'")
    Page<Member> findAllByChurchIdAndUserTypeChurchAdmin(@Param("churchId") Long churchId, Pageable pageable);

    Optional<Member> findByChurchIdAndIsAdminTrue(Long churchId);
}
