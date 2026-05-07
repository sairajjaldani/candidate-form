package com.basic.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.basic.dto.CandidateRequestDTO;
import com.basic.dto.CandidateResponseDTO;

public interface CandidateService {

    CandidateResponseDTO saveCandidate(CandidateRequestDTO request);

    String saveResume(Long id, MultipartFile file) throws Exception;
    List<CandidateResponseDTO> getAllCandidates();
}
