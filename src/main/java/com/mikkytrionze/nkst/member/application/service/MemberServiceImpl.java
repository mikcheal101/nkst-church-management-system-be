package com.mikkytrionze.nkst.member.application.service;

import com.mikkytrionze.member.application.dto.MemberCreatedEvent;
import com.mikkytrionze.nkst.church.domain.model.Church;
import com.mikkytrionze.nkst.church.domain.repository.ChurchRepository;
import com.mikkytrionze.nkst.member.api.request.MemberRequest;
import com.mikkytrionze.nkst.member.api.response.MemberResponse;
import com.mikkytrionze.nkst.member.application.mapper.MemberMapper;
import com.mikkytrionze.nkst.member.domain.enums.Gender;
import com.mikkytrionze.nkst.member.domain.gateway.MemberEventPublisher;
import com.mikkytrionze.nkst.member.domain.model.BaptismRecord;
import com.mikkytrionze.nkst.member.domain.model.Member;
import com.mikkytrionze.nkst.member.domain.repository.MemberRepository;
import com.mikkytrionze.nkst.member.domain.service.BaptismRecordService;
import com.mikkytrionze.nkst.member.domain.service.MemberService;
import com.mikkytrionze.nkst.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BaptismRecordService baptismRecordService;
    private final ChurchRepository churchRepository;
    private final MemberEventPublisher memberEventPublisher;

    @Override
    public MemberResponse getById(Long id) {
        log.info("Getting Member by Id: {}", id);
        
        Member member = findById(id, null);
        
        return MemberMapper.toResponse(member);
    }

    @Override
    public Member findMemberById(Long id) {
        log.info("Finding Member by Id: {}", id);

        return findById(id, null);
    }

    @Override
    public void deleteMember(Long id) {
        log.info("Deleting Member with id: {}", id);

        Member member = findById(id, null);

        member.setDeleted(true);

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public MemberResponse saveMember(MemberRequest memberRequest) {
        Church church = churchRepository.findById(memberRequest.getChurchId())
                .orElseThrow(() -> new ResourceNotFoundException("Church", memberRequest.getChurchId()));

        Member member = Member.builder()
                .tel(memberRequest.getTel())
                .firstName(memberRequest.getFirstName())
                .middleName(memberRequest.getMiddleName())
                .lastName(memberRequest.getLastName())
                .emailAddress(memberRequest.getEmailAddress())
                .gender(Gender.valueOf(memberRequest.getGender().toUpperCase()))
                .isBaptised(memberRequest.getIsBaptised())
                .address(memberRequest.getAddress())
                .church(church)
                .build();

        BaptismRecord baptismRecord = null;
        if (Boolean.TRUE.equals(memberRequest.getIsBaptised())) {
            baptismRecord = BaptismRecord.builder()
                            .dateOfBaptism(memberRequest.getDateOfBaptism())
                            .bibleVerse(memberRequest.getBibleVerse())
                            .serialNumber(memberRequest.getSerialNumber())
                            .baptizedBy(memberRequest.getBaptisedBy())
                            .worshipCenter(memberRequest.getWorshipCenter())
                            .remark(memberRequest.getRemark())
                            .build();
            member.setBaptismRecord(baptismRecordService.save(baptismRecord));
        }

        Member savedMember = memberRepository.save(member);

        // publish member to kafka
        MemberCreatedEvent memberCreatedEvent = MemberCreatedEvent.builder()
                .memberId(savedMember.getId())
                .username(savedMember.getTel().trim().isBlank() ?
                        savedMember.getEmailAddress().trim().toLowerCase() : savedMember.getTel().trim())
                .password(null)
                .roles(List.of(Boolean.TRUE.equals(memberRequest.getIsBaptised()) ? "MEMBER_BAPTISED" : "MEMBER_UNBAPTISED"))
                .build();
        memberEventPublisher.publishMemberCreated(memberCreatedEvent);

        log.info("Saved Member with id: {}", member.getId());

        return MemberMapper.toResponse(savedMember);
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long id, MemberRequest memberRequest) {
        Member savedMember = findById(id, null);

        savedMember.setBaptised(memberRequest.getIsBaptised());
        savedMember.setGender(Gender.valueOf(memberRequest.getGender()));
        savedMember.setTel(memberRequest.getTel());
        savedMember.setFirstName(memberRequest.getFirstName());
        savedMember.setEmailAddress(memberRequest.getEmailAddress());
        savedMember.setLastName(memberRequest.getLastName());
        savedMember.setMiddleName(memberRequest.getMiddleName());
        savedMember.setAddress(memberRequest.getAddress());

        if (memberRequest.getChurchId() != null) {
            Church church = churchRepository.findById(memberRequest.getChurchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Church", memberRequest.getChurchId()));
            savedMember.setChurch(church);
        }

        if (Boolean.TRUE.equals(memberRequest.getIsBaptised())) {
            BaptismRecord baptismRecord = getBaptismRecord(memberRequest, savedMember.getBaptismRecord());
            savedMember.setBaptismRecord(baptismRecordService.update(baptismRecord.getId(), baptismRecord));
        }
        
        Member updatedMember = memberRepository.save(savedMember);
        
        return MemberMapper.toResponse(updatedMember);
    }

    @Override
    public Page<MemberResponse> getAllMembers(Pageable pageable) {
        log.info("Fetching all members based on: {}", pageable);

        Page<Member> members = memberRepository.findAll(pageable);

        return members.map(MemberMapper::toResponse);
    }

    @Override
    public Page<MemberResponse> getAllChurchMembers(Long churchId, Pageable pageable) {
        log.info("Fetching all members of a church with id: {}, page: {}", churchId, pageable);

        Page<Member> churchMembers = memberRepository.findAllByChurchId(churchId, pageable);

        return churchMembers.map(MemberMapper::toResponse);
    }

    @Override
    public MemberResponse getChurchMember(Long churchId, Long memberId) {
        log.info("Getting church member with id: {} from church with id: {}", memberId, churchId);

        Member member = findById(memberId, churchId);

        return MemberMapper.toResponse(member);
    }

    @Override
    public Page<MemberResponse> fetchProspectiveAdmins(Long churchId, Pageable pageable) {
        log.info("Fetching prospective admins for church id: {}", churchId);

        Page<Member> churchAdmins = this.memberRepository.findAllByChurchIdAndUserTypeChurchAdmin(churchId, pageable);

        return churchAdmins.map(MemberMapper::toResponse);
    }

    private static @NonNull BaptismRecord getBaptismRecord(MemberRequest memberRequest, BaptismRecord baptismRecord) {
        baptismRecord.setWorshipCenter(memberRequest.getWorshipCenter());
        baptismRecord.setDateOfBaptism(memberRequest.getDateOfBaptism());
        baptismRecord.setSerialNumber(memberRequest.getSerialNumber());
        baptismRecord.setBibleVerse(memberRequest.getBibleVerse());
        baptismRecord.setBaptizedBy(memberRequest.getBaptisedBy());
        baptismRecord.setRemark(memberRequest.getRemark());
        return baptismRecord;
    }

    private Member findById(Long id, Long churchId) throws IllegalArgumentException, ResourceNotFoundException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id is required!");
        }

        Member member = memberRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Member", id));

        if (churchId != null && churchId > 0 && !churchId.equals(member.getChurch().getId())) {
            throw new ResourceNotFoundException("Member", id);
        }

        return member;
    }
}
