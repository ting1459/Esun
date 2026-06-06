package com.company.seat_system.dto;

import java.util.List;

/**
 * Data Transfer Object for a floor's seating information.
 * Groups all seats for a specific floor.
 *
 * @param floorNo the floor number
 * @param seats   the list of seats on this floor
 */
public record FloorSeatsDto(
        Integer floorNo,
        List<SeatDto> seats
) {
}
