package com.company.seat_system.dto;

/**
 * Data Transfer Object for employee information.
 * Contains details about a specific employee and their seat assignment.
 *
 * @param empId         the employee ID (5-digit code)
 * @param name          the employee's name
 * @param email         the employee's email address
 * @param floorSeatSeq  the unique identifier of the assigned seat (null if not assigned)
 */
public record EmployeeDto(
        String empId,
        String name,
        String email,
        Long floorSeatSeq
) {
}
