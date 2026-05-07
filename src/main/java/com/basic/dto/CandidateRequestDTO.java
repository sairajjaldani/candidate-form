package com.basic.dto;


import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateRequestDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "DOB must be a past date")
    private LocalDate dob;

    @NotBlank(message = "PAN card is required")
    @Pattern(
        regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}",
        message = "Invalid PAN format. Example: ABCDE1234F"
    )
    private String panCard;

    @NotBlank(message = "Current company is required")
    private String currentCompany;

    @NotNull(message = "Please select notice period status")
    private Boolean onNoticePeriod;

    // Only required if onNoticePeriod = true
    // Validated manually in service
    private Integer noticePeriodDays;
    private LocalDate lastWorkingDay;

    @NotNull(message = "Current salary is required")
    @Positive(message = "Salary must be a positive number")
    private Double currentSalary;

    @NotNull(message = "Expected salary is required")
    @Positive(message = "Salary must be a positive number")
    private Double expectedSalary;
}