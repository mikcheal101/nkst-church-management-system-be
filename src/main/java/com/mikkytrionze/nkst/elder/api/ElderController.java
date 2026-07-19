package com.mikkytrionze.nkst.elder.api;

import com.mikkytrionze.nkst.elder.api.request.MakeElderRequest;
import com.mikkytrionze.nkst.elder.domain.service.ElderService;
import com.mikkytrionze.nkst.member.api.response.MemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/elders")
public class ElderController {

    private final ElderService elderService;

    @GetMapping
    @PreAuthorize("hasAuthority('can_view_elders')")
    public ResponseEntity<Page<MemberResponse>> elders(Pageable pageable) {
        Page<MemberResponse> response = this.elderService.getElders(pageable);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('can_read_elders')")
    public ResponseEntity<MemberResponse> elder(@PathVariable Long id) {
        MemberResponse response = this.elderService.getElder(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('can_create_elders') OR hasAuthority('can_update_elders')")
    public ResponseEntity<Boolean> makeAnElder(@Valid MakeElderRequest makeElderRequest) {
        Boolean response = elderService.makeElder(makeElderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
