<%-- File: WebContent/reports.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hotel Reports</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; color: #333; }
        .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 600px; margin: auto; }
        h2 { color: #0056b3; text-align: center; }
        ul { list-style-type: none; padding: 0; }
        li { margin-bottom: 10px; padding: 10px; background-color: #e9ecef; border-radius: 4px; }
        li a { text-decoration: none; color: #007bff; font-weight: bold; }
        li a:hover { color: #0056b3; }
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
    </style>
</head>
<body>
    <div class="container">
        <h2>Generate Hotel Reports</h2>
        <ul>
            <li><a href="ReportCriteriaServlet?reportType=dateRange">Reservations in a Date Range</a></li>
            <li><a href="ReportCriteriaServlet?reportType=frequentRooms">Rooms Booked Most Frequently</a></li>
            <li><a href="ReportCriteriaServlet?reportType=revenuePeriod">Total Revenue Over a Period</a></li>
        </ul>
        <a href="index.jsp" class="back-link">Back to Home</a>
    </div>
</body>
</html>