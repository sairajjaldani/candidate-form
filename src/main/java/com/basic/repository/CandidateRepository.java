package com.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.basic.model.Candidate;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    // Check if PAN already exists - avoid duplicate entries
    boolean existsByPanCard(String panCard);

    // Find by PAN if needed later
    Optional<Candidate> findByPanCard(String panCard);
}
