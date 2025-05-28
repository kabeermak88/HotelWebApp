// File: src/com/hotel/model/Reservation.java
package com.hotel.model;

import java.math.BigDecimal;
import java.util.Date; // Use java.util.Date for now, will convert to java.sql.Date for DB

public class Reservation {
    private int reservationID;
    private String customerName;
    private String roomNumber;
    private Date checkIn;
    private Date checkOut;
    private BigDecimal totalAmount;

    // Constructors
    public Reservation() {
    }

    public Reservation(int reservationID, String customerName, String roomNumber, Date checkIn, Date checkOut, BigDecimal totalAmount) {
        this.reservationID = reservationID;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Reservation [reservationID=" + reservationID + ", customerName=" + customerName + ", roomNumber="
                + roomNumber + ", checkIn=" + checkIn + ", checkOut=" + checkOut + ", totalAmount=" + totalAmount
                + "]";
    }
}