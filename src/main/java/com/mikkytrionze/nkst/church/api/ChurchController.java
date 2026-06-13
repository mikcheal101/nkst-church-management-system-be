package com.mikkytrionze.nkst.church.api;

import com.mikkytrionze.nkst.church.api.request.ChurchRequest;
import com.mikkytrionze.nkst.church.api.response.ChurchResponse;
import com.mikkytrionze.nkst.church.domain.service.ChurchService;
import com.mikkytrionze.nkst.shared.util.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/churches")
public class ChurchController {

    private final ChurchService churchService;

    @GetMapping
    public ResponseEntity<Page<ChurchResponse>> getChurches(
            @PageableDefault(page = Constants.INIT_PAGE, size = Constants.PAGE_SIZE) Pageable pageable) {
        Page<ChurchResponse> churches = this.churchService.getChurches(pageable);
        return ResponseEntity.ok().body(churches);
    }

    @PostMapping
    public ResponseEntity<ChurchResponse> createChurch(@RequestBody @Valid ChurchRequest churchRequest) {
        ChurchResponse churchResponse = this.churchService.createChurch(churchRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(churchResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChurchResponse> updateChurch(
            @PathVariable Long id,
            @RequestBody @Valid ChurchRequest churchRequest) {
        ChurchResponse churchResponse = this.churchService.updateChurch(id, churchRequest);
        return ResponseEntity.ok().body(churchResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChurchResponse> getChurch(@PathVariable Long id) {
        ChurchResponse churchResponse = this.churchService.getChurch(id);
        return ResponseEntity.ok().body(churchResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChurch(@PathVariable Long id) {
        this.churchService.deleteChurch(id);
        return ResponseEntity.noContent().build();
    }
}
