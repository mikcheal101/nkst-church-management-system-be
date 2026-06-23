package com.mikkytrionze.nkst.member.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidateBaptismRecord
@Builder(toBuilder = true)
public class MemberRequest {

    @NotBlank(message = "Telephone number is required!")
    private String tel;

    @NotBlank(message = "Last Name is required!")
    private String lastName;

    private String middleName;

    @NotBlank(message = "First Name is required!")
    private String firstName;

    @NotBlank(message = "Gender is required!")
    private String gender;

    @NotBlank(message = "Email Address is required!")
    private String emailAddress;

    @NotNull(message = "Church ID is required!")
    private Long churchId;

    @Builder.Default
    private Boolean isBaptised = false;

    private Integer serialNumber;
    private String worshipCenter;
    private String bibleVerse;
    private String baptisedBy;
    private String remark;
    private String address;
    private String imageUri;
    private Instant dateOfBaptism;
}
