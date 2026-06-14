package com.mikkytrionze.nkst.church.application.dto;

import com.mikkytrionze.nkst.pastor.application.dto.PastorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChurchDTO {
    private Long id;
    private String name;
    private String address;
    private ChurchDTO parentChurch;
    private List<PastorDTO> pastors;
}
