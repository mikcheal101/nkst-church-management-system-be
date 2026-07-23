package com.mikkytrionze.nkst.member.domain.repository;

import com.mikkytrionze.nkst.member.domain.model.Member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findAllByChurchId(Long churchId, Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.church.id = :churchId AND m.userType = 'CHURCH_ADMIN'")
    Page<Member> findAllByChurchIdAndUserTypeChurchAdmin(@Param("churchId") Long churchId, Pageable pageable);

    Optional<Member> findByChurchIdAndIsAdminTrue(Long churchId);

    @Query("SELECT m.church.id FROM Member m WHERE m.tel = :identifier OR m.emailAddress = :identifier")
    Optional<Long> findChurchIdByIdentifier(@Param("identifier") String identifier);


    @Query("SELECT m FROM Member m WHERE m.church.id = :churchId AND " +
        "(" +
            "LOWER(m.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.middleName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.tel) LIKE LOWER(CONCAT('%', :query, '%'))" +
        ")")
    Page<Member> findByChurchIdAndName(@Param("churchId") Long churchId,
                                       @Param("query") String query,
                                       Pageable pageable);

    @Query("SELECT m FROM Member m "+
        "WHERE m.church.id = :churchId "+ 
        "AND ( "+
            "LOWER(m.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.middleName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.tel) LIKE LOWER(CONCAT('%', :query, '%'))" +
        ") AND NOT EXISTS ( "+
            "SELECT e FROM Elder e  "+
            "WHERE e.member.id = m.id AND e.church.id = :churchId "+
        ")")
    Page<Member> searchEligibleMembersForElder(
        @Param("churchId") Long churchId, 
        @Param("query") String query, 
        Pageable pageable
    );
}
