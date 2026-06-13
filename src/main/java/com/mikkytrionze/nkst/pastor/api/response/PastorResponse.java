package com.mikkytrionze.nkst.pastor.api.response;

import com.mikkytrionze.nkst.church.application.dto.ChurchDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class PastorResponse {
    private Long id;
    private String lastName;
    private String middleName;
    private String firstName;
    private String emailAddress;
    private String pastorRole;
    private ChurchDTO church;
}
