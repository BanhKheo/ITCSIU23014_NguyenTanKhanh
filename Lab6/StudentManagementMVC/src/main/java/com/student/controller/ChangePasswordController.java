package com.student.controller;

import com.student.dao.UserDAO;
import com.student.model.User;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Security Check: Ensure user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        // Show the form
        request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get Session User
        HttpSession session = request.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (sessionUser == null) {
            response.sendRedirect("login");
            return;
        }

        // 2. Get Form Parameters
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // 3. Validation

        // A. Check for empty fields
        if (currentPassword == null || newPassword == null || confirmPassword == null ||
                currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            return;
        }

        // B. Check new password length
        if (newPassword.length() < 8) {
            request.setAttribute("error", "New password must be at least 8 characters");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            return;
        }

        // C. Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New passwords do not match");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            return;
        }

        // 4. Verify Current Password
        // We must fetch the user from DB again to get the current HASHED password
        User dbUser = userDAO.getUserById(sessionUser.getId());

        if (!BCrypt.checkpw(currentPassword, dbUser.getPassword())) {
            request.setAttribute("error", "Current password is incorrect");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            return;
        }

        // 5. Hash New Password & Update
        String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        boolean isUpdated = userDAO.updatePassword(sessionUser.getId(), newHashedPassword);

        if (isUpdated) {
            // Success! Redirect to dashboard or logout
            request.setAttribute("message", "Password changed successfully!");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Database error occurred. Please try again.");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
        }
    }
}