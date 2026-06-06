package com.company.seat_system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * Data Transfer Object for seat assignment requests.
 * Validates that the request contains valid employee ID and seat information.
 *
 * @param empId         the 5-digit employee ID to assign the seat to
 * @param floorSeatSeq  the unique identifier of the seat to assign
 */
public record AssignSeatRequest(
        @NotBlank(message = "員工編號不可空白")
        @Pattern(regexp = "\\d{5}", message = "員工編號需為固定 5 碼數字")
        String empId,

        @NotNull(message = "座位序號不可空白")
        @Positive(message = "座位序號需為正整數")
        Long floorSeatSeq
) {
}
