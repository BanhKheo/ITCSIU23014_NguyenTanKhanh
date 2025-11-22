<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Student Management</title>
    <style>
        /* Base Styles */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7f6;
            min-height: 100vh;
        }

        /* Navigation Bar */
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .brand { font-size: 20px; font-weight: bold; display: flex; align-items: center; gap: 10px; }
        .user-menu { display: flex; align-items: center; gap: 20px; }
        .user-info { font-weight: 500; }
        .logout-btn {
            background-color: rgba(255,255,255,0.2);
            color: white;
            text-decoration: none;
            padding: 8px 15px;
            border-radius: 4px;
            font-size: 14px;
            transition: background 0.3s;
        }
        .logout-btn:hover { background-color: rgba(255,255,255,0.3); }

        /* Main Layout */
        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
        }

        /* Welcome Section */
        .welcome-section { margin-bottom: 40px; }
        .welcome-section h2 { color: #333; font-size: 28px; margin-bottom: 5px; }
        .welcome-section p { color: #666; }

        /* Statistics Cards Grid */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }

        .card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            border-left: 5px solid #667eea;
            transition: transform 0.2s;
        }
        .card:hover { transform: translateY(-5px); }
        .card h3 { color: #888; font-size: 14px; text-transform: uppercase; margin-bottom: 10px; }
        .card .number { font-size: 36px; font-weight: bold; color: #333; }
        .card .icon { float: right; font-size: 30px; opacity: 0.2; }

        /* Quick Actions Grid */
        .actions-section h3 { margin-bottom: 20px; color: #444; }
        .actions-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
        }

        .action-btn {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            background: white;
            padding: 30px;
            border-radius: 10px;
            text-decoration: none;
            color: #555;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            transition: all 0.3s;
            border: 1px solid transparent;
        }
        .action-btn:hover {
            border-color: #667eea;
            color: #667eea;
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.15);
        }
        .action-icon { font-size: 32px; margin-bottom: 10px; }
        .action-title { font-weight: 600; }

        /* Admin Badge */
        .role-badge {
            font-size: 12px;
            background-color: #fff;
            color: #667eea;
            padding: 2px 8px;
            border-radius: 10px;
            margin-left: 5px;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <nav class="navbar">
        <div class="brand">
            🎓 SMS Dashboard
        </div>
        <div class="user-menu">
            <div class="user-info">
                Hello, <c:out value="${sessionScope.user.fullName}"/>
                <span class="role-badge"><c:out value="${sessionScope.user.role}"/></span>
            </div>
            <a href="logout" class="logout-btn">Logout ➔</a>
        </div>
    </nav>

    <div class="container">

        <div class="welcome-section">
            <h2>Welcome back! 👋</h2>
            <p>Here is what's happening with your students today.</p>
        </div>

        <div class="stats-grid">
            <div class="card">
                <span class="icon">👨‍🎓</span>
                <h3>Total Students</h3>
                <div class="number">${totalStudents}</div>
            </div>

            <div class="card" style="border-left-color: #28a745;">
                <span class="icon">📅</span>
                <h3>Last Login</h3>
                <div class="number" style="font-size: 16px; line-height: 40px;">
                    <c:out value="${sessionScope.user.lastLogin}"/>
                </div>
            </div>

            <div class="card" style="border-left-color: #ffc107;">
                <span class="icon">📚</span>
                <h3>Active Majors</h3>
                <div class="number">4</div>
            </div>
        </div>

        <div class="actions-section">
            <h3>Quick Actions</h3>
            <div class="actions-grid">

                <a href="student?action=list" class="action-btn">
                    <span class="action-icon">📋</span>
                    <span class="action-title">View All Students</span>
                </a>

                <a href="student?action=list" class="action-btn">
                    <span class="action-icon">🔍</span>
                    <span class="action-title">Search Records</span>
                </a>

                <c:if test="${sessionScope.user.role == 'admin'}">
                    <a href="student?action=new" class="action-btn">
                        <span class="action-icon">➕</span>
                        <span class="action-title">Add New Student</span>
                    </a>
                </c:if>

                <a href="#" class="action-btn" onclick="alert('Profile feature coming soon!')">
                    <span class="action-icon">👤</span>
                    <span class="action-title">My Profile</span>
                </a>
            </div>
        </div>
    </div>

</body>
</html>