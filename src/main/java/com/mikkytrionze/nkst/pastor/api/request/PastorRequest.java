package com.mikkytrionze.nkst.pastor.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PastorRequest {
    @NotBlank(message = "Telephone / Mobile number is required!")
    private String tel;

    @NotBlank(message = "Lastname is required!")
    private String lastName;

    @NotBlank(message = "Firstname is required!")
    private String firstName;

    private String middleName;

    private String emailAddress;

    private Long churchId;

    private String pastorRole;
}
