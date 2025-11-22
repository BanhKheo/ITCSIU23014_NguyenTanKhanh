<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student List - MVC</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }

        h1 { color: #333; margin-bottom: 10px; font-size: 32px; }
        .subtitle { color: #666; margin-bottom: 30px; font-style: italic; }

        /* Messages */
        .message { padding: 15px; margin-bottom: 20px; border-radius: 5px; font-weight: 500; }
        .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }

        /* Buttons */
        .btn { display: inline-block; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: 500; transition: all 0.3s; border: none; cursor: pointer; font-size: 14px; }
        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4); }
        .btn-secondary { background-color: #6c757d; color: white; }
        .btn-danger { background-color: #dc3545; color: white; padding: 8px 16px; font-size: 13px; }
        .btn-danger:hover { background-color: #c82333; }

        /* Controls Area (Add + Filter + Search) */
        .controls-area {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            margin-bottom: 20px;
            align-items: center;
            justify-content: space-between;
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
        }

        .filter-form, .search-form {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        input[type="text"], select {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        /* Table */
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        thead { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        th, td { padding: 15px; text-align: left; border-bottom: 1px solid #ddd; }

        /* Sortable Headers */
        th a { color: white; text-decoration: none; display: flex; align-items: center; gap: 5px; }
        th a:hover { text-decoration: underline; }

        tbody tr { transition: background-color 0.2s; }
        tbody tr:hover { background-color: #f8f9fa; }

        .actions { display: flex; gap: 10px; }
        .empty-state { text-align: center; padding: 60px 20px; color: #999; }
        .empty-state-icon { font-size: 64px; margin-bottom: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>📚 Student Management System</h1>
        <p class="subtitle">MVC Pattern with Jakarta EE & JSTL</p>

        <c:if test="${not empty param.message}">
            <div class="message success">✅ ${param.message}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="message error">❌ ${param.error}</div>
        </c:if>

        <div class="controls-area">

            <div>
                <a href="student?action=new" class="btn btn-primary">➕ Add New Student</a>
            </div>

            <form action="student" method="GET" class="filter-form">
                <input type="hidden" name="action" value="filter">

                <input type="hidden" name="keyword" value="<c:out value='${keyword}'/>">
                <input type="hidden" name="sortBy" value="${sortBy}">
                <input type="hidden" name="order" value="${order}">

                <label><strong>Major:</strong></label>
                <select name="major" onchange="this.form.submit()">
                    <option value="">-- All Majors --</option>
                    <option value="Computer Science" ${selectedMajor == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                    <option value="Information Technology" ${selectedMajor == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                    <option value="Software Engineering" ${selectedMajor == 'Software Engineering' ? 'selected' : ''}>Software Engineering</option>
                    <option value="Business Administration" ${selectedMajor == 'Business Administration' ? 'selected' : ''}>Business Administration</option>
                </select>

                <c:if test="${not empty selectedMajor}">
                    <a href="student?action=list&keyword=<c:out value='${keyword}'/>" style="color: #dc3545; text-decoration: none; font-size: 0.9em;">
                        ❌ Clear
                    </a>
                </c:if>
            </form>

            <form action="student" method="GET" class="search-form">
                <input type="hidden" name="action" value="search">

                <input type="hidden" name="major" value="${selectedMajor}">
                <input type="hidden" name="sortBy" value="${sortBy}">
                <input type="hidden" name="order" value="${order}">

                <input type="text" name="keyword" placeholder="Search name/code..." value="<c:out value='${keyword}'/>">
                <button type="submit" class="btn btn-secondary" style="padding: 8px 12px;">🔍</button>

                <c:if test="${not empty keyword}">
                    <a href="student?action=list&major=${selectedMajor}" style="color: #dc3545; text-decoration: none; font-size: 0.9em;">
                        ❌ Clear
                    </a>
                </c:if>
            </form>
        </div>

        <c:if test="${not empty keyword}">
            <p style="margin-bottom: 15px; color: #555;">
                Found results for: <strong><c:out value="${keyword}"/></strong>
            </p>
        </c:if>

        <c:choose>
            <c:when test="${not empty students}">
                <table>
                    <thead>
                        <tr>
                            <th>
                                <a href="student?action=sort&sortBy=id&order=${sortBy == 'id' && order == 'asc' ? 'desc' : 'asc'}&major=${selectedMajor}&keyword=${keyword}">
                                    ID <c:if test="${sortBy == 'id'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                                </a>
                            </th>
                            <th>
                                <a href="student?action=sort&sortBy=student_code&order=${sortBy == 'student_code' && order == 'asc' ? 'desc' : 'asc'}&major=${selectedMajor}&keyword=${keyword}">
                                    Code <c:if test="${sortBy == 'student_code'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                                </a>
                            </th>
                            <th>
                                <a href="student?action=sort&sortBy=full_name&order=${sortBy == 'full_name' && order == 'asc' ? 'desc' : 'asc'}&major=${selectedMajor}&keyword=${keyword}">
                                    Full Name <c:if test="${sortBy == 'full_name'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                                </a>
                            </th>
                            <th>Email</th>
                            <th>
                                <a href="student?action=sort&sortBy=major&order=${sortBy == 'major' && order == 'asc' ? 'desc' : 'asc'}&major=${selectedMajor}&keyword=${keyword}">
                                    Major <c:if test="${sortBy == 'major'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                                </a>
                            </th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="student" items="${students}">
                            <tr>
                                <td>${student.id}</td>
                                <td><strong><c:out value="${student.studentCode}"/></strong></td>
                                <td><c:out value="${student.fullName}"/></td>
                                <td><c:out value="${student.email != null ? student.email : 'N/A'}"/></td>
                                <td><c:out value="${student.major}"/></td>
                                <td>
                                    <div class="actions">
                                        <a href="student?action=edit&id=${student.id}" class="btn btn-secondary" style="font-size: 12px; padding: 6px 12px;">
                                            ✏️ Edit
                                        </a>
                                        <a href="student?action=delete&id=${student.id}"
                                           class="btn btn-danger"
                                           style="font-size: 12px; padding: 6px 12px;"
                                           onclick="return confirm('Are you sure you want to delete this student?')">
                                            🗑️ Delete
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <h3>No students found</h3>
                    <c:if test="${not empty keyword or not empty selectedMajor}">
                        <p>Try clearing your search or filter.</p>
                        <a href="student?action=list" class="btn btn-secondary" style="margin-top: 10px;">Reset View</a>
                    </c:if>
                    <c:if test="${empty keyword and empty selectedMajor}">
                        <p>Start by adding a new student.</p>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>