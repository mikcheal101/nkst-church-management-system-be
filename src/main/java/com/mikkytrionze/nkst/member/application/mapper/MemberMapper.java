package com.mikkytrionze.nkst.member.application.mapper;

import com.mikkytrionze.nkst.member.application.dto.MemberDTO;
import com.mikkytrionze.nkst.member.domain.enums.Gender;
import com.mikkytrionze.nkst.member.domain.model.Member;

import java.util.Objects;

public class MemberMapper {
    public static MemberDTO toDTO(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .tel(member.getTel())
                .gender(member.getGender().name())
                .isBaptised(member.isBaptised())
                .lastName(member.getLastName())
                .middleName(member.getMiddleName())
                .firstName(member.getFirstName())
                .emailAddress(member.getEmailAddress())
                .baptismRecordDTO(BaptismRecordMapper.toDTO(member.getBaptismRecord()))
                .build();
    }

    public static Member toEntity(MemberDTO memberDTO) {
        if (Objects.isNull(memberDTO)) {
            return null;
        }

        Gender gender = Objects.isNull(memberDTO.getGender()) ? Gender.MALE : Gender.valueOf(memberDTO.getGender());
        return Member.builder()
                .tel(memberDTO.getTel())
                .gender(gender)
                .isBaptised(memberDTO.getIsBaptised())
                .lastName(memberDTO.getLastName())
                .middleName(memberDTO.getMiddleName())
                .firstName(memberDTO.getFirstName())
                .emailAddress(memberDTO.getEmailAddress())
                .baptismRecord(BaptismRecordMapper.toEntity(memberDTO.getBaptismRecordDTO()))
                .build();
    }
}
