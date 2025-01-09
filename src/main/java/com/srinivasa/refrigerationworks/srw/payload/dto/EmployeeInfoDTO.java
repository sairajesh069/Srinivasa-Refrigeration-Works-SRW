package com.srinivasa.refrigerationworks.srw.payload.dto;

import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/*
 * DTO class for transferring Employee info.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfoDTO implements Serializable {

    /*
     * Unique ID for serialization compatibility.
     */
    @Serial
    private static final long serialVersionUID = 42L;

    /*
     * Employee's unique Id.
     */
    private String employeeId;

    /*
     * Employee's full name.
     */
    private String fullName;

    /*
     * Employee's phone number.
     */
    private String phoneNumber;

    /*
     * Employee's designation.
     */
    private String designation;

    /*
     * Employee's status.
     */
    private UserStatus status;
}