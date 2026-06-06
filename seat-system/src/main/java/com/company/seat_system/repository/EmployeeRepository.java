package com.company.seat_system.repository;

import java.util.List;

import com.company.seat_system.dto.EmployeeDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository class for employee data access operations.
 * Uses JDBC and stored procedures to interact with the EMPLOYEE table.
 */
@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs an EmployeeRepository with a JDBC template.
     *
     * @param jdbcTemplate the JDBC template for database operations
     */
    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves all employees from the database.
     * Uses stored procedure SP_GET_EMPLOYEES to fetch employee data with seat assignments.
     *
     * @return a list of all EmployeeDto objects with their assigned seat information
     */
    public List<EmployeeDto> findAll() {
        return jdbcTemplate.query("CALL SP_GET_EMPLOYEES()", (rs, rowNum) -> {
            long seq = rs.getLong("FLOOR_SEAT_SEQ");
            return new EmployeeDto(
                rs.getString("EMP_ID"),
                rs.getString("NAME"),
                rs.getString("EMAIL"),
                rs.wasNull() ? null : seq
            );
        });
    }
}
