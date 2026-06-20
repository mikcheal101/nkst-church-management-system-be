package com.mikkytrionze.nkst.pastor.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PastorRequest {

    private Long churchId;

    private Long pastorRoleId;

    // -> Member
    @NotBlank(message = "Telephone / Mobile number is required!")
    private String tel;

    @NotBlank(message = "Lastname is required!")
    private String lastName;

    @NotBlank(message = "Firstname is required!")
    private String firstName;

    @NotBlank(message = "Gender is required!")
    private String gender;

    private String middleName;

    private String emailAddress;

    // -> Baptism Record
    @NotNull(message = "Serial Number is required!")
    private Integer serialNumber;

    @NotNull(message = "Date of baptism is required!")
    private Instant dateOfBaptism;

    @NotBlank(message = "Worship center is required!")
    private String worshipCenter;

    @NotBlank(message = "Bible verse is required!")
    private String bibleVerse;

    @NotBlank(message = "Baptised By is required!")
    private String baptisedBy;

    private String remark;

    private String address;

    private String imageUri;
}
