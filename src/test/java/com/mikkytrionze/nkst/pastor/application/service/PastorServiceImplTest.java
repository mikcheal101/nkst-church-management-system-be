package com.mikkytrionze.nkst.pastor.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.mikkytrionze.nkst.church.domain.model.Church;
import com.mikkytrionze.nkst.church.domain.service.ChurchService;
import com.mikkytrionze.nkst.member.domain.model.Member;
import com.mikkytrionze.nkst.member.domain.service.MemberService;
import com.mikkytrionze.nkst.pastor.api.request.PastorRequest;
import com.mikkytrionze.nkst.pastor.api.response.PastorResponse;
import com.mikkytrionze.nkst.pastor.application.mapper.PastorMapper;
import com.mikkytrionze.nkst.pastor.domain.model.Pastor;
import com.mikkytrionze.nkst.pastor.domain.model.PastorRole;
import com.mikkytrionze.nkst.pastor.domain.repository.PastorRepository;
import java.util.List;
import java.util.Optional;

import com.mikkytrionze.nkst.pastor.domain.service.PastorRoleService;
import com.mikkytrionze.nkst.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class PastorServiceImplTest {

    @Mock
    private PastorRepository pastorRepository;

    @Mock
    private ChurchService churchService;

    @Mock
    private PastorRoleService pastorRoleService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private PastorServiceImpl pastorService;


    @Test
    void shouldGetPastorById() {
        Pastor pastor = Pastor.builder()
                .id(1L)
                .member(Member.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build();
        PastorResponse response = PastorResponse.builder().id(1L).firstName("John").lastName("Doe").build();

        when(pastorRepository.findById(1L)).thenReturn(Optional.of(pastor));
        when(PastorMapper.toResponse(pastor)).thenReturn(response);

        PastorResponse result = pastorService.getPastorById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    void shouldThrowExceptionWhenPastorNotFound() {
        when(pastorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pastorService.getPastorById(99L));
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> pastorService.getPastorById(null));
    }

    @Test
    void shouldGetAllPastors() {
        Pastor pastor = Pastor.builder()
                .id(1L)
                .member(Member.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build();
        PastorResponse response = PastorResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
        PageRequest pageable = PageRequest.of(0, 20);
        Page<Pastor> pastorPage = new PageImpl<>(List.of(pastor));

        when(pastorRepository.findAll(pageable)).thenReturn(pastorPage);
        when(PastorMapper.toResponse(pastor)).thenReturn(response);

        Page<PastorResponse> result = pastorService.getPastors(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldCreatePastor() {
        Church church = Church.builder().id(1L).name("Main Church").build();
        PastorRole pastorRole = PastorRole.builder().id(1L).name("Lead Pastor").build();

        PastorRequest request = PastorRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("M")
                .emailAddress("john@test.com")
                .tel("1234567890")
                .churchId(1L)
                .pastorRoleId(1L)
                .build();

        Pastor pastor = Pastor.builder()
                .member(Member.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .middleName("M")
                        .emailAddress("john@test.com")
                        .tel("1234567890")
                        .build())
                .church(church)
                .pastorRole(pastorRole)
                .build();

        PastorResponse response = PastorResponse.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        when(churchService.findChurchById(1L)).thenReturn(church);
        when(pastorRoleService.findPastorRole(1L)).thenReturn(pastorRole);
        when(pastorRepository.save(any(Pastor.class))).thenReturn(pastor);
        when(PastorMapper.toResponse(any(Pastor.class))).thenReturn(response);

        PastorResponse result = pastorService.createPastor(request);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void shouldUpdatePastor() {
        Church church = Church.builder().id(1L).name("Main Church").build();
        PastorRole pastorRole = PastorRole.builder().id(1L).name("Assistant Pastor").build();

        Long pastorId = 1L;
        PastorRequest request = PastorRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .middleName("A")
                .emailAddress("jane@test.com")
                .tel("9876543210")
                .churchId(1L)
                .pastorRoleId(1L)
                .build();

        PastorRole existingRole = PastorRole.builder().id(2L).name("Associate Pastor").build();
        Pastor existingPastor = Pastor.builder()
                .id(pastorId)
                .member(Member.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .church(church)
                .pastorRole(existingRole)
                .build();

        PastorResponse response = PastorResponse.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();

        when(pastorRepository.findById(pastorId)).thenReturn(Optional.of(existingPastor));
        when(churchService.findChurchById(1L)).thenReturn(church);
        when(pastorRoleService.findPastorRole(1L)).thenReturn(pastorRole);
        when(pastorRepository.save(any(Pastor.class))).thenReturn(existingPastor);
        when(PastorMapper.toResponse(any(Pastor.class))).thenReturn(response);

        PastorResponse result = pastorService.updatePastor(pastorId, request);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
    }

    @Test
    void shouldThrowExceptionOnUpdateWhenIdIsNull() {
        PastorRequest request = PastorRequest.builder().firstName("John").build();

        assertThrows(IllegalArgumentException.class, () -> pastorService.updatePastor(null, request));
    }

    @Test
    void shouldSoftDeletePastor() {
        Long pastorId = 1L;
        Pastor pastor = Pastor.builder()
                .id(pastorId)
                .member(Member.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .build())
                .build();

        when(pastorRepository.findById(pastorId)).thenReturn(Optional.of(pastor));
        when(pastorRepository.save(any(Pastor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        pastorService.deletePastorById(pastorId);

        assertTrue(pastor.isDeleted());
        verify(pastorRepository).save(pastor);
    }

    @Test
    void shouldThrowExceptionOnDeleteWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> pastorService.deletePastorById(null));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPastor() {
        when(pastorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> pastorService.updatePastor(99L, PastorRequest.builder().build()));
    }
}
