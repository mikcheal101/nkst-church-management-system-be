package com.mikkytrionze.nkst.member.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.mikkytrionze.nkst.member.domain.enums.Gender;
import com.mikkytrionze.nkst.member.domain.model.BaptismRecord;
import com.mikkytrionze.nkst.member.domain.model.Member;
import com.mikkytrionze.nkst.member.domain.repository.MemberRepository;
import com.mikkytrionze.nkst.member.domain.service.BaptismRecordService;
import com.mikkytrionze.nkst.shared.exception.ResourceNotFoundException;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BaptismRecordService baptismRecordService;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void shouldGetMemberById() {
        Member member = Member.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .tel("1234567890")
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Member result = memberService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMemberNotFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.getById(99L));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> memberService.getById(null));
    }

    @Test
    void shouldSaveMemberWithoutBaptismRecord() {
        Member member = Member.builder()
                .firstName("John")
                .lastName("Doe")
                .tel("1234567890")
                .build();

        Member savedMember = Member.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .tel("1234567890")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        Member result = memberService.saveMember(member);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(baptismRecordService, never()).save(any());
    }

    @Test
    void shouldSaveMemberWithBaptismRecord() {
        BaptismRecord baptismRecord = BaptismRecord.builder()
                .serialNumber(100)
                .dateOfBaptism(Instant.now())
                .worshipCenter("Main Church")
                .bibleVerse("John 3:16")
                .baptizedBy("Pastor Mike")
                .build();

        Member member = Member.builder()
                .firstName("John")
                .lastName("Doe")
                .tel("1234567890")
                .isBaptised(true)
                .baptismRecord(baptismRecord)
                .build();

        BaptismRecord savedBaptismRecord = BaptismRecord.builder()
                .id(1L)
                .serialNumber(100)
                .dateOfBaptism(Instant.now())
                .worshipCenter("Main Church")
                .bibleVerse("John 3:16")
                .baptizedBy("Pastor Mike")
                .build();

        Member savedMember = Member.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .tel("1234567890")
                .isBaptised(true)
                .baptismRecord(savedBaptismRecord)
                .build();

        when(baptismRecordService.save(any(BaptismRecord.class))).thenReturn(savedBaptismRecord);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        Member result = memberService.saveMember(member);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(baptismRecordService).save(any(BaptismRecord.class));
    }

    @Test
    void shouldUpdateMemberWithoutBaptismRecord() {
        Long memberId = 1L;
        Member existingMember = Member.builder()
                .id(memberId)
                .firstName("John")
                .lastName("Doe")
                .tel("1234567890")
                .gender(Gender.MALE)
                .build();

        Member updateMember = Member.builder()
                .firstName("Jane")
                .lastName("Smith")
                .tel("0987654321")
                .gender(Gender.FEMALE)
                .emailAddress("jane@test.com")
                .build();

        Member updatedMember = Member.builder()
                .id(memberId)
                .firstName("Jane")
                .lastName("Smith")
                .tel("0987654321")
                .gender(Gender.FEMALE)
                .emailAddress("jane@test.com")
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(any(Member.class))).thenReturn(updatedMember);

        Member result = memberService.updateMember(memberId, updateMember);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        verify(baptismRecordService, never()).update(anyLong(), any());
        verify(baptismRecordService, never()).save(any());
    }

    @Test
    void shouldUpdateMemberWithExistingBaptismRecord() {
        Long memberId = 1L;
        Long baptismRecordId = 2L;
        BaptismRecord existingBaptismRecord = BaptismRecord.builder()
                .id(baptismRecordId)
                .build();

        Member existingMember = Member.builder()
                .id(memberId)
                .firstName("John")
                .lastName("Doe")
                .tel("1234567890")
                .baptismRecord(existingBaptismRecord)
                .build();

        BaptismRecord newBaptismRecord = BaptismRecord.builder()
                .serialNumber(200)
                .dateOfBaptism(Instant.now())
                .worshipCenter("New Church")
                .bibleVerse("Matthew 28:19")
                .baptizedBy("Pastor Jane")
                .build();

        Member updateMember = Member.builder()
                .firstName("Jane")
                .lastName("Smith")
                .tel("0987654321")
                .baptismRecord(newBaptismRecord)
                .build();

        Member updatedMember = Member.builder()
                .id(memberId)
                .firstName("Jane")
                .lastName("Smith")
                .tel("0987654321")
                .baptismRecord(existingBaptismRecord)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));
        when(baptismRecordService.update(eq(baptismRecordId), any(BaptismRecord.class))).thenReturn(existingBaptismRecord);
        when(memberRepository.save(any(Member.class))).thenReturn(updatedMember);

        Member result = memberService.updateMember(memberId, updateMember);

        assertNotNull(result);
        verify(baptismRecordService).update(eq(baptismRecordId), any(BaptismRecord.class));
    }

    @Test
    void shouldUpdateMemberAddingNewBaptismRecord() {
        Long memberId = 1L;
        Member existingMember = Member.builder()
                .id(memberId)
                .firstName("John")
                .lastName("Doe")
                .tel("1234567890")
                .build();

        BaptismRecord newBaptismRecord = BaptismRecord.builder()
                .serialNumber(200)
                .dateOfBaptism(Instant.now())
                .worshipCenter("New Church")
                .bibleVerse("Matthew 28:19")
                .baptizedBy("Pastor Jane")
                .build();

        Member updateMember = Member.builder()
                .firstName("Jane")
                .lastName("Smith")
                .tel("0987654321")
                .isBaptised(true)
                .baptismRecord(newBaptismRecord)
                .build();

        BaptismRecord savedBaptismRecord = BaptismRecord.builder()
                .id(2L)
                .serialNumber(200)
                .dateOfBaptism(Instant.now())
                .worshipCenter("New Church")
                .bibleVerse("Matthew 28:19")
                .baptizedBy("Pastor Jane")
                .build();

        Member updatedMember = Member.builder()
                .id(memberId)
                .firstName("Jane")
                .lastName("Smith")
                .tel("0987654321")
                .isBaptised(true)
                .baptismRecord(savedBaptismRecord)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));
        when(baptismRecordService.save(any(BaptismRecord.class))).thenReturn(savedBaptismRecord);
        when(memberRepository.save(any(Member.class))).thenReturn(updatedMember);

        Member result = memberService.updateMember(memberId, updateMember);

        assertNotNull(result);
        verify(baptismRecordService).save(any(BaptismRecord.class));
    }

    @Test
    void shouldSoftDeleteMember() {
        Long memberId = 1L;
        Member member = Member.builder()
                .id(memberId)
                .firstName("John")
                .lastName("Doe")
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        memberService.deleteMember(memberId);

        assertTrue(member.isDeleted());
        verify(memberRepository).save(member);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistingMember() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.deleteMember(99L));
    }
}
