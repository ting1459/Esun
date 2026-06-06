package com.company.seat_system.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Database configuration and initialization class.
 * Sets up the H2 database schema, stored procedures, and sample data on application startup.
 */
@Configuration
public class DatabaseInitializer {

    /**
     * Initializes the database with DDL, stored procedures, and sample data.
     * This bean is executed automatically on application startup.
     *
     * @param jdbc the JDBC template for executing SQL statements
     * @return an ApplicationRunner that performs database initialization
     */
    @Bean
    ApplicationRunner initDatabase(JdbcTemplate jdbc) {
        return args -> {
            // Create table schema
            jdbc.execute("DROP TABLE IF EXISTS EMPLOYEE");
            jdbc.execute("DROP TABLE IF EXISTS SEATING_CHART");
            jdbc.execute("CREATE TABLE SEATING_CHART ("
                + "FLOOR_SEAT_SEQ BIGINT AUTO_INCREMENT PRIMARY KEY,"
                + "FLOOR_NO INT NOT NULL,"
                + "SEAT_NO VARCHAR(20) NOT NULL)");
            jdbc.execute("CREATE TABLE EMPLOYEE ("
                + "EMP_ID CHAR(5) PRIMARY KEY,"
                + "NAME VARCHAR(50) NOT NULL,"
                + "EMAIL VARCHAR(100),"
                + "FLOOR_SEAT_SEQ BIGINT,"
                + "CONSTRAINT FK_EMP_SEAT FOREIGN KEY (FLOOR_SEAT_SEQ)"
                + " REFERENCES SEATING_CHART(FLOOR_SEAT_SEQ))");

            // Register stored procedure to retrieve all seats
            jdbc.execute("DROP ALIAS IF EXISTS SP_GET_SEATS");
            jdbc.execute("CREATE ALIAS SP_GET_SEATS AS $$ "
                + "import java.sql.*; "
                + "@CODE "
                + "ResultSet SP_GET_SEATS(Connection conn) throws SQLException { "
                + "  return conn.prepareStatement("
                + "\"SELECT sc.FLOOR_SEAT_SEQ,sc.FLOOR_NO,sc.SEAT_NO,e.EMP_ID,e.NAME AS EMPLOYEE_NAME "
                + "FROM SEATING_CHART sc LEFT JOIN EMPLOYEE e ON e.FLOOR_SEAT_SEQ=sc.FLOOR_SEAT_SEQ "
                + "ORDER BY sc.FLOOR_NO,sc.SEAT_NO\""
                + ").executeQuery(); "
                + "} $$");

            // Register stored procedure to retrieve all employees
            jdbc.execute("DROP ALIAS IF EXISTS SP_GET_EMPLOYEES");
            jdbc.execute("CREATE ALIAS SP_GET_EMPLOYEES AS $$ "
                + "import java.sql.*; "
                + "@CODE "
                + "ResultSet SP_GET_EMPLOYEES(Connection conn) throws SQLException { "
                + "  return conn.prepareStatement("
                + "\"SELECT EMP_ID,NAME,EMAIL,FLOOR_SEAT_SEQ FROM EMPLOYEE ORDER BY EMP_ID\""
                + ").executeQuery(); "
                + "} $$");

            // Register stored procedure to assign a seat to an employee
            jdbc.execute("DROP ALIAS IF EXISTS SP_ASSIGN_SEAT");
            jdbc.execute("CREATE ALIAS SP_ASSIGN_SEAT AS $$ "
                + "import java.sql.*; "
                + "@CODE "
                + "void SP_ASSIGN_SEAT(Connection conn,String empId,Long floorSeatSeq) throws SQLException { "
                + "  PreparedStatement c1=conn.prepareStatement(\"SELECT COUNT(*) FROM EMPLOYEE WHERE EMP_ID=?\"); "
                + "  c1.setString(1,empId); ResultSet r1=c1.executeQuery(); r1.next(); "
                + "  if(r1.getInt(1)==0) throw new SQLException(\"\u67e5\u7121\u54e1\u5de5\u8cc7\u6599\"); "
                + "  PreparedStatement c2=conn.prepareStatement(\"SELECT COUNT(*) FROM SEATING_CHART WHERE FLOOR_SEAT_SEQ=?\"); "
                + "  c2.setLong(1,floorSeatSeq); ResultSet r2=c2.executeQuery(); r2.next(); "
                + "  if(r2.getInt(1)==0) throw new SQLException(\"\u67e5\u7121\u5ea7\u4f4d\u8cc7\u6599\"); "
                + "  PreparedStatement c3=conn.prepareStatement(\"SELECT EMP_ID FROM EMPLOYEE WHERE FLOOR_SEAT_SEQ=?\"); "
                + "  c3.setLong(1,floorSeatSeq); ResultSet r3=c3.executeQuery(); "
                + "  if(r3.next()&&!r3.getString(\"EMP_ID\").trim().equals(empId.trim())) "
                + "    throw new SQLException(\"\u5ea7\u4f4d\u5df2\u88ab\u5176\u4ed6\u54e1\u5de5\u4f54\u7528\"); "
                + "  PreparedStatement c4=conn.prepareStatement(\"UPDATE EMPLOYEE SET FLOOR_SEAT_SEQ=NULL WHERE EMP_ID=?\"); "
                + "  c4.setString(1,empId); c4.executeUpdate(); "
                + "  PreparedStatement c5=conn.prepareStatement(\"UPDATE EMPLOYEE SET FLOOR_SEAT_SEQ=? WHERE EMP_ID=?\"); "
                + "  c5.setLong(1,floorSeatSeq); c5.setString(2,empId); c5.executeUpdate(); "
                + "} $$");

            // Register stored procedure to clear a seat assignment
            jdbc.execute("DROP ALIAS IF EXISTS SP_CLEAR_SEAT");
            jdbc.execute("CREATE ALIAS SP_CLEAR_SEAT AS $$ "
                + "import java.sql.*; "
                + "@CODE "
                + "void SP_CLEAR_SEAT(Connection conn,Long floorSeatSeq) throws SQLException { "
                + "  PreparedStatement c1=conn.prepareStatement(\"SELECT COUNT(*) FROM SEATING_CHART WHERE FLOOR_SEAT_SEQ=?\"); "
                + "  c1.setLong(1,floorSeatSeq); ResultSet r1=c1.executeQuery(); r1.next(); "
                + "  if(r1.getInt(1)==0) throw new SQLException(\"\u67e5\u7121\u5ea7\u4f4d\u8cc7\u6599\"); "
                + "  PreparedStatement ps=conn.prepareStatement(\"UPDATE EMPLOYEE SET FLOOR_SEAT_SEQ=NULL WHERE FLOOR_SEAT_SEQ=?\"); "
                + "  ps.setLong(1,floorSeatSeq); ps.executeUpdate(); "
                + "} $$");

            // Seed sample data: 4 floors with 4 seats each
            String[] seatNumbers = {"\u5ea7\u4f4d1", "\u5ea7\u4f4d2", "\u5ea7\u4f4d3", "\u5ea7\u4f4d4"};
            for (int floorNumber = 1; floorNumber <= 4; floorNumber++)
                for (String seatNumber : seatNumbers)
                    jdbc.update("INSERT INTO SEATING_CHART (FLOOR_NO,SEAT_NO) VALUES (?,?)", floorNumber, seatNumber);

            // Seed sample employees (all without seat assignments initially)
            jdbc.update("INSERT INTO EMPLOYEE VALUES (?,?,?,?)", "10001", "\u738b\u5c0f\u660e", "wang@company.com", null);
            jdbc.update("INSERT INTO EMPLOYEE VALUES (?,?,?,?)", "10002", "\u674e\u96c5\u5a77", "yating.li@example.com", null);
            jdbc.update("INSERT INTO EMPLOYEE VALUES (?,?,?,?)", "10003", "\u9673\u5fd7\u8c6a", "zhihao.chen@example.com", null);
            jdbc.update("INSERT INTO EMPLOYEE VALUES (?,?,?,?)", "10004", "\u6797\u4f73\u8363", "jiarong.lin@example.com", null);
            jdbc.update("INSERT INTO EMPLOYEE VALUES (?,?,?,?)", "10005", "\u5f35\u5bb6\u8c6a", "jiahao.zhang@example.com", null);
            jdbc.update("INSERT INTO EMPLOYEE VALUES (?,?,?,?)", "10006", "\u9ec3\u8a69\u6db5", "shihan.huang@example.com", null);
        };
    }
}
