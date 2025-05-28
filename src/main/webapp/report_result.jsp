<%-- File: WebContent/report_result.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Report Results</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; color: #333; }
        .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { color: #0056b3; text-align: center; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #007bff; color: white; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 15px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .back-link:hover { background-color: #5a6268; }
        .total-revenue { font-size: 1.2em; font-weight: bold; margin-top: 15px; color: #28a745;}
    </style>
</head>
<body>
    <div class="container">
        <h2>Report: <c:out value="${reportTitle}" /></h2>

        <c:if test="${not empty errorMessage}">
             <p style="color:red; text-align:center;"><c:out value="${errorMessage}"/></p>
        </c:if>

        <%-- Report Type: Reservations in Date Range --%>
        <c:if test="${reportType == 'dateRange' && not empty reportData_Reservations}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Customer Name</th>
                        <th>Room No.</th>
                        <th>Check-In</th>
                        <th>Check-Out</th>
                        <th>Total Amount</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="res" items="${reportData_Reservations}">
                        <tr>
                            <td><c:out value="${res.reservationID}" /></td>
                            <td><c:out value="${res.customerName}" /></td>
                            <td><c:out value="${res.roomNumber}" /></td>
                            <td><fmt:formatDate value="${res.checkIn}" pattern="yyyy-MM-dd" /></td>
                            <td><fmt:formatDate value="${res.checkOut}" pattern="yyyy-MM-dd" /></td>
                            <td><fmt:formatNumber value="${res.totalAmount}" type="currency" currencySymbol="$" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${reportType == 'dateRange' && empty reportData_Reservations && empty errorMessage}">
            <p>No reservations found for the selected date range.</p>
        </c:if>

        <%-- Report Type: Rooms Booked Most Frequently --%>
        <c:if test="${reportType == 'frequentRooms' && not empty reportData_FrequentRooms}">
             <table>
                <thead>
                    <tr>
                        <th>Room Number</th>
                        <th>Booking Count</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="roomStat" items="${reportData_FrequentRooms}">
                        <tr>
                            <td><c:out value="${roomStat.key}" /></td> <%-- Assuming key is room number --%>
                            <td><c:out value="${roomStat.value}" /></td> <%-- Assuming value is count --%>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
         <c:if test="${reportType == 'frequentRooms' && empty reportData_FrequentRooms && empty errorMessage}">
            <p>No room booking data available for the selected criteria.</p>
        </c:if>

        <%-- Report Type: Total Revenue Over a Period --%>
        <c:if test="${reportType == 'revenuePeriod' && not empty reportData_TotalRevenue}">
            <p class="total-revenue">
                Total Revenue from <fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd" />
                to <fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd" />:
                <strong><fmt:formatNumber value="${reportData_TotalRevenue}" type="currency" currencySymbol="$" /></strong>
            </p>
        </c:if>
        <c:if test="${reportType == 'revenuePeriod' && empty reportData_TotalRevenue && empty errorMessage}">
             <p>No revenue data found for the selected period.</p>
        </c:if>


        <a href="reports.jsp" class="back-link">Back to Reports</a>
    </div>
</body>
</html>