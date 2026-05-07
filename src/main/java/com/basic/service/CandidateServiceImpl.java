package com.basic.service;




import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.basic.dto.CandidateRequestDTO;
import com.basic.dto.CandidateResponseDTO;
import com.basic.model.Candidate;
import com.basic.repository.CandidateRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final Cloudinary cloudinary;
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
                .portfolioLink(dto.getPortfolioLink())
                .linkedinLink(dto.getLinkedinLink())
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
                .resumeFileName(candidate.getResumeFileName())
                .portfolioLink(candidate.getPortfolioLink())
                .linkedinLink(candidate.getLinkedinLink())
                .build();
    }
    
    @Override
    public String saveResume(Long id, MultipartFile file) throws Exception {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Candidate not found"));

        log.info("Uploading resume to Cloudinary for candidate ID: {}", id);

        // Upload to Cloudinary — auto detects any file format
        Map uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                "folder", "candidate-resumes",
                "resource_type", "auto",
                "public_id", "candidate_" + id + "_" + System.currentTimeMillis()
            )
        );
        // Get URL
        String url = (String) uploadResult.get("secure_url");

        // Fix URL so PDF/file opens directly in browser
        if (url.contains("/raw/upload/")) {
            url = url.replace("/raw/upload/", "/raw/upload/fl_attachment/");
        }

        log.info("Resume uploaded to Cloudinary: {}", url);

        // Save to DB
        candidate.setResumeFileName(file.getOriginalFilename());
        candidate.setResumeUrl(url);
        candidate.setResumePublicId((String) uploadResult.get("public_id"));
        candidateRepository.save(candidate);

        return url;
    }
}