package com.srinivasa.refrigerationworks.srw.payload.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO for user identification, supports userId, phoneNumber, or email.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIdentifierDTO {

    /*
     * Identifier representing userId, phoneNumber, or email.
     */
    @Pattern(regexp = "^[0-9]{10}$|^\\+91[0-9]{10}$|^SRW[0-9]{3}$|^SRW[0-9]{4}$|^SRW[0-9]{7}$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String identifier;
}