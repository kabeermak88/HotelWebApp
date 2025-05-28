<%-- File: WebContent/reservationupdate.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Reservation</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; color: #333; }
        .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 500px; margin: auto; }
        h2 { color: #0056b3; text-align: center;}
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="date"], input[type="number"], input[type="hidden"] {
            width: calc(100% - 22px);
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="submit"], .back-link {
            background-color: #28a745; /* Green for update */
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
            display: inline-block;
            margin-right: 10px;
        }
        .back-link { background-color: #6c757d; } /* Grey for back */
        input[type="submit"]:hover { background-color: #218838; }
        .back-link:hover { background-color: #5a6268; }

        .message { padding: 10px; margin-bottom: 15px; border-radius: 4px; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Update Reservation</h2>

        <%-- Display error messages if any --%>
        <c:if test="${not empty requestScope.error}">
            <p class="message error"><c:out value="${requestScope.error}" /></p>
        </c:if>

        <c:if test="${reservation != null}">
            <form action="ReservationServlet?action=update" method="post">
                <input type="hidden" name="reservationID" value="<c:out value='${reservation.reservationID}' />">
                
                <div>
                    <label for="customerName">Customer Name:</label>
                    <input type="text" id="customerName" name="customerName" value="<c:out value='${reservation.customerName}' />" required>
                </div>
                <div>
                    <label for="roomNumber">Room Number:</label>
                    <input type="text" id="roomNumber" name="roomNumber" value="<c:out value='${reservation.roomNumber}' />" required>
                </div>
                <div>
                    <label for="checkIn">Check-In Date:</label>
                    <input type="date" id="checkIn" name="checkIn" value="<fmt:formatDate value='${reservation.checkIn}' pattern='yyyy-MM-dd' />" required>
                </div>
                <div>
                    <label for="checkOut">Check-Out Date:</label>
                    <input type="date" id="checkOut" name="checkOut" value="<fmt:formatDate value='${reservation.checkOut}' pattern='yyyy-MM-dd' />" required>
                </div>
                <div>
                    <label for="totalAmount">Total Amount:</label>
                    <input type="number" id="totalAmount" name="totalAmount" value="<c:out value='${reservation.totalAmount}' />" step="0.01" min="0" required>
                </div>
                <div>
                    <input type="submit" value="Update Reservation">
                    <a href="ReservationServlet?action=list" class="back-link">Cancel</a>
                </div>
            </form>
        </c:if>
        <c:if test="${reservation == null && empty requestScope.error}">
            <p>Reservation not found or cannot be edited.</p>
            <a href="ReservationServlet?action=list" class="back-link">Back to List</a>
        </c:if>
    </div>
</body>
</html>