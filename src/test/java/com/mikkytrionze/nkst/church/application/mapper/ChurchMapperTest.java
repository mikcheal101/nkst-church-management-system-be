package com.mikkytrionze.nkst.church.application.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.mikkytrionze.nkst.church.application.dto.ChurchDTO;
import com.mikkytrionze.nkst.church.api.response.ChurchResponse;
import com.mikkytrionze.nkst.church.domain.model.Church;
import com.mikkytrionze.nkst.pastor.application.dto.PastorDTO;
import com.mikkytrionze.nkst.pastor.application.dto.PastorRoleDTO;
import com.mikkytrionze.nkst.pastor.domain.model.Pastor;
import com.mikkytrionze.nkst.pastor.domain.model.PastorRole;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ChurchMapperTest {

    private final ChurchMapper mapper = Mappers.getMapper(ChurchMapper.class);

    @Test
    void shouldMapChurchToDTO() {
        Church church = Church.builder()
                .id(1L)
                .name("Main Church")
                .address("123 Main St")
                .build();

        ChurchDTO dto = mapper.toDTO(church);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Main Church", dto.getName());
        assertEquals("123 Main St", dto.getAddress());
    }

    @Test
    void shouldMapChurchToResponse() {
        Church church = Church.builder()
                .id(1L)
                .name("Main Church")
                .address("123 Main St")
                .build();

        ChurchResponse response = mapper.toResponse(church);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Main Church", response.getName());
        assertEquals("123 Main St", response.getAddress());
    }

    @Test
    void shouldMapDTOToEntityWithIgnoredId() {
        ChurchDTO dto = ChurchDTO.builder()
                .id(1L)
                .name("Main Church")
                .address("123 Main St")
                .build();

        Church church = mapper.toEntity(dto);

        assertNotNull(church);
        assertNull(church.getId());
        assertEquals("Main Church", church.getName());
        assertEquals("123 Main St", church.getAddress());
    }

    @Test
    void shouldReturnNullWhenChurchIsNull() {
        assertNull(mapper.toDTO(null));
        assertNull(mapper.toResponse(null));
        assertNull(mapper.toEntity(null));
    }

    @Test
    void shouldMapChurchWithParentAndPastors() {
        Church parent = Church.builder()
                .id(2L)
                .name("Parent Church")
                .build();

        PastorRole pastorRole = PastorRole.builder().name("Lead Pastor").build();
        Pastor pastor = Pastor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pastorRole(pastorRole)
                .build();

        Church church = Church.builder()
                .id(1L)
                .name("Main Church")
                .address("123 Main St")
                .parentChurch(parent)
                .pastors(new java.util.LinkedHashSet<>(List.of(pastor)))
                .build();

        ChurchDTO dto = mapper.toDTO(church);
        assertNotNull(dto);
        assertNotNull(dto.getParentChurch());
        assertEquals("Parent Church", dto.getParentChurch().getName());
        assertNotNull(dto.getPastors());
        assertEquals(1, dto.getPastors().size());

        ChurchResponse response = mapper.toResponse(church);
        assertNotNull(response);
        assertNotNull(response.getParentChurch());
        assertNotNull(response.getPastors());
    }

    @Test
    void shouldMapDTOToEntityWithNestedObjects() {
        PastorDTO pastorDTO = PastorDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pastorRoleDTO(PastorRoleDTO.builder().name("Lead Pastor").build())
                .build();

        ChurchDTO parentDTO = ChurchDTO.builder()
                .id(2L)
                .name("Parent Church")
                .build();

        ChurchDTO churchDTO = ChurchDTO.builder()
                .id(1L)
                .name("Main Church")
                .address("123 Main St")
                .parentChurch(parentDTO)
                .pastors(List.of(pastorDTO))
                .build();

        Church church = mapper.toEntity(churchDTO);

        assertNotNull(church);
        assertNull(church.getId());
        assertEquals("Main Church", church.getName());
        assertNotNull(church.getParentChurch());
        assertNull(church.getParentChurch().getId());
        assertFalse(church.getPastors().isEmpty());
    }

    @Test
    void shouldMapEmptyPastorsSet() {
        Church church = Church.builder()
                .id(1L)
                .name("Church")
                .pastors(new java.util.LinkedHashSet<>())
                .build();

        ChurchDTO dto = mapper.toDTO(church);
        assertNotNull(dto.getPastors());
        assertTrue(dto.getPastors().isEmpty());
    }

    @Test
    void shouldMapPastorWithNullRole() {
        Pastor pastor = new Pastor();
        pastor.setId(1L);
        pastor.setFirstName("John");
        pastor.setLastName("Doe");
        pastor.setTel("123");
        pastor.setPastorRole(null);

        Church church = Church.builder()
                .id(1L)
                .name("Church")
                .pastors(new java.util.LinkedHashSet<>(List.of(pastor)))
                .build();

        ChurchDTO dto = mapper.toDTO(church);
        assertNotNull(dto.getPastors());
        assertEquals(1, dto.getPastors().size());
        assertNull(dto.getPastors().getFirst().getPastorRoleDTO());
    }

    @Test
    void shouldMapPastorDTOWithNullRoleToEntity() {
        PastorDTO pastorDTO = PastorDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        ChurchDTO churchDTO = ChurchDTO.builder()
                .id(1L)
                .name("Church")
                .pastors(List.of(pastorDTO))
                .build();

        Church church = mapper.toEntity(churchDTO);
        assertNotNull(church);
        assertFalse(church.getPastors().isEmpty());
        assertNull(church.getPastors().iterator().next().getPastorRole());
    }

    @Test
    void shouldHandleNullPastorsSet() {
        Church church = new Church();
        church.setId(1L);
        church.setName("Church");
        church.setPastors(null);

        ChurchDTO dto = mapper.toDTO(church);
        assertNotNull(dto);
        assertNull(dto.getPastors());
    }

    @Test
    void shouldHandleNullPastorsListInDTO() {
        ChurchDTO churchDTO = ChurchDTO.builder()
                .id(1L)
                .name("Church")
                .build();

        Church church = mapper.toEntity(churchDTO);
        assertNotNull(church);
        assertTrue(church.getPastors() == null || church.getPastors().isEmpty());
    }
}
