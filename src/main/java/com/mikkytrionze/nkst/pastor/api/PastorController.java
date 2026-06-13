package com.mikkytrionze.nkst.pastor.api;

import com.mikkytrionze.nkst.pastor.api.request.PastorRequest;
import com.mikkytrionze.nkst.pastor.api.response.PastorResponse;
import com.mikkytrionze.nkst.pastor.domain.service.PastorService;
import com.mikkytrionze.nkst.shared.util.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pastors")
public class PastorController {

    private final PastorService pastorService;

    @GetMapping
    public ResponseEntity<Page<PastorResponse>> getPastors(
            @PageableDefault(page = Constants.INIT_PAGE, size = Constants.PAGE_SIZE) Pageable pageable) {
        Page<PastorResponse> pastors = pastorService.getPastors(pageable);
        return ResponseEntity.ok(pastors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PastorResponse> getPastor(@PathVariable Long id) {
        PastorResponse pastorResponse = pastorService.getPastorById(id);
        return ResponseEntity.ok(pastorResponse);
    }

    @PostMapping
    public ResponseEntity<PastorResponse> createPastor(@RequestBody @Valid PastorRequest pastorRequest) {
        PastorResponse pastorResponse = pastorService.createPastor(pastorRequest);
        return ResponseEntity.ok(pastorResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PastorResponse> updatePastor(
            @PathVariable Long id,
            @RequestBody @Valid PastorRequest pastorRequest) {
        PastorResponse pastorResponse = pastorService.updatePastor(id, pastorRequest);
        return ResponseEntity.ok(pastorResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePastor(@PathVariable Long id) {
        this.pastorService.deletePastorById(id);
        return ResponseEntity.noContent().build();
    }
}
