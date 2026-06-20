package com.mikkytrionze.nkst.member.domain.service;

import com.mikkytrionze.nkst.member.domain.model.Member;

public interface MemberService {
    void deleteMember(Long id);
    Member getById(Long id);
    Member saveMember(Member member);
    Member updateMember(Long id, Member member);
}
