package com.mikkytrionze.nkst.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BaptismRecordDTO {
    private Long id;
    private Integer serialNumber;
    private Instant dateOfBaptism;
    private String worshipCenter;
    private String bibleVerse;
    private String baptizedBy;
    private String remark;
    private String address;
    private String imageUri;
}
