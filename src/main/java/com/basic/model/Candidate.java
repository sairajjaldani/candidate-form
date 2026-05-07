package com.basic.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false, unique = true)
    private String panCard;

    @Column(nullable = false)
    private String currentCompany;

    @Column(nullable = false)
    private Boolean onNoticePeriod;

    // Only filled if onNoticePeriod = true
    private Integer noticePeriodDays;
    private LocalDate lastWorkingDay;

    @Column(nullable = false)
    private Double currentSalary;

    @Column(nullable = false)
    private Double expectedSalary;

    @Column(updatable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    public void prePersist() {
        this.submittedAt = LocalDateTime.now();
    }
}
