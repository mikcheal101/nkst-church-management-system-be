package com.mikkytrionze.nkst.pastor.application.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.mikkytrionze.nkst.church.application.dto.ChurchDTO;
import com.mikkytrionze.nkst.church.domain.model.Church;
import com.mikkytrionze.nkst.pastor.api.response.PastorResponse;
import com.mikkytrionze.nkst.pastor.application.dto.PastorDTO;
import com.mikkytrionze.nkst.pastor.domain.model.Pastor;
import com.mikkytrionze.nkst.pastor.domain.model.enums.PastorRole;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class PastorMapperTest {

    private final PastorMapper mapper = Mappers.getMapper(PastorMapper.class);

    @Test
    void shouldMapPastorToDTO() {
        Pastor pastor = Pastor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .middleName("M")
                .emailAddress("john@test.com")
                .pastorRole(PastorRole.LEAD)
                .build();

        PastorDTO dto = mapper.toDTO(pastor);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("M", dto.getMiddleName());
        assertEquals("john@test.com", dto.getEmailAddress());
    }

    @Test
    void shouldMapPastorToDTOWithNullRole() {
        Pastor pastor = Pastor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .tel("123")
                .pastorRole(null)
                .build();

        PastorDTO dto = mapper.toDTO(pastor);
        assertNotNull(dto);
        assertNull(dto.getPastorRole());
    }

    @Test
    void shouldMapPastorToResponse() {
        Church church = Church.builder()
                .id(1L)
                .name("Main Church")
                .build();

        Pastor pastor = Pastor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .emailAddress("john@test.com")
                .pastorRole(PastorRole.LEAD)
                .church(church)
                .build();

        PastorResponse response = mapper.toResponse(pastor);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertNotNull(response.getChurch());
        assertEquals("Main Church", response.getChurch().getName());

        ChurchDTO churchDto = response.getChurch();
        assertNotNull(churchDto.getPastors());
        assertTrue(churchDto.getPastors().isEmpty());
    }

    @Test
    void shouldMapPastorToResponseWithNullRole() {
        Pastor pastor = Pastor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .tel("123")
                .pastorRole(null)
                .build();

        PastorResponse response = mapper.toResponse(pastor);
        assertNotNull(response);
        assertNull(response.getPastorRole());
    }

    @Test
    void shouldMapPastorToResponseWithChurchAndPastors() {
        Pastor childPastor = new Pastor();
        childPastor.setId(2L);
        childPastor.setFirstName("Jane");
        childPastor.setLastName("Smith");
        childPastor.setTel("456");
        childPastor.setPastorRole(PastorRole.ASSOCIATE);

        Church church = Church.builder()
                .id(1L)
                .name("Main Church")
                .pastors(new java.util.LinkedHashSet<>(Set.of(childPastor)))
                .build();

        Pastor pastor = Pastor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pastorRole(PastorRole.LEAD)
                .church(church)
                .build();

        PastorResponse response = mapper.toResponse(pastor);
        assertNotNull(response.getChurch());
        assertNotNull(response.getChurch().getPastors());
        assertEquals(1, response.getChurch().getPastors().size());
    }

    @Test
    void shouldMapPastorToResponseWithChurchHavingNullPastors() {
        Church church = new Church();
        church.setId(1L);
        church.setName("Main Church");
        church.setPastors(null);

        Pastor pastor = Pastor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pastorRole(PastorRole.LEAD)
                .church(church)
                .build();

        PastorResponse response = mapper.toResponse(pastor);
        assertNotNull(response.getChurch());
        assertNull(response.getChurch().getPastors());
    }

    @Test
    void shouldMapDTOToEntityWithIgnoredId() {
        PastorDTO dto = PastorDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pastorRole("LEAD")
                .build();

        Pastor pastor = mapper.toEntity(dto);

        assertNotNull(pastor);
        assertNull(pastor.getId());
        assertEquals("John", pastor.getFirstName());
        assertEquals("Doe", pastor.getLastName());
        assertEquals(PastorRole.LEAD, pastor.getPastorRole());
    }

    @Test
    void shouldReturnNullWhenPastorIsNull() {
        assertNull(mapper.toDTO(null));
        assertNull(mapper.toResponse(null));
        assertNull(mapper.toEntity(null));
    }

    @Test
    void shouldHandlePastorRoleWithDefault() {
        Pastor pastor = Pastor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        PastorDTO dto = mapper.toDTO(pastor);
        assertEquals("ASSOCIATE", dto.getPastorRole());

        PastorResponse response = mapper.toResponse(pastor);
        assertEquals("ASSOCIATE", response.getPastorRole());

        PastorDTO dtoWithExplicitRole = PastorDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pastorRole("LEAD")
                .build();

        Pastor entity = mapper.toEntity(dtoWithExplicitRole);
        assertEquals(PastorRole.LEAD, entity.getPastorRole());
    }

    @Test
    void shouldHandleNullChurchInPastor() {
        Pastor pastor = Pastor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pastorRole(PastorRole.LEAD)
                .build();

        PastorResponse response = mapper.toResponse(pastor);
        assertNull(response.getChurch());
    }
}
