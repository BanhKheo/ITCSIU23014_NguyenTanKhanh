package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteStudent(request, response);
                break;
            // ALL view actions go to the same method now!
            case "search":
            case "sort":
            case "filter":
            case "list":
            default:
                listStudents(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "insert": insertStudent(request, response); break;
            case "update": updateStudent(request, response); break;
        }
    }

    // --- THE UNIFIED LIST METHOD ---
    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get ALL Parameters (Search, Filter, Sort)
        String keyword = request.getParameter("keyword");
        String major = request.getParameter("major");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");

        // 2. Call the new Universal DAO method
        List<Student> students = studentDAO.getStudentsUniversal(keyword, major, sortBy, order);

        // 3. Send Data to View
        request.setAttribute("students", students);

        // 4. Preserve State (Send inputs back to JSP)
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedMajor", major);
        request.setAttribute("sortBy", (sortBy != null && !sortBy.isEmpty()) ? sortBy : "id");
        request.setAttribute("order", (order != null && !order.isEmpty()) ? order : "asc");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // ... (Your existing methods: validateStudent, showNewForm, showEditForm, insertStudent, updateStudent, deleteStudent REMAIN THE SAME) ...
    // Note: You can delete the old private searchStudents() method now.

    // Paste your existing validation and CRUD methods here...

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Student existingStudent = studentDAO.getStudentById(id);
            request.setAttribute("student", existingStudent);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            response.sendRedirect("student?action=list&error=Invalid ID");
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if (studentDAO.deleteStudent(id)) {
                response.sendRedirect("student?action=list&message=Student deleted successfully");
            } else {
                response.sendRedirect("student?action=list&error=Failed to delete student");
            }
        } catch (Exception e) {
            response.sendRedirect("student?action=list&error=Invalid ID");
        }
    }

    private boolean validateStudent(Student student, HttpServletRequest request) {
        boolean isValid = true;
        String codePattern = "[A-Z]{2}[0-9]{3,}";

        if (student.getStudentCode() == null || student.getStudentCode().trim().isEmpty()) {
            request.setAttribute("errorCode", "Student code is required");
            isValid = false;
        } else if (!student.getStudentCode().matches(codePattern)) {
            request.setAttribute("errorCode", "Invalid format. Use 2 letters + 3+ digits");
            isValid = false;
        }

        if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
            request.setAttribute("errorName", "Full name is required");
            isValid = false;
        }

        if (student.getMajor() == null || student.getMajor().trim().isEmpty()) {
            request.setAttribute("errorMajor", "Major is required");
            isValid = false;
        }

        return isValid;
    }

    private void insertStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student newStudent = new Student(studentCode, fullName, email, major);

        if (!validateStudent(newStudent, request)) {
            request.setAttribute("student", newStudent);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (studentDAO.addStudent(newStudent)) {
            response.sendRedirect("student?action=list&message=Student added successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to add student");
        }
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String studentCode = request.getParameter("studentCode");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String major = request.getParameter("major");

            Student student = new Student(studentCode, fullName, email, major);
            student.setId(id);

            if (!validateStudent(student, request)) {
                request.setAttribute("student", student);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
                dispatcher.forward(request, response);
                return;
            }

            if (studentDAO.updateStudent(student)) {
                response.sendRedirect("student?action=list&message=Student updated successfully");
            } else {
                response.sendRedirect("student?action=list&error=Failed to update");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("student?action=list&error=Invalid ID");
        }
    }
}