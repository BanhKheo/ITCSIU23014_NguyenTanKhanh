<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${student.id != 0}">Edit Student</c:when>
            <c:otherwise>Add New Student</c:otherwise>
        </c:choose>
    </title>
    <style>
        /* ... (Your existing CSS is great, keep it!) ... */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 10px;
            padding: 40px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 600px;
        }
        h1 { color: #333; margin-bottom: 30px; font-size: 28px; text-align: center; }
        .form-group { margin-bottom: 20px; } /* Reduced slightly to fit errors */
        label { display: block; margin-bottom: 8px; color: #555; font-weight: 500; font-size: 14px; }
        input[type="text"], input[type="email"], select {
            width: 100%; padding: 12px 15px; border: 2px solid #ddd;
            border-radius: 5px; font-size: 14px; transition: border-color 0.3s;
        }
        input:focus, select:focus { outline: none; border-color: #667eea; }
        .required-star { color: #dc3545; }
        .button-group { display: flex; gap: 15px; margin-top: 30px; }
        .btn {
            flex: 1; padding: 14px; border: none; border-radius: 5px; font-size: 16px; font-weight: 600;
            cursor: pointer; transition: all 0.3s; text-decoration: none; text-align: center; display: inline-block;
        }
        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4); }
        .btn-secondary { background-color: #6c757d; color: white; }
        .btn-secondary:hover { background-color: #5a6268; }
        .info-text { font-size: 12px; color: #666; margin-top: 5px; }

        /* --- NEW CSS FOR VALIDATION ERRORS --- */
        .error-message {
            color: #dc3545;
            font-size: 13px;
            margin-top: 5px;
            display: block;
            font-weight: 500;
        }
        .input-error {
            border-color: #dc3545 !important;
            background-color: #fff8f8;
        }
        /* ------------------------------------- */
    </style>
</head>
<body>
    <div class="container">

        <c:if test="${not empty error}">
            <div style="background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px; margin-bottom: 20px; text-align: center;">
                ⚠️ <c:out value="${error}"/>
            </div>
        </c:if>

        <h1>
            <c:choose>
                <c:when test="${not empty student and student.id != 0}">✏️ Edit Student</c:when>
                <c:otherwise>➕ Add New Student</c:otherwise>
            </c:choose>
        </h1>

        <form action="student" method="POST">
            <input type="hidden" name="action"
                   value="${(not empty student and student.id != 0) ? 'update' : 'insert'}">

            <c:if test="${not empty student and student.id != 0}">
                <input type="hidden" name="id" value="${student.id}">
            </c:if>

            <div class="form-group">
                <label for="studentCode">Student Code <span class="required-star">*</span></label>

                <input type="text"
                       id="studentCode"
                       name="studentCode"
                       value="<c:out value='${student.studentCode}'/>"
                       ${(not empty student and student.id != 0) ? 'readonly' : 'required'}
                       placeholder="e.g., SV001, IT123"
                       class="${not empty errorCode ? 'input-error' : ''}">

                <p class="info-text">Format: 2 uppercase letters + 3 digits</p>

                <c:if test="${not empty errorCode}">
                    <span class="error-message">❌ ${errorCode}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="fullName">Full Name <span class="required-star">*</span></label>
                <input type="text"
                       id="fullName"
                       name="fullName"
                       value="<c:out value='${student.fullName}'/>"
                       required
                       placeholder="Enter full name"
                       class="${not empty errorName ? 'input-error' : ''}">

                <c:if test="${not empty errorName}">
                    <span class="error-message">❌ ${errorName}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email"
                       id="email"
                       name="email"
                       value="<c:out value='${student.email}'/>"
                       placeholder="student@example.com (Optional)"
                       class="${not empty errorEmail ? 'input-error' : ''}">

                <c:if test="${not empty errorEmail}">
                    <span class="error-message">❌ ${errorEmail}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="major">Major <span class="required-star">*</span></label>
                <select id="major" name="major" required class="${not empty errorMajor ? 'input-error' : ''}">
                    <option value="">-- Select Major --</option>

                    <option value="Computer Science" ${student.major == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                    <option value="Information Technology" ${student.major == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                    <option value="Software Engineering" ${student.major == 'Software Engineering' ? 'selected' : ''}>Software Engineering</option>
                    <option value="Business Administration" ${student.major == 'Business Administration' ? 'selected' : ''}>Business Administration</option>
                </select>

                <c:if test="${not empty errorMajor}">
                    <span class="error-message">❌ ${errorMajor}</span>
                </c:if>
            </div>

            <div class="button-group">
                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${not empty student and student.id != 0}">💾 Update Student</c:when>
                        <c:otherwise>➕ Add Student</c:otherwise>
                    </c:choose>
                </button>

                <a href="student?action=list" class="btn btn-secondary">❌ Cancel</a>
            </div>

        </form>
    </div>
</body>
</html>