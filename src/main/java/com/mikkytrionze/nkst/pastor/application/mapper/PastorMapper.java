package com.mikkytrionze.nkst.pastor.application.mapper;

import com.mikkytrionze.nkst.pastor.api.response.PastorResponse;
import com.mikkytrionze.nkst.pastor.application.dto.PastorDTO;
import com.mikkytrionze.nkst.pastor.domain.model.Pastor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PastorMapper {
    PastorDTO toDTO(Pastor pastor);

    PastorResponse toResponse(Pastor pastor);

    @Mapping(target = "id", ignore = true)
    Pastor toEntity(PastorDTO pastorDTO);
}
