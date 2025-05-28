// File: src/com/hotel/servlet/ReservationServlet.java
package com.hotel.servlet;

import com.hotel.dao.ReservationDAO;
import com.hotel.model.Reservation;

//import jakarta.servlet.RequestDispatcher; // Jakarta EE 9+
//import jakarta.servlet.ServletException; // Jakarta EE 9+
//import jakarta.servlet.annotation.WebServlet; // Jakarta EE 9+
//import jakarta.servlet.http.HttpServlet; // Jakarta EE 9+
//import jakarta.servlet.http.HttpServletRequest; // Jakarta EE 9+
//import jakarta.servlet.http.HttpServletResponse; // Jakarta EE 9+

 //If you are using older Tomcat (e.g., Tomcat 8.5 or older) with Java EE (javax.* packages):
 import javax.servlet.RequestDispatcher;
 import javax.servlet.ServletException;
 import javax.servlet.annotation.WebServlet;
 import javax.servlet.http.HttpServlet;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/ReservationServlet") // Defines the base URL for this servlet
public class ReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReservationDAO reservationDAO;

    public void init() {
        reservationDAO = new ReservationDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Forward POST requests to doGet for simplicity here
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Default action
        }

        try {
            switch (action) {
                case "add":
                    addReservation(request, response);
                    break;
                case "list":
                    listReservations(request, response);
                    break;
                case "showEditForm":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateReservation(request, response);
                    break;
                case "delete":
                    deleteReservation(request, response);
                    break;
                // Add cases for other actions like "edit", "delete", "showNewForm"
                default:
                    listReservations(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error: " + ex.getMessage(), ex);
        } catch (ParseException ex) {
            throw new ServletException("Date parsing error: " + ex.getMessage(), ex);
        }
    }

    private void listReservations(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Reservation> listReservation = reservationDAO.getAllReservations();
        request.setAttribute("listReservation", listReservation);
        RequestDispatcher dispatcher = request.getRequestDispatcher("reservationdisplay.jsp"); // We'll create this JSP next
        dispatcher.forward(request, response);
    }

    private void addReservation(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException, ServletException {
        String customerName = request.getParameter("customerName");
        String roomNumber = request.getParameter("roomNumber");
        String checkInStr = request.getParameter("checkIn");
        String checkOutStr = request.getParameter("checkOut");
        String totalAmountStr = request.getParameter("totalAmount");

        // Basic Validation (Server-side)
        if (customerName == null || customerName.trim().isEmpty() ||
            roomNumber == null || roomNumber.trim().isEmpty() ||
            checkInStr == null || checkInStr.trim().isEmpty() ||
            checkOutStr == null || checkOutStr.trim().isEmpty() ||
            totalAmountStr == null || totalAmountStr.trim().isEmpty()) {

            request.setAttribute("error", "All fields are required.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("reservationadd.jsp");
            dispatcher.forward(request, response);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date checkIn = null;
        Date checkOut = null;
        BigDecimal totalAmount = null;

        try {
            checkIn = sdf.parse(checkInStr);
            checkOut = sdf.parse(checkOutStr);
        } catch (ParseException e) {
            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("reservationadd.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (checkOut.before(checkIn)) {
            request.setAttribute("error", "Check-out date cannot be before check-in date.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("reservationadd.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            totalAmount = new BigDecimal(totalAmountStr);
            if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
                 request.setAttribute("error", "Total amount cannot be negative.");
                 RequestDispatcher dispatcher = request.getRequestDispatcher("reservationadd.jsp");
                 dispatcher.forward(request, response);
                 return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid total amount. Please enter a valid number.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("reservationadd.jsp");
            dispatcher.forward(request, response);
            return;
        }


        Reservation newReservation = new Reservation();
        newReservation.setCustomerName(customerName);
        newReservation.setRoomNumber(roomNumber);
        newReservation.setCheckIn(checkIn);
        newReservation.setCheckOut(checkOut);
        newReservation.setTotalAmount(totalAmount);

        boolean success = reservationDAO.addReservation(newReservation);

        if (success) {
            // Option 1: Redirect to list page (PRG pattern - Post/Redirect/Get)
             response.sendRedirect("ReservationServlet?action=list&message=Reservation+added+successfully");

            // Option 2: Forward back to add form with a success message
            // request.setAttribute("message", "Reservation added successfully!");
            // RequestDispatcher dispatcher = request.getRequestDispatcher("reservationadd.jsp");
            // dispatcher.forward(request, response);
        } else {
            request.setAttribute("error", "Failed to add reservation. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("reservationadd.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Reservation existingReservation = reservationDAO.getReservationById(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("reservationupdate.jsp"); // We'll create this
        request.setAttribute("reservation", existingReservation);
        dispatcher.forward(request, response);
    }

    private void updateReservation(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException, ServletException {
        int id = Integer.parseInt(request.getParameter("reservationID")); // Ensure this name matches the form
        String customerName = request.getParameter("customerName");
        String roomNumber = request.getParameter("roomNumber");
        String checkInStr = request.getParameter("checkIn");
        String checkOutStr = request.getParameter("checkOut");
        String totalAmountStr = request.getParameter("totalAmount");

        // Basic Validation (Server-side)
        if (customerName == null || customerName.trim().isEmpty() ||
            roomNumber == null || roomNumber.trim().isEmpty() ||
            checkInStr == null || checkInStr.trim().isEmpty() ||
            checkOutStr == null || checkOutStr.trim().isEmpty() ||
            totalAmountStr == null || totalAmountStr.trim().isEmpty()) {

            request.setAttribute("error", "All fields are required.");
            // Reload the edit form with the error and existing data
            Reservation existingReservation = reservationDAO.getReservationById(id); // Re-fetch to preserve state if needed
            request.setAttribute("reservation", existingReservation); 
            RequestDispatcher dispatcher = request.getRequestDispatcher("reservationupdate.jsp");
            dispatcher.forward(request, response);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date checkIn = null;
        Date checkOut = null;
        BigDecimal totalAmount = null;
        
        try {
            checkIn = sdf.parse(checkInStr);
            checkOut = sdf.parse(checkOutStr);
        } catch (ParseException e) {
            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD.");
             Reservation existingReservation = reservationDAO.getReservationById(id);
            request.setAttribute("reservation", existingReservation);
            RequestDispatcher dispatcher = request.getRequestDispatcher("reservationupdate.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (checkOut.before(checkIn)) {
            request.setAttribute("error", "Check-out date cannot be before check-in date.");
            Reservation existingReservation = reservationDAO.getReservationById(id);
            request.setAttribute("reservation", existingReservation);
            RequestDispatcher dispatcher = request.getRequestDispatcher("reservationupdate.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        try {
            totalAmount = new BigDecimal(totalAmountStr);
             if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
                 request.setAttribute("error", "Total amount cannot be negative.");
                 Reservation existingReservation = reservationDAO.getReservationById(id);
                 request.setAttribute("reservation", existingReservation);
                 RequestDispatcher dispatcher = request.getRequestDispatcher("reservationupdate.jsp");
                 dispatcher.forward(request, response);
                 return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid total amount. Please enter a valid number.");
            Reservation existingReservation = reservationDAO.getReservationById(id);
            request.setAttribute("reservation", existingReservation);
            RequestDispatcher dispatcher = request.getRequestDispatcher("reservationupdate.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Reservation reservation = new Reservation(id, customerName, roomNumber, checkIn, checkOut, totalAmount);
        boolean success = reservationDAO.updateReservation(reservation);
        
        if(success) {
            response.sendRedirect("ReservationServlet?action=list&message=Reservation+updated+successfully");
        } else {
            request.setAttribute("error", "Failed to update reservation.");
            Reservation existingReservation = reservationDAO.getReservationById(id); // Re-fetch for consistency
            request.setAttribute("reservation", existingReservation);
            RequestDispatcher dispatcher = request.getRequestDispatcher("reservationupdate.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void deleteReservation(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = reservationDAO.deleteReservation(id);
        String message;
        if(success) {
            message = "Reservation+deleted+successfully";
        } else {
            message = "Failed+to+delete+reservation"; // or use an error parameter
        }
        response.sendRedirect("ReservationServlet?action=list&message=" + message);
    }
}