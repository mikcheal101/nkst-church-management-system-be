package com.mikkytrionze.nkst.elder.domain.repository;

import com.mikkytrionze.nkst.elder.domain.model.Elder;
import com.mikkytrionze.nkst.member.domain.model.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ElderRepository extends JpaRepository<Elder, Long> {

    @Query("SELECT e.member FROM Elder e WHERE e.church.id = :id")
    Page<Member> findAllByChurchId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT e.member FROM Elder e WHERE e.church.id = :id AND e.id = :id")
    Optional<Member> findByChurchId(@Param("churchId") Long churchId, @Param("id") Long id);
}
