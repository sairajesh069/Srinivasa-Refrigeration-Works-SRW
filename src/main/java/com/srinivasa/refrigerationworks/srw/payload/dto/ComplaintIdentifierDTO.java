package com.srinivasa.refrigerationworks.srw.payload.dto;

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
    private String identifier;

    /*
     * Complaint registration date.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registeredDate;
}