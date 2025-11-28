<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Change Password</title>
    <style>
        /* Reuse existing styles for consistency */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 450px;
        }
        h2 { text-align: center; color: #333; margin-bottom: 20px; }

        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; color: #666; font-weight: 500; }
        input[type="password"] {
            width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;
            font-size: 14px;
        }

        .btn {
            width: 100%; padding: 12px; background: #667eea; color: white; border: none;
            border-radius: 5px; font-size: 16px; cursor: pointer; margin-top: 10px;
        }
        .btn:hover { background: #5a6fd6; }

        .back-link { display: block; text-align: center; margin-top: 15px; text-decoration: none; color: #666; }
        .back-link:hover { color: #333; }

        .alert { padding: 10px; border-radius: 5px; margin-bottom: 20px; text-align: center; font-size: 14px; }
        .alert-error { background: #f8d7da; color: #721c24; }
        .alert-success { background: #d4edda; color: #155724; }
    </style>
</head>
<body>

    <div class="container">
        <h2>🔒 Change Password</h2>

        <c:if test="${not empty message}">
            <div class="alert alert-success">✅ ${message}</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-error">❌ ${error}</div>
        </c:if>

        <form action="change-password" method="post">
            <div class="form-group">
                <label>Current Password</label>
                <input type="password" name="currentPassword" required>
            </div>

            <div class="form-group">
                <label>New Password (Min 8 chars)</label>
                <input type="password" name="newPassword" required minlength="8">
            </div>

            <div class="form-group">
                <label>Confirm New Password</label>
                <input type="password" name="confirmPassword" required>
            </div>

            <button type="submit" class="btn">Update Password</button>
        </form>

        <a href="dashboard" class="back-link">← Back to Dashboard</a>
    </div>

</body>
</html>