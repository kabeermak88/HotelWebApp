

<%-- File: WebContent/index.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Management System</title>
    <!-- Google Fonts for modern typography -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <!-- Font Awesome for icons (optional) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        :root {
            --primary-color: #4361ee;
            --primary-dark: #3a0ca3;
            --background-light: #f8f9fa;
            --text-dark: #2b2d42;
            --text-light: #8d99ae;
            --white: #ffffff;
            --shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        body {
            font-family: 'Poppins', sans-serif;
            margin: 0;
            padding: 0;
            background-color: var(--background-light);
            color: var(--text-dark);
            line-height: 1.6;
        }

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 30px;
            background-color: var(--white);
            border-radius: 12px;
            box-shadow: var(--shadow);
        }

        h1 {
            color: var(--primary-dark);
            text-align: center;
            margin-bottom: 30px;
            font-weight: 600;
            font-size: 2.5rem;
            position: relative;
            padding-bottom: 15px;
        }

        h1::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 4px;
            background: var(--primary-color);
            border-radius: 2px;
        }

        .nav-menu {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 40px;
        }

        .nav-menu a {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 12px 24px;
            background-color: var(--primary-color);
            color: var(--white);
            border-radius: 8px;
            font-weight: 500;
            text-decoration: none;
            transition: all 0.3s ease;
            box-shadow: var(--shadow);
            min-width: 200px;
            text-align: center;
        }

        .nav-menu a:hover {
            background-color: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
        }

        .nav-menu a i {
            margin-right: 8px;
        }

        /* Responsive adjustments */
        @media (max-width: 768px) {
            .container {
                margin: 20px;
                padding: 20px;
            }

            h1 {
                font-size: 2rem;
            }

            .nav-menu {
                flex-direction: column;
                align-items: center;
            }

            .nav-menu a {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Hotel Management System</h1>

        <div class="nav-menu">
            <a href="reservationadd.jsp">
                <i class="fas fa-plus-circle"></i> Add New Reservation
            </a>
            <a href="ReservationServlet?action=list">
                <i class="fas fa-list"></i> View All Reservations
            </a>
            <a href="reports.jsp">
                <i class="fas fa-chart-bar"></i> View Reports
            </a>
        </div>
    </div>
</body>
</html>