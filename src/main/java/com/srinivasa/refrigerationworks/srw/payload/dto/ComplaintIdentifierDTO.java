package com.srinivasa.refrigerationworks.srw.payload.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/*
 * DTO for complaint identification (complaintId, phoneNumber, registered date).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintIdentifierDTO {

    /*
     * Complaint ID or phone number.
     */
    @Pattern(regexp = "^[0-9]{10}$|^\\+91[0-9]{10}$|^SRWC[0-9]{8}$")
    private String identifier;

    /*
     * Complaint registration date.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registeredDate;
}