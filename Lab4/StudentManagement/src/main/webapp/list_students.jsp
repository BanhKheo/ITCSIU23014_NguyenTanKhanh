<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        h1 { color: #333; }

        /* ✅ (a) Enhanced Message Styling */
        .message {
            padding: 12px 16px;
            margin-bottom: 20px;
            border-radius: 5px;
            display: flex;
            align-items: center;
            gap: 8px;
            font-weight: bold;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            border-left: 5px solid #28a745;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border-left: 5px solid #dc3545;
        }

        /* ✅ Table Responsive Enhancement */
        .table-responsive {
            overflow-x: auto;
            background-color: white;
            border-radius: 8px;
        }

        .btn {
            display: inline-block;
            padding: 10px 20px;
            margin-bottom: 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .btn:hover { background-color: #0056b3; }

        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
        }
        th {
            background-color: #007bff;
            color: white;
            padding: 12px;
            text-align: left;
        }
        td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }
        tr:hover { background-color: #f8f9fa; }

        .action-link {
            color: #007bff;
            text-decoration: none;
            margin-right: 10px;
        }
        .delete-link { color: #dc3545; }

        /* ✅ (c) Responsive Design */
        @media (max-width: 768px) {
            table { font-size: 12px; }
            th, td { padding: 6px; }
        }
    </style>
</head>
<body>
    <h1>📚 Student Management System</h1>

    <% if (request.getParameter("message") != null) { %>
        <!-- ✅ Added ✓ icon -->
        <div class="message success">✓ <%= request.getParameter("message") %></div>
    <% } %>

    <% if (request.getParameter("error") != null) { %>
        <!-- ✅ Added ✗ icon -->
        <div class="message error">✗ <%= request.getParameter("error") %></div>
    <% } %>

    <a href="add_student.jsp" class="btn">➕ Add New Student</a>

    <!-- ✅ Added loading prevention for double submission -->
    <form action="list_students.jsp" method="GET" onsubmit="return submitForm(this)">
        <input type="text" name="keyword" placeholder="Search by name or code...">
        <button type="submit">Search</button>
        <a href="list_students.jsp">Clear</a>
    </form>

    <!-- ✅ Added responsive wrapper -->
    <div class="table-responsive">
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Student Code</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Major</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
<%
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    int totalRecords = 0;
    int totalPages = 0;
    int recordsPerPage = 10;
    int currentPage = 1;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/student_management",
            "root",
            "khanhdepzai16"
        );

        String keyword = request.getParameter("keyword");
        String pageParam = request.getParameter("page");
        if (pageParam != null) currentPage = Integer.parseInt(pageParam);

        int offset = (currentPage - 1) * recordsPerPage;

        // Count total records
        String countSql = (keyword != null && !keyword.isEmpty()) ?
            "SELECT COUNT(*) FROM students WHERE full_name LIKE ? OR student_code LIKE ?" :
            "SELECT COUNT(*) FROM students";
        pstmt = conn.prepareStatement(countSql);
        if (keyword != null && !keyword.isEmpty()) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
        }
        rs = pstmt.executeQuery();
        if (rs.next()) totalRecords = rs.getInt(1);
        totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        rs.close();
        pstmt.close();

        // Main query
        String sql = (keyword != null && !keyword.isEmpty()) ?
            "SELECT * FROM students WHERE full_name LIKE ? OR student_code LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?" :
            "SELECT * FROM students ORDER BY id DESC LIMIT ? OFFSET ?";
        pstmt = conn.prepareStatement(sql);

        if (keyword != null && !keyword.isEmpty()) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setInt(3, recordsPerPage);
            pstmt.setInt(4, offset);
        } else {
            pstmt.setInt(1, recordsPerPage);
            pstmt.setInt(2, offset);
        }

        rs = pstmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String studentCode = rs.getString("student_code");
            String fullName = rs.getString("full_name");
            String email = rs.getString("email");
            String major = rs.getString("major");
            Timestamp createdAt = rs.getTimestamp("created_at");
%>
            <tr>
                <td><%= id %></td>
                <td><%= studentCode %></td>
                <td><%= fullName %></td>
                <td><%= email != null ? email : "N/A" %></td>
                <td><%= major != null ? major : "N/A" %></td>
                <td><%= createdAt %></td>
                <td>
                    <a href="edit_student.jsp?id=<%= id %>" class="action-link">✏️ Edit</a>
                    <a href="delete_student.jsp?id=<%= id %>"
                       class="action-link delete-link"
                       onclick="return confirm('Are you sure?')">🗑️ Delete</a>
                </td>
            </tr>
<%
        }
    } catch (ClassNotFoundException e) {
        out.println("<tr><td colspan='7'>Error: JDBC Driver not found!</td></tr>");
    } catch (SQLException e) {
        out.println("<tr><td colspan='7'>Database Error: " + e.getMessage() + "</td></tr>");
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }
%>
            </tbody>
        </table>
    </div>

    <div class="pagination">
        <% if (currentPage > 1) { %>
            <a href="list_students.jsp?page=<%= currentPage - 1 %>">Previous</a>
        <% } %>
        <% for (int i = 1; i <= totalPages; i++) { %>
            <% if (i == currentPage) { %>
                <strong><%= i %></strong>
            <% } else { %>
                <a href="list_students.jsp?page=<%= i %>"><%= i %></a>
            <% } %>
        <% } %>
        <% if (currentPage < totalPages) { %>
            <a href="list_students.jsp?page=<%= currentPage + 1 %>">Next</a>
        <% } %>
    </div>

    <!-- ✅ (a) Auto-hide messages -->
    <script>
    setTimeout(function() {
        var messages = document.querySelectorAll('.message');
        messages.forEach(function(msg) { msg.style.display = 'none'; });
    }, 3000);

    // ✅ (b) Prevent double submission
    function submitForm(form) {
        var btn = form.querySelector('button[type="submit"]');
        btn.disabled = true;
        btn.textContent = 'Processing...';
        return true;
    }
    </script>

</body>
</html>
