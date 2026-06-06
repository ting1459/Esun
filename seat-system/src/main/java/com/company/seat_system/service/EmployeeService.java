package com.company.seat_system.service;

import java.util.List;

import com.company.seat_system.dto.EmployeeDto;
import com.company.seat_system.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing employee operations.
 * Handles business logic for employee queries.
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Constructs an EmployeeService with an employee repository.
     *
     * @param employeeRepository the repository for employee data access
     */
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Retrieves all employees.
     *
     * @return a list of EmployeeDto objects containing all employees
     */
    public List<EmployeeDto> getEmployees() {
        return employeeRepository.findAll();
    }
}
