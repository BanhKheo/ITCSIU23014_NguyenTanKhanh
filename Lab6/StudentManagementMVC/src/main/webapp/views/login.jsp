<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Student Management</title>
    <style>
        /* Reset and Base Styles */
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            /* Gradient Background */
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        /* Centered Login Container */
        .login-container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }

        .login-header { margin-bottom: 30px; }
        .login-header h1 { color: #333; font-size: 28px; margin-bottom: 5px; }
        .login-header p { color: #666; font-size: 14px; }

        /* Form Elements */
        .form-group { margin-bottom: 20px; text-align: left; }

        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
            font-size: 14px;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #eee;
            border-radius: 5px;
            font-size: 15px;
            transition: all 0.3s ease;
        }

        /* Input Focus Effect */
        input[type="text"]:focus, input[type="password"]:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            outline: none;
        }

        /* Submit Button with Hover Effect */
        .btn-login {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-login:active { transform: translateY(0); }

        /* Alerts */
        .alert {
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 14px;
            text-align: left;
        }
        .alert-error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .alert-success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }

        /* Demo Credentials Box */
        .demo-credentials {
            margin-top: 25px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
            border-left: 4px solid #667eea;
            text-align: left;
            font-size: 13px;
            color: #555;
        }
        .demo-credentials h4 { margin-bottom: 5px; color: #333; }
        .demo-credentials p { margin-bottom: 2px; }

        /* Utilities */
        .text-center { text-align: center; }
        .checkbox-group { display: flex; align-items: center; gap: 8px; font-size: 14px; color: #666; }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <h1>🔐 Login</h1>
            <p>Student Management System</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ❌ <c:out value="${error}"/>
            </div>
        </c:if>

        <c:if test="${not empty param.message}">
            <div class="alert alert-success">
                ✅ <c:out value="${param.message}"/>
            </div>
        </c:if>

        <form action="login" method="post">

            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username"
                       placeholder="Enter username" required autofocus
                       value="<c:out value='${param.username}'/>"> </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password"
                       placeholder="Enter password" required>
            </div>

            <div class="form-group checkbox-group">
                <input type="checkbox" id="remember" name="remember">
                <label for="remember" style="margin:0; font-weight:normal;">Remember me</label>
            </div>

            <button type="submit" class="btn-login">Sign In</button>
        </form>

        <div class="demo-credentials">
            <h4>💡 Demo Credentials:</h4>
            <p><strong>Admin:</strong> admin / password123</p>
            <p><strong>User:</strong> john / password123</p>
        </div>
    </div>
</body>
</html>