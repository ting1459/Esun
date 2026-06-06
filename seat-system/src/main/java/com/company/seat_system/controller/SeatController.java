package com.company.seat_system.controller;

import java.util.List;

import com.company.seat_system.common.ApiResponse;
import com.company.seat_system.dto.AssignSeatRequest;
import com.company.seat_system.dto.FloorSeatsDto;
import com.company.seat_system.service.SeatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for seat management operations.
 * Provides endpoints for retrieving seats, assigning seats to employees, and clearing seat assignments.
 */
@Validated
@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    /**
     * Constructs a SeatController with a seat service.
     *
     * @param seatService the service for seat operations
     */
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    /**
     * Retrieves all seats organized by floor.
     * Returns seats grouped by floor number in ascending order.
     *
     * @return an ApiResponse containing a list of FloorSeatsDto objects
     */
    @GetMapping("/floors")
    public ApiResponse<List<FloorSeatsDto>> getFloors() {
        return ApiResponse.ok(seatService.getSeatsByFloor());
    }

    /**
     * Assigns a seat to an employee.
     * If the employee already has a seat assigned, it will be cleared first.
     *
     * @param request the seat assignment request containing employee ID and seat ID
     * @return an ApiResponse with success status
     */
    @PostMapping("/assign")
    public ApiResponse<Void> assignSeat(@Valid @RequestBody AssignSeatRequest request) {
        seatService.assignSeat(request.empId(), request.floorSeatSeq());
        return ApiResponse.ok("座位已更新");
    }

    /**
     * Clears a seat assignment (makes it available).
     * Removes the employee assignment from the specified seat.
     *
     * @param floorSeatSeq the unique identifier of the seat to clear
     * @return an ApiResponse with success status
     */
    @DeleteMapping("/{floorSeatSeq}/employee")
    public ApiResponse<Void> clearSeat(@PathVariable @Positive Long floorSeatSeq) {
        seatService.clearSeat(floorSeatSeq);
        return ApiResponse.ok("座位已清除");
    }
}
