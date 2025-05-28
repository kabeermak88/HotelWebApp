package com.hotel.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//File: src/com/hotel/servlet/ReportServlet.java
//package com.hotel.servlet;

import com.hotel.dao.ReservationDAO;
import com.hotel.model.Reservation; // For one of the reports


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;
 private ReservationDAO reservationDAO;

 public void init() {
     reservationDAO = new ReservationDAO();
 }

 protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
     String reportType = request.getParameter("reportType");
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     String errorRedirectPage = "report_form.jsp?reportType=" + reportType; // For redirecting back with error

     try {
         switch (reportType) {
             case "dateRange":
                 generateDateRangeReport(request, response, sdf, errorRedirectPage);
                 break;
             case "frequentRooms":
                 generateFrequentRoomsReport(request, response, sdf, errorRedirectPage);
                 break;
             case "revenuePeriod":
                 generateRevenueReport(request, response, sdf, errorRedirectPage);
                 break;
             default:
                 request.setAttribute("errorMessage", "Invalid report type selected.");
                 RequestDispatcher dispatcher = request.getRequestDispatcher("reports.jsp");
                 dispatcher.forward(request, response);
                 break;
         }
     } catch (SQLException ex) {
         request.setAttribute("errorMessage", "Database error generating report: " + ex.getMessage());
         RequestDispatcher dispatcher = request.getRequestDispatcher("report_result.jsp"); // Show error on result page
         dispatcher.forward(request, response);
     } catch (ParseException ex) {
         request.setAttribute("error", "Invalid date format submitted. Please use YYYY-MM-DD."); // attribute for report_form.jsp
         RequestDispatcher dispatcher = request.getRequestDispatcher(errorRedirectPage);
         dispatcher.forward(request, response);
     }
 }
 
 private void generateDateRangeReport(HttpServletRequest request, HttpServletResponse response, SimpleDateFormat sdf, String errorRedirectPage)
         throws ServletException, IOException, SQLException, ParseException {
     String startDateStr = request.getParameter("startDate");
     String endDateStr = request.getParameter("endDate");

     if (startDateStr == null || startDateStr.isEmpty() || endDateStr == null || endDateStr.isEmpty()) {
         request.setAttribute("error", "Start Date and End Date are required for this report.");
         RequestDispatcher dispatcher = request.getRequestDispatcher(errorRedirectPage);
         dispatcher.forward(request, response);
         return;
     }

     Date startDate = sdf.parse(startDateStr);
     Date endDate = sdf.parse(endDateStr);

     if (endDate.before(startDate)) {
         request.setAttribute("error", "End Date cannot be before Start Date.");
         RequestDispatcher dispatcher = request.getRequestDispatcher(errorRedirectPage);
         dispatcher.forward(request, response);
         return;
     }

     List<Reservation> reservations = reservationDAO.getReservationsByDateRange(startDate, endDate);
     request.setAttribute("reportTitle", "Reservations from " + startDateStr + " to " + endDateStr);
     request.setAttribute("reportType", "dateRange");
     request.setAttribute("reportData_Reservations", reservations);
     RequestDispatcher dispatcher = request.getRequestDispatcher("report_result.jsp");
     dispatcher.forward(request, response);
 }

 private void generateFrequentRoomsReport(HttpServletRequest request, HttpServletResponse response, SimpleDateFormat sdf, String errorRedirectPage)
         throws ServletException, IOException, SQLException, ParseException {
     String limitStr = request.getParameter("limit");
     String startDateStr = request.getParameter("startDate"); // Optional
     String endDateStr = request.getParameter("endDate");   // Optional
     
     int limit = 5; // Default limit
     if (limitStr != null && !limitStr.isEmpty()) {
         try {
             limit = Integer.parseInt(limitStr);
             if (limit <= 0) {
                 request.setAttribute("error", "Number of rooms must be a positive integer.");
                 RequestDispatcher dispatcher = request.getRequestDispatcher(errorRedirectPage);
                 dispatcher.forward(request, response);
                 return;
             }
         } catch (NumberFormatException e) {
              request.setAttribute("error", "Invalid number for 'Number of rooms'.");
              RequestDispatcher dispatcher = request.getRequestDispatcher(errorRedirectPage);
              dispatcher.forward(request, response);
              return;
         }
     }

     Date startDate = null;
     Date endDate = null;

     if (startDateStr != null && !startDateStr.isEmpty()) {
         startDate = sdf.parse(startDateStr);
     }
     if (endDateStr != null && !endDateStr.isEmpty()) {
         endDate = sdf.parse(endDateStr);
     }

     if (startDate != null && endDate != null && endDate.before(startDate)) {
         request.setAttribute("error", "End Date cannot be before Start Date for the optional date filter.");
         RequestDispatcher dispatcher = request.getRequestDispatcher(errorRedirectPage);
         dispatcher.forward(request, response);
         return;
     }


     Map<String, Integer> frequentRooms = reservationDAO.getMostFrequentRooms(limit, startDate, endDate);
     String title = "Top " + limit + " Most Frequently Booked Rooms";
     if(startDate != null && endDate != null) title += " (from " + startDateStr + " to " + endDateStr + ")";
     else if (startDate != null) title += " (from " + startDateStr + ")";
     else if (endDate != null) title += " (until " + endDateStr + ")";

     request.setAttribute("reportTitle", title);
     request.setAttribute("reportType", "frequentRooms");
     request.setAttribute("reportData_FrequentRooms", frequentRooms);
     RequestDispatcher dispatcher = request.getRequestDispatcher("report_result.jsp");
     dispatcher.forward(request, response);
 }

 private void generateRevenueReport(HttpServletRequest request, HttpServletResponse response, SimpleDateFormat sdf, String errorRedirectPage)
         throws ServletException, IOException, SQLException, ParseException {
     String startDateStr = request.getParameter("startDate");
     String endDateStr = request.getParameter("endDate");
     
     if (startDateStr == null || startDateStr.isEmpty() || endDateStr == null || endDateStr.isEmpty()) {
         request.setAttribute("error", "Start Date and End Date are required for this report.");
         RequestDispatcher dispatcher = request.getRequestDispatcher(errorRedirectPage);
         dispatcher.forward(request, response);
         return;
     }

     Date startDate = sdf.parse(startDateStr);
     Date endDate = sdf.parse(endDateStr);

     if (endDate.before(startDate)) {
         request.setAttribute("error", "End Date cannot be before Start Date.");
         RequestDispatcher dispatcher = request.getRequestDispatcher(errorRedirectPage);
         dispatcher.forward(request, response);
         return;
     }

     BigDecimal totalRevenue = reservationDAO.getTotalRevenueByPeriod(startDate, endDate);
     request.setAttribute("reportTitle", "Total Revenue Report");
     request.setAttribute("reportType", "revenuePeriod");
     request.setAttribute("reportData_TotalRevenue", totalRevenue);
     request.setAttribute("startDate", startDate); // For display on result page
     request.setAttribute("endDate", endDate);   // For display on result page
     RequestDispatcher dispatcher = request.getRequestDispatcher("report_result.jsp");
     dispatcher.forward(request, response);
 }


 // doGet can be added if you want to allow GET requests for reports,
 // but POST is generally better for forms with parameters.
 protected void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
     // Optionally, you could redirect to reports.jsp or handle specific GET requests
     response.sendRedirect("reports.jsp");
 }
}