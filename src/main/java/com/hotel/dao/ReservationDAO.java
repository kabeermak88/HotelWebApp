// File: src/com/hotel/dao/ReservationDAO.java
package com.hotel.dao;

import com.hotel.model.Reservation;
import com.hotel.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date; // For date parameters
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map; // For frequent rooms

public class ReservationDAO {

    // Method to add a new reservation
    public boolean addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservations (CustomerName, RoomNumber, CheckIn, CheckOut, TotalAmount) VALUES (?, ?, ?, ?, ?)";
        boolean rowInserted = false;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Added RETURN_GENERATED_KEYS

            statement.setString(1, reservation.getCustomerName());
            statement.setString(2, reservation.getRoomNumber());
            statement.setDate(3, new java.sql.Date(reservation.getCheckIn().getTime()));
            statement.setDate(4, new java.sql.Date(reservation.getCheckOut().getTime()));
            statement.setBigDecimal(5, reservation.getTotalAmount());

            rowInserted = statement.executeUpdate() > 0;

            // If you have AUTO_INCREMENT on ReservationID and want to get it back:
            if (rowInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reservation.setReservationID(generatedKeys.getInt(1));
                    }
                }
            }
        }
        return rowInserted;
    }

    // Method to get a reservation by ID
    public Reservation getReservationById(int reservationId) throws SQLException {
        Reservation reservation = null;
        String sql = "SELECT * FROM Reservations WHERE ReservationID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reservationId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String customerName = resultSet.getString("CustomerName");
                String roomNumber = resultSet.getString("RoomNumber");
                java.sql.Date checkInSql = resultSet.getDate("CheckIn");
                java.sql.Date checkOutSql = resultSet.getDate("CheckOut");
                java.math.BigDecimal totalAmount = resultSet.getBigDecimal("TotalAmount");

                java.util.Date checkIn = new java.util.Date(checkInSql.getTime());
                java.util.Date checkOut = new java.util.Date(checkOutSql.getTime());

                reservation = new Reservation(reservationId, customerName, roomNumber, checkIn, checkOut, totalAmount);
            }
        }
        return reservation;
    }

    // Method to get all reservations
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservations ORDER BY CheckIn DESC, ReservationID DESC";

        try (Connection connection = DBUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("ReservationID");
                String customerName = resultSet.getString("CustomerName");
                String roomNumber = resultSet.getString("RoomNumber");
                java.sql.Date checkInSql = resultSet.getDate("CheckIn");
                java.sql.Date checkOutSql = resultSet.getDate("CheckOut");
                java.math.BigDecimal totalAmount = resultSet.getBigDecimal("TotalAmount");

                java.util.Date checkIn = new java.util.Date(checkInSql.getTime());
                java.util.Date checkOut = new java.util.Date(checkOutSql.getTime());

                reservations.add(new Reservation(id, customerName, roomNumber, checkIn, checkOut, totalAmount));
            }
        }
        return reservations;
    }

    // Method to update an existing reservation
    public boolean updateReservation(Reservation reservation) throws SQLException {
        String sql = "UPDATE Reservations SET CustomerName = ?, RoomNumber = ?, CheckIn = ?, CheckOut = ?, TotalAmount = ? WHERE ReservationID = ?";
        boolean rowUpdated = false;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, reservation.getCustomerName());
            statement.setString(2, reservation.getRoomNumber());
            statement.setDate(3, new java.sql.Date(reservation.getCheckIn().getTime()));
            statement.setDate(4, new java.sql.Date(reservation.getCheckOut().getTime()));
            statement.setBigDecimal(5, reservation.getTotalAmount());
            statement.setInt(6, reservation.getReservationID());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // Method to delete a reservation
    public boolean deleteReservation(int reservationId) throws SQLException {
        String sql = "DELETE FROM Reservations WHERE ReservationID = ?";
        boolean rowDeleted = false;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reservationId);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    // --- REPORTING METHODS ---

    // Report 1: Reservations in a date range
    public List<Reservation> getReservationsByDateRange(Date startDate, Date endDate) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE CheckIn <= ? AND CheckOut >= ? ORDER BY CheckIn";
        
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, new java.sql.Date(endDate.getTime()));
            statement.setDate(2, new java.sql.Date(startDate.getTime()));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ReservationID");
                String customerName = resultSet.getString("CustomerName");
                String roomNumber = resultSet.getString("RoomNumber");
                java.sql.Date checkInSql = resultSet.getDate("CheckIn");
                java.sql.Date checkOutSql = resultSet.getDate("CheckOut");
                BigDecimal totalAmount = resultSet.getBigDecimal("TotalAmount");

                reservations.add(new Reservation(id, customerName, roomNumber, 
                                   new java.util.Date(checkInSql.getTime()), 
                                   new java.util.Date(checkOutSql.getTime()), 
                                   totalAmount));
            }
        }
        return reservations;
    }

    // Report 2: Rooms booked most frequently
    public Map<String, Integer> getMostFrequentRooms(int limit, Date startDate, Date endDate) throws SQLException {
        Map<String, Integer> frequentRooms = new LinkedHashMap<>(); // Use LinkedHashMap to preserve order from DB
        StringBuilder sqlBuilder = new StringBuilder("SELECT RoomNumber, COUNT(*) as BookingCount ");
        sqlBuilder.append("FROM Reservations ");

        List<Object> params = new ArrayList<>(); // To hold query parameters

        // Building WHERE clause dynamically
        boolean hasWhereClause = false;
        if (startDate != null && endDate != null) {
            sqlBuilder.append("WHERE CheckIn <= ? AND CheckOut >= ? ");
            params.add(new java.sql.Date(endDate.getTime()));
            params.add(new java.sql.Date(startDate.getTime()));
            hasWhereClause = true;
        } else if (startDate != null) {
            sqlBuilder.append("WHERE CheckOut >= ? ");
            params.add(new java.sql.Date(startDate.getTime()));
            hasWhereClause = true;
        } else if (endDate != null) {
            sqlBuilder.append("WHERE CheckIn <= ? ");
            params.add(new java.sql.Date(endDate.getTime()));
            hasWhereClause = true;
        }
        
        sqlBuilder.append("GROUP BY RoomNumber ORDER BY BookingCount DESC, RoomNumber ASC LIMIT ?"); // Added RoomNumber to ORDER BY for consistent ordering for same counts
        params.add(limit);

        String sql = sqlBuilder.toString();
        
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Set parameters
            for(int i=0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof java.sql.Date) {
                    statement.setDate(i + 1, (java.sql.Date) param);
                } else if (param instanceof Integer) {
                    statement.setInt(i + 1, (Integer) param);
                }
                // Add other types if necessary
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                frequentRooms.put(resultSet.getString("RoomNumber"), resultSet.getInt("BookingCount"));
            }
        }
        return frequentRooms;
    }

    // Report 3: Total revenue over a period
    public BigDecimal getTotalRevenueByPeriod(Date startDate, Date endDate) throws SQLException {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        String sql = "SELECT SUM(TotalAmount) as TotalRevenue FROM Reservations WHERE CheckIn <= ? AND CheckOut >= ?";
        
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, new java.sql.Date(endDate.getTime()));
            statement.setDate(2, new java.sql.Date(startDate.getTime()));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                BigDecimal revenue = resultSet.getBigDecimal("TotalRevenue");
                if (revenue != null) {
                    totalRevenue = revenue;
                }
            }
        }
        return totalRevenue;
    }
}