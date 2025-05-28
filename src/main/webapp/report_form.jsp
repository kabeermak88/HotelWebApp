<%-- File: WebContent/report_form.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Report Criteria</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; color: #333; }
        .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 500px; margin: auto; }
        h2 { color: #0056b3; text-align: center;}
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="date"], input[type="number"] {
            width: calc(100% - 22px);
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="submit"], .back-link {
            background-color: #007bff;
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
        input[type="submit"]:hover { background-color: #0056b3; }
        .back-link { background-color: #6c757d; }
        .back-link:hover { background-color: #5a6268; }
         .message { padding: 10px; margin-bottom: 15px; border-radius: 4px; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    </style>
</head>
<body>
    <div class="container">
        <h2>
            <c:choose>
                <c:when test="${param.reportType == 'dateRange'}">Reservations by Date Range Criteria</c:when>
                <c:when test="${param.reportType == 'frequentRooms'}">Most Frequent Rooms Criteria</c:when>
                <c:when test="${param.reportType == 'revenuePeriod'}">Revenue by Period Criteria</c:when>
                <c:otherwise>Report Criteria</c:otherwise>
            </c:choose>
        </h2>

        <%-- Display error messages --%>
        <c:if test="${not empty requestScope.error}">
            <p class="message error"><c:out value="${requestScope.error}" /></p>
        </c:if>

        <form action="ReportServlet" method="post">
            <input type="hidden" name="reportType" value="<c:out value='${param.reportType}'/>">

            <c:if test="${param.reportType == 'dateRange' || param.reportType == 'revenuePeriod'}">
                <div>
                    <label for="startDate">Start Date:</label>
                    <input type="date" id="startDate" name="startDate" required>
                </div>
                <div>
                    <label for="endDate">End Date:</label>
                    <input type="date" id="endDate" name="endDate" required>
                </div>
            </c:if>

            <c:if test="${param.reportType == 'frequentRooms'}">
                 <div>
                    <label for="limit">Number of Top Rooms to Display:</label>
                    <input type="number" id="limit" name="limit" min="1" value="5" required> <%-- Default to top 5 --%>
                </div>
                <%-- For "Rooms booked most frequently", we might also add date range filters --%>
                 <div>
                    <label for="freqStartDate">Start Date (Optional):</label>
                    <input type="date" id="freqStartDate" name="startDate">
                </div>
                <div>
                    <label for="freqEndDate">End Date (Optional):</label>
                    <input type="date" id="freqEndDate" name="endDate">
                </div>
            </c:if>

            <div>
                <input type="submit" value="Generate Report">
                <a href="reports.jsp" class="back-link">Back to Reports</a>
            </div>
        </form>
    </div>
</body>
</html>