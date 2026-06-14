package com.mikkytrionze.nkst.pastor.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PastorDTO {
    private Long id;
    private String lastName;
    private String middleName;
    private String firstName;
    private String emailAddress;
    private String pastorRole;
}
