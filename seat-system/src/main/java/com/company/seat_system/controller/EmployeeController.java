package com.company.seat_system.controller;

import java.util.List;

import com.company.seat_system.common.ApiResponse;
import com.company.seat_system.dto.EmployeeDto;
import com.company.seat_system.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for employee management operations.
 * Provides endpoints for retrieving employee information.
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Constructs an EmployeeController with an employee service.
     *
     * @param employeeService the service for employee operations
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves all employees.
     * Returns a list of all employees in the system.
     *
     * @return an ApiResponse containing a list of EmployeeDto objects
     */
    @GetMapping
    public ApiResponse<List<EmployeeDto>> getEmployees() {
        return ApiResponse.ok(employeeService.getEmployees());
    }
}
