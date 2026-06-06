package com.company.seat_system.dto;

import com.company.seat_system.common.SeatStatus;

/**
 * Data Transfer Object for seat information.
 * Contains details about a specific seat and its occupancy status.
 *
 * @param floorSeatSeq  the unique identifier for the seat
 * @param floorNo       the floor number where the seat is located
 * @param seatNo        the seat number/identifier on the floor
 * @param empId         the ID of the employee assigned to this seat (null if available)
 * @param employeeName  the name of the employee assigned to this seat (null if available)
 * @param status        the current status of the seat (AVAILABLE or OCCUPIED)
 */
public record SeatDto(
        Long floorSeatSeq,
        Integer floorNo,
        String seatNo,
        String empId,
        String employeeName,
        SeatStatus status
) {
}
