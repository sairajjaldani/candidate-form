package com.basic.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    
 // POST /api/candidates/{id}/resume
    @PostMapping("/{id}/resume")
    public ResponseEntity<ApiResponse<String>> uploadResume(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Please select a file"));
        }

        // Only allow PDF, DOC, DOCX
        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        if (!List.of(".pdf", ".doc", ".docx").contains(ext)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Only PDF, DOC, DOCX files allowed"));
        }
        
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File size must be under 5MB"));
        }

        String saved = candidateService.saveResume(id, file);

        return ResponseEntity.ok(ApiResponse.success("Resume uploaded!", saved));
    }
}