package com.doftec.SpeakWise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<String> getStatus(){
        return ResponseEntity.ok("All Okay :)");
    }
}
