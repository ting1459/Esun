DROP TABLE IF EXISTS EMPLOYEE;
DROP TABLE IF EXISTS SEATING_CHART;

CREATE TABLE SEATING_CHART (
    FLOOR_SEAT_SEQ BIGINT AUTO_INCREMENT PRIMARY KEY,
    FLOOR_NO       INT         NOT NULL,
    SEAT_NO        VARCHAR(20) NOT NULL
);

CREATE TABLE EMPLOYEE (
    EMP_ID         CHAR(5)     PRIMARY KEY,
    NAME           VARCHAR(50) NOT NULL,
    EMAIL          VARCHAR(100),
    FLOOR_SEAT_SEQ BIGINT,
    CONSTRAINT FK_EMP_SEAT FOREIGN KEY (FLOOR_SEAT_SEQ) REFERENCES SEATING_CHART(FLOOR_SEAT_SEQ)
);

-- Stored Procedures (H2 syntax)
CREATE ALIAS IF NOT EXISTS SP_GET_SEATS AS $$
import java.sql.*;
ResultSet SP_GET_SEATS(Connection conn) throws SQLException {
    String sql = """
        SELECT sc.FLOOR_SEAT_SEQ, sc.FLOOR_NO, sc.SEAT_NO,
               e.EMP_ID, e.NAME AS EMPLOYEE_NAME
        FROM SEATING_CHART sc
        LEFT JOIN EMPLOYEE e ON e.FLOOR_SEAT_SEQ = sc.FLOOR_SEAT_SEQ
        ORDER BY sc.FLOOR_NO, sc.SEAT_NO
        """;
    return conn.prepareStatement(sql).executeQuery();
}
$$;

CREATE ALIAS IF NOT EXISTS SP_GET_EMPLOYEES AS $$
import java.sql.*;
ResultSet SP_GET_EMPLOYEES(Connection conn) throws SQLException {
    return conn.prepareStatement(
        "SELECT EMP_ID, NAME, EMAIL, FLOOR_SEAT_SEQ FROM EMPLOYEE ORDER BY EMP_ID"
    ).executeQuery();
}
$$;

CREATE ALIAS IF NOT EXISTS SP_ASSIGN_SEAT AS $$
import java.sql.*;
void SP_ASSIGN_SEAT(Connection conn, String empId, Long floorSeatSeq) throws SQLException {
    // Validate employee exists
    PreparedStatement chkEmp = conn.prepareStatement(
        "SELECT COUNT(*) FROM EMPLOYEE WHERE EMP_ID = ?");
    chkEmp.setString(1, empId);
    ResultSet rs = chkEmp.executeQuery();
    rs.next();
    if (rs.getInt(1) == 0) throw new SQLException("查無員工資料");

    // Validate seat exists
    PreparedStatement chkSeat = conn.prepareStatement(
        "SELECT COUNT(*) FROM SEATING_CHART WHERE FLOOR_SEAT_SEQ = ?");
    chkSeat.setLong(1, floorSeatSeq);
    ResultSet rs2 = chkSeat.executeQuery();t();
    if (rs2.getInt(1) == 0) throw new SQLException("查無座位資料");

    // Check seat not taken by another employee
    PreparedStatement chkOccupied = conn.prepareStatement(
        "SELECT EMP_ID FROM EMPLOYEE WHERE FLOOR_SEAT_SEQ = ?");
    chkOccupied.setLong(1, floorSeatSeq);
    ResultSet rs3 = chkOccupied.executeQuery();
    if (rs3.next() && !rs3.getString("EMP_ID").equals(empId)) {
        throw new SQLException("座位已被其他員工佔用");
    }

    // Clear employee's current seat
    PreparedStatement clearOld = conn.prepareStatement(
        "UPDATE EMPLOYEE SET FLOOR_SEAT_SEQ = NULL WHERE EMP_ID = ?");
    clearOld.setString(1, empId);
    clearOld.executeUpdate();

    // Assign new seat
    PreparedStatement assign = conn.prepareStatement(
        "UPDATE EMPLOYEE SET FLOOR_SEAT_SEQ = ? WHERE EMP_ID = ?");
    assign.setLong(1, floorSeatSeq);
    assign.setString(2, empId);
    assign.executeUpdate();
}
$$;

CREATE ALIAS IF NOT EXISTS SP_CLEAR_SEAT AS $$
import java.sql.*;onn, Long floorSeatSeq) throws SQLException {
    PreparedStatement chkSeat = conn.prepareStatement(
        "SELECT COUNT(*) FROM SEATING_CHART WHERE FLOOR_SEAT_SEQ = ?");
    chkSeat.setLong(1, floorSeatSeq);
    ResultSet rs = chkSeat.executeQuery();
    rs.next();
    if (rs.getInt(1) == 0) throw new SQLException("查無座位資料");

    PreparedStatement ps = conn.prepareStatement(
        "UPDATE EMPLOYEE SET FLOOR_SEAT_SEQ = NULL WHERE FLOOR_SEAT_SEQ = ?");
    ps.setLong(1, floorSeatSeq);
    ps.executeUpdate();
}
$$;
