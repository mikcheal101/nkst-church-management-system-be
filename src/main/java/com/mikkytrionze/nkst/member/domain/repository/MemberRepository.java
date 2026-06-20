package com.mikkytrionze.nkst.member.domain.repository;

import com.mikkytrionze.nkst.member.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
