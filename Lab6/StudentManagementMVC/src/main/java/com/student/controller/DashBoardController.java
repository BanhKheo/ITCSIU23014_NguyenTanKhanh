package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashBoardController extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        // Initialize studentDAO
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get user from session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // Security Check: If not logged in, redirect to login
        if (user == null) {
            response.sendRedirect("login?error=Session expired, please login again");
            return;
        }

        // 2. Get statistics (Total Students)
        // Ensure your StudentDAO has the getTotalStudents() method!
        int totalStudents = studentDAO.getTotalStudents();

        // 3. Set attributes
        request.setAttribute("totalStudents", totalStudents);

        // 4. Forward to dashboard.jsp
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}