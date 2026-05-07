package com.basic.service;




import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.basic.dto.CandidateRequestDTO;
import com.basic.dto.CandidateResponseDTO;
import com.basic.model.Candidate;
import com.basic.repository.CandidateRepository;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    @Override
    public CandidateResponseDTO saveCandidate(CandidateRequestDTO request) {

        log.info("Saving candidate with PAN: {}", request.getPanCard());

        if (candidateRepository.existsByPanCard(request.getPanCard())) {
            throw new ValidationException("A candidate with this PAN card already exists.");
        }

        if (Boolean.TRUE.equals(request.getOnNoticePeriod())) {
            if (request.getNoticePeriodDays() == null) {
                throw new ValidationException("Notice period days are required.");
            }
            if (request.getLastWorkingDay() == null) {
                throw new ValidationException("Last working day is required.");
            }
        }

        Candidate candidate = toEntity(request);
        Candidate saved = candidateRepository.save(candidate);

        log.info("Candidate saved with ID: {}", saved.getId());

        return toResponseDTO(saved);
    }

    @Override
    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private Candidate toEntity(CandidateRequestDTO dto) {
        return Candidate.builder()
                .firstName(dto.getFirstName().trim())
                .lastName(dto.getLastName().trim())
                .dob(dto.getDob())
                .panCard(dto.getPanCard().toUpperCase().trim())
                .currentCompany(dto.getCurrentCompany().trim())
                .onNoticePeriod(dto.getOnNoticePeriod())
                .noticePeriodDays(
                    Boolean.TRUE.equals(dto.getOnNoticePeriod())
                        ? dto.getNoticePeriodDays() : null
                )
                .lastWorkingDay(
                    Boolean.TRUE.equals(dto.getOnNoticePeriod())
                        ? dto.getLastWorkingDay() : null
                )
                .currentSalary(dto.getCurrentSalary())
                .expectedSalary(dto.getExpectedSalary())
                .build();
    }

    private CandidateResponseDTO toResponseDTO(Candidate candidate) {
        return CandidateResponseDTO.builder()
                .id(candidate.getId())
                .firstName(candidate.getFirstName())
                .lastName(candidate.getLastName())
                .dob(candidate.getDob())
                .panCard(candidate.getPanCard())
                .currentCompany(candidate.getCurrentCompany())
                .onNoticePeriod(candidate.getOnNoticePeriod())
                .noticePeriodDays(candidate.getNoticePeriodDays())
                .lastWorkingDay(candidate.getLastWorkingDay())
                .currentSalary(candidate.getCurrentSalary())
                .expectedSalary(candidate.getExpectedSalary())
                .submittedAt(candidate.getSubmittedAt())
                .build();
    }
}