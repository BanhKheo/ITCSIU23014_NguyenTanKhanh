package com.student.controller;

import com.student.dao.UserDAO;
import com.student.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Check if already logged in
        HttpSession session = request.getSession(false); // false = don't create new one
        if (session != null && session.getAttribute("user") != null) {
            // Already logged in? Go to the student list
            response.sendRedirect("dashboard");
            return;
        }

        // 2. If not logged in, show the login form
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 2. Validate input
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        // 3. Authenticate user via DAO
        User user = userDAO.authenticate(username, password);

        if (user != null) {
            // --- SUCCESSFUL LOGIN ---

            // A. Security: Invalidate old session to prevent Session Fixation attacks
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // B. Create a new, clean session
            HttpSession newSession = request.getSession(true);

            // C. Store user data in session
            newSession.setAttribute("user", user);

            // D. Set session timeout (30 minutes = 1800 seconds)
            newSession.setMaxInactiveInterval(30 * 60);

            // E. Redirect based on role (Optional)
            // For now, everyone goes to the student list
            response.sendRedirect("dashboard");

        } else {
            // --- FAILED LOGIN ---
            request.setAttribute("error", "Invalid username or password");
            // Forward back to login page to show error
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}