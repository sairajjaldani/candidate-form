package com.basic.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basic.dto.ApiResponse;
import com.basic.dto.CandidateRequestDTO;
import com.basic.dto.CandidateResponseDTO;
import com.basic.service.CandidateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // React can call this API
public class CandidateController {

    private final CandidateService candidateService;

    // ─── POST /api/candidates ──────────────────────────────
    // React form submits here
    @PostMapping
    public ResponseEntity<ApiResponse<CandidateResponseDTO>> submitCandidate(
            @Valid @RequestBody CandidateRequestDTO request) {

        log.info("Received candidate form submission");

        CandidateResponseDTO response = candidateService.saveCandidate(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Details submitted successfully!", response));
    }
}