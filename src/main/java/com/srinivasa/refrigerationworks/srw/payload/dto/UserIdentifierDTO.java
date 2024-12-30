package com.srinivasa.refrigerationworks.srw.payload.dto;

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
    private String identifier;
}