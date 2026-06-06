package com.company.seat_system.service;

import com.company.seat_system.dto.EmployeeDto;
import com.company.seat_system.dto.FloorSeatsDto;
import com.company.seat_system.dto.SeatDto;
import com.company.seat_system.repository.EmployeeRepository;
import com.company.seat_system.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing seat operations.
 * Handles business logic for seat assignments, clearances, and seat/employee queries.
 */
@Service
public class SeatService {

    private final EmployeeRepository employeeRepository;
    private final SeatRepository seatRepository;

    /**
     * Constructs a SeatService with employee and seat repositories.
     *
     * @param employeeRepository the repository for employee data access
     * @param seatRepository     the repository for seat data access
     */
    public SeatService(EmployeeRepository employeeRepository,
                       SeatRepository seatRepository) {
        this.employeeRepository = employeeRepository;
        this.seatRepository = seatRepository;
    }

    /**
     * Retrieves all seats organized by floor.
     *
     * @return a list of FloorSeatsDto objects, each containing seats for a specific floor
     */
    public List<FloorSeatsDto> getSeatsByFloor() {
        Map<Integer, List<SeatDto>> seatsByFloor = seatRepository.findAll().stream()
                .collect(Collectors.groupingBy(SeatDto::floorNo));

        return seatsByFloor.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new FloorSeatsDto(entry.getKey(), entry.getValue()))
                .toList();
    }

    /**
     * Retrieves all employees.
     *
     * @return a list of EmployeeDto objects
     */
    public List<EmployeeDto> getEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Retrieves all seats.
     *
     * @return a list of SeatDto objects
     */
    public List<SeatDto> getSeats() {
        return seatRepository.findAll();
    }

    /**
     * Assigns a seat to an employee.
     * Clears any previously assigned seat for the employee before assigning the new one.
     *
     * @param empId         the employee ID
     * @param floorSeatSeq  the unique identifier of the seat to assign
     */
    public void assignSeat(String empId, Long floorSeatSeq) {
        seatRepository.assignSeat(empId, floorSeatSeq);
    }

    /**
     * Clears a seat assignment (makes the seat available).
     *
     * @param floorSeatSeq the unique identifier of the seat to clear
     */
    public void clearSeat(Long floorSeatSeq) {
        seatRepository.clearSeat(floorSeatSeq);
    }
}