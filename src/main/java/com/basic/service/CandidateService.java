package com.basic.service;

import java.util.List;

import com.basic.dto.CandidateRequestDTO;
import com.basic.dto.CandidateResponseDTO;

public interface CandidateService {

    CandidateResponseDTO saveCandidate(CandidateRequestDTO request);

    List<CandidateResponseDTO> getAllCandidates();
}