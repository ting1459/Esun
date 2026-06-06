package com.company.seat_system.repository;

import java.util.List;

import com.company.seat_system.common.SeatStatus;
import com.company.seat_system.dto.SeatDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository class for seat data access operations.
 * Uses JDBC and stored procedures to interact with the SEATING_CHART table.
 */
@Repository
public class SeatRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a SeatRepository with a JDBC template.
     *
     * @param jdbcTemplate the JDBC template for database operations
     */
    public SeatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves all seats from the database.
     * Uses stored procedure SP_GET_SEATS to fetch seat data with employee information.
     *
     * @return a list of all SeatDto objects with their current assignment status
     */
    public List<SeatDto> findAll() {
        return jdbcTemplate.query("CALL SP_GET_SEATS()", (rs, rowNum) -> {
            String empId = rs.getString("EMP_ID");
            return new SeatDto(
                    rs.getLong("FLOOR_SEAT_SEQ"),
                    rs.getInt("FLOOR_NO"),
                    rs.getString("SEAT_NO"),
                    empId,
                    rs.getString("EMPLOYEE_NAME"),
                    empId == null ? SeatStatus.AVAILABLE : SeatStatus.OCCUPIED
            );
        });
    }

    /**
     * Assigns a seat to an employee.
     * Uses stored procedure SP_ASSIGN_SEAT to perform the assignment.
     * If the employee already has a seat, it will be cleared first.
     *
     * @param empId         the employee ID to assign the seat to
     * @param floorSeatSeq  the unique identifier of the seat to assign
     */
    public void assignSeat(String empId, Long floorSeatSeq) {
        jdbcTemplate.update("CALL SP_ASSIGN_SEAT(?, ?)", empId, floorSeatSeq);
    }

    /**
     * Clears a seat assignment (makes it available).
     * Uses stored procedure SP_CLEAR_SEAT to remove the employee assignment.
     *
     * @param floorSeatSeq the unique identifier of the seat to clear
     */
    public void clearSeat(Long floorSeatSeq) {
        jdbcTemplate.update("CALL SP_CLEAR_SEAT(?)", floorSeatSeq);
    }
}
