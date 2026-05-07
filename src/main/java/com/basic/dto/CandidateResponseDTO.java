package com.basic.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String panCard;
    private String currentCompany;
    private Boolean onNoticePeriod;
    private Integer noticePeriodDays;
    private LocalDate lastWorkingDay;
    private Double currentSalary;
    private Double expectedSalary;
    private LocalDateTime submittedAt;
}