package com.mikkytrionze.nkst.pastor.application.mapper;

import com.mikkytrionze.nkst.pastor.api.response.PastorRoleResponse;
import com.mikkytrionze.nkst.pastor.application.dto.PastorRoleDTO;
import com.mikkytrionze.nkst.pastor.domain.model.PastorRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PastorRoleMapper {
    PastorRoleDTO toDTO(PastorRole pastorRole);

    PastorRoleResponse toResponse(PastorRole pastorRole);

    @Mapping(target = "id", ignore = true)
    PastorRole toEntity(PastorRoleDTO pastorRoleDTO);
}
