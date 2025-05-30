# College Fee Payment Management System  
**Subject Name**: Advanced Java  
**Subject Code**: BCS613D  
**Name**: Moideen Ahmed Kabeer  
**USN**: 4AL22CS090  
**Sem/Section**: VI/B  

A comprehensive web application for managing student fee payments built with JSP, Servlets, and MySQL following MVC architecture principles.

---

## 🚀 Features  
- **Complete CRUD Operations**: Add, Update, Delete, and Display student fee records  
- **Advanced Search**: Search student fee status by ID or name  
- **Comprehensive Reports**: Generate reports including:  
  - Students with pending dues  
  - Fully paid students  
  - Total fees collected per department  
- **Input Validation**: Client-side and server-side validation  
- **Professional UI**: Bootstrap-based responsive design  
- **MVC Architecture**: Clear separation of concerns  
- **Database Integration**: MySQL with JDBC connectivity  

---

## 📋 Prerequisites  
Ensure you have the following installed before running this project:  
- Java Development Kit (JDK) 8 or higher  
- Apache Tomcat 9.0 or higher  
- MySQL Server 5.7 or XAMPP Server  
- MySQL JDBC Driver (mysql-connector-java)  
- IDE: Eclipse (J2EE), IntelliJ IDEA, or similar  
- Web Browser: Chrome, Firefox, or Edge  

---

## 🛠️ Project Structure  




FeeManagementApp/
├── src/
│ ├── dao/
│ │ └── FeeDAO.java
│ ├── model/
│ │ └── FeeRecord.java
│ └── servlet/
│ ├── AddFeeServlet.java
│ ├── UpdateFeeServlet.java
│ ├── DeleteFeeServlet.java
│ ├── DisplayFeeServlet.java
│ ├── ReportServlet.java
│ └── ReportCriteriaServlet.java
├── WebContent/
│ ├── index.jsp
│ ├── feeadd.jsp
│ ├── feeupdate.jsp
│ ├── feedelete.jsp
│ ├── feedisplay.jsp
│ ├── reports.jsp
│ ├── report_form.jsp
│ └── report_result.jsp
├── WEB-INF/
│ └── web.xml
└── README.md


---

## 🗄️ Database Setup  

1. **Create Database**  
```sql

2. CreateTable
CREATE DATABASE IF NOT EXISTS fee_management;  
USE fee_management;  
CREATE TABLE IF NOT EXISTS StudentFees (  
    StudentID INT PRIMARY KEY,  
    StudentName VARCHAR(100) NOT NULL,  
    Department VARCHAR(50) NOT NULL,  
    TotalFees INT NOT NULL,  
    PaidAmount INT NOT NULL,  
    DueAmount INT GENERATED ALWAYS AS (TotalFees - PaidAmount) STORED,  
    PaymentDate DATE NOT NULL  
);  


3.InsertSAmple data
INSERT INTO StudentFees (StudentID, StudentName, Department, TotalFees, PaidAmount, PaymentDate) VALUES  
(2002, 'Moideen Ahmed Kabeer', 'CSE', 50000, 30000, '2025-05-02'),  
(2003, 'Aisha Khan', 'ISE', 45000, 45000, '2025-05-03'),  
(2004, 'Rohit Patil', 'ECE', 48000, 20000, '2025-05-01'),  
(2005, 'Sneha Reddy', 'ME', 40000, 40000, '2025-05-05');  


⚙️ Installation & Setup
Step 1: Clone/Download the Project
Download the project files and arrange them according to the structure above.

Step 2: Database Configuration
Start MySQL server

Execute the database setup queries

Update database credentials in FeeDAO.java:

connection = DriverManager.getConnection(  
    "jdbc:mysql://localhost:3306/fee_management",  
    "your_username",  
    "your_password"  
);  


Step 3: Add MySQL JDBC Driver
Download MySQL Connector/J

Place the .jar in WEB-INF/lib

Add it to your project’s build path in your IDE

Step 4: Deploy to Tomcat
Create a new Dynamic Web Project

Place all source files appropriately

Deploy on Apache Tomcat server

Start the server

Step 5: Access the Application
Open a browser and visit:
http://localhost:8080/FeeManagementApp/  

🖼️ Screenshots
🏠 Home Page
➕ Add Fee Record
🔍 Search Student Fees
🆕 Update Payment
❌ Delete Fee Record
📋 Display All Payments
📊 Generate Reports

🎯 Usage Application
Adding Student Fee Record

Navigate to "Add Fees"

Enter: Student ID, Name, Department, Total Fees, Paid Amount, Payment Date

Click "Add Record"

Updating Student Fee

Go to "Update Fee"

Search using Student ID

Edit fields as needed

Click "Update Record"

Deleting Fee Record

Go to "Delete Fee"

Search by ID

Confirm deletion

Click "Delete Record"

Displaying Fee Records

Go to "Display Fees"

View all or search by Student ID

Generating Reports

Navigate to "Reports"

Choose criteria:

Pending Dues

Fully Paid Students

Collection by Department

Click "Generate Report"

🔧 Technical Features
Input Validation

JavaScript-based client validation

Servlet-based backend validation

Database constraints for consistency

Error Handling

Try-catch blocks around DB operations

User-friendly error messages

Security Features

SQL injection protection using prepared statements

Session control and form validations

Responsive Design

Built with Bootstrap 5

Mobile-friendly layouts

Print-friendly reports

🧪 Testing the Application
Test Cases

Add Operations:

Add valid records

Try duplicate IDs (should fail)

Test validations (empty/invalid input)

Update Operations:

Modify existing records

Handle non-existing IDs

Delete Operations:

Delete valid entries

Handle invalid IDs

Display Operations:

Show all records

Search specific IDs

Report Generation:

Test all report types

Validate summaries

🎓 Outcomes
This project demonstrates:

MVC-based clean software architecture

Web development using JSP, Servlets, HTML, JS

Integration with MySQL using JDBC

CRUD-based full-stack app development

Secure, responsive, and professional fee management interface
