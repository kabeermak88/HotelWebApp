<%-- File: WebContent/reservationdisplay.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- For date formatting --%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Reservations</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; color: #333; }
        .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { color: #0056b3; text-align: center; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #007bff; color: white; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        .actions a { margin-right: 8px; text-decoration: none; }
        .edit-link { color: #28a745; }
        .delete-link { color: #dc3545; }
        .back-link, .add-link {
            display: inline-block;
            margin-top: 20px;
            margin-right: 10px;
            padding: 10px 15px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .back-link:hover, .add-link:hover {
            background-color: #0056b3;
        }
        .message { padding: 10px; margin-bottom: 15px; border-radius: 4px; text-align: center;}
        .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
    </style>
</head>
<body>
    <div class="container">
        <h2>All Reservations</h2>

        <%-- Display message from servlet (e.g., after add/update/delete) --%>
        <c:if test="${not empty param.message}">
            <p class="message success"><c:out value="${param.message}" /></p>
        </c:if>


        <a href="index.jsp" class="back-link">Back to Home</a>
        <a href="reservationadd.jsp" class="add-link">Add New Reservation</a>

        <c:choose>
            <c:when test="${not empty listReservation}">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Customer Name</th>
                            <th>Room No.</th>
                            <th>Check-In</th>
                            <th>Check-Out</th>
                            <th>Total Amount</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="res" items="${listReservation}">
                            <tr>
                                <td><c:out value="${res.reservationID}" /></td>
                                <td><c:out value="${res.customerName}" /></td>
                                <td><c:out value="${res.roomNumber}" /></td>
                                <td><fmt:formatDate value="${res.checkIn}" pattern="yyyy-MM-dd" /></td>
                                <td><fmt:formatDate value="${res.checkOut}" pattern="yyyy-MM-dd" /></td>
                                <td><fmt:formatNumber value="${res.totalAmount}" type="currency" currencySymbol="$" /></td> <%-- Adjust currency symbol as needed --%>
                                <td class="actions">
                                    <a href="ReservationServlet?action=showEditForm&id=<c:out value='${res.reservationID}'/>" class="edit-link">Edit</a>
                                    <a href="ReservationServlet?action=delete&id=<c:out value='${res.reservationID}'/>" class="delete-link" onclick="return confirm('Are you sure you want to delete this reservation?');">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p style="text-align:center; margin-top:20px;">No reservations found.</p>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>html>