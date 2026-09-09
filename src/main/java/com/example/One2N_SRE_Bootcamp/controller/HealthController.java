package com.example.One2N_SRE_Bootcamp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    // Deliberately NOT under /api/v1 — health checks are infrastructure-level,
    // used by load balancers/orchestrators, and conventionally sit at the root.
    @GetMapping("/healthcheck")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}