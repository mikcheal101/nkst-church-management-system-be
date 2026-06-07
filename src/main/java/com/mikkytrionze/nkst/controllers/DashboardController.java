package com.mikkytrionze.nkst.controllers;

import com.mikkytrionze.nkst.application.dto.response.DashboardResponse;
import com.mikkytrionze.nkst.services.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/counters")
    public ResponseEntity<DashboardResponse> getCounters() {
        DashboardResponse response = dashboardService.getCounters();
        return ResponseEntity.ok().body(response);
    }
}
