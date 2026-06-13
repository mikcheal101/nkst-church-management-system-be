package com.mikkytrionze.nkst.church.application.mapper;

import com.mikkytrionze.nkst.church.application.dto.ChurchDTO;
import com.mikkytrionze.nkst.church.api.response.ChurchResponse;
import com.mikkytrionze.nkst.church.domain.model.Church;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChurchMapper {

    ChurchDTO toDTO(Church church);

    ChurchResponse toResponse(Church church);

    @Mapping(target = "id", ignore = true)
    Church toEntity(ChurchDTO churchResponse);
}
