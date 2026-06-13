package com.mikkytrionze.nkst.church.application.dto;

import com.mikkytrionze.nkst.pastor.application.dto.PastorDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class ChurchDTO {
    private Long id;
    private String name;
    private String address;
    private ChurchDTO parentChurch;
    private List<PastorDTO> pastors;
}
