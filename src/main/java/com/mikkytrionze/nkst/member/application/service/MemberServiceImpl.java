package com.mikkytrionze.nkst.member.application.service;

import com.mikkytrionze.nkst.member.domain.model.BaptismRecord;
import com.mikkytrionze.nkst.member.domain.model.Member;
import com.mikkytrionze.nkst.member.domain.repository.MemberRepository;
import com.mikkytrionze.nkst.member.domain.service.BaptismRecordService;
import com.mikkytrionze.nkst.member.domain.service.MemberService;
import com.mikkytrionze.nkst.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BaptismRecordService baptismRecordService;

    @Override
    public Member getById(Long id) {
        log.info("Getting Member by Id: {}", id);

        return findById(id);
    }

    @Override
    public void deleteMember(Long id) {
        log.info("Deleting Member with id: {}", id);

        Member member = findById(id);

        member.setDeleted(true);

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member saveMember(Member member) {

        if (member.isBaptised() && member.getBaptismRecord() != null) {
            member.setBaptismRecord(baptismRecordService.save(member.getBaptismRecord()));
        }

        Member savedMember = memberRepository.save(member);

        log.info("Saved Member with id: {}", member.getId());

        return savedMember;
    }

    @Override
    @Transactional
    public Member updateMember(Long id, Member member) {
        Member savedMember = findById(id);

        savedMember.setBaptised(member.isBaptised());
        savedMember.setGender(member.getGender());
        savedMember.setTel(member.getTel());
        savedMember.setFirstName(member.getFirstName());
        savedMember.setEmailAddress(member.getEmailAddress());
        savedMember.setLastName(member.getLastName());
        savedMember.setMiddleName(member.getMiddleName());

        baptismRecordService.update(savedMember.getBaptismRecord().getId(), member.getBaptismRecord());
        return memberRepository.save(savedMember);
    }

    private Member findById(Long id) throws IllegalArgumentException, ResourceNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("Id is required!");
        }
        return memberRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Member", id));
    }
}
