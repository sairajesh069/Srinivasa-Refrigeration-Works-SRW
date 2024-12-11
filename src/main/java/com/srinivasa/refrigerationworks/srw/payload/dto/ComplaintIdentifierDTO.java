package com.srinivasa.refrigerationworks.srw.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/*
 * DTO for complaint identification.
 * Can represent complaintId or phoneNumber and registered date for querying a complaint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintIdentifierDTO {

    /*
     * Identifier representing complaintId or phoneNumber.
     */
    private String identifier;

    /*
     * Date when the complaint is registered.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registeredDate;
}