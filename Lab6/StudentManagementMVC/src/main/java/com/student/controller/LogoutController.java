package com.student.controller; // Make sure this matches your package structure (e.g., com.student.controller)

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get session (false = don't create if doesn't exist)
        // We use 'false' because if they don't have a session, they are already logged out!
        HttpSession session = request.getSession(false);

        // 2. If session exists, invalidate it
        if (session != null) {
            // This destroys all data (user object, cart, history) stored in the server memory
            session.invalidate();
        }

        // 3. Redirect to login with success message
        // We redirect to the Servlet (login), not the JSP directly
        response.sendRedirect("login?message=You have been logged out successfully");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}