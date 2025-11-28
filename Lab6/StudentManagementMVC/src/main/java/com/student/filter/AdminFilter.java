package com.student.filter;

import com.student.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(filterName = "AdminFilter", urlPatterns = {"/student"})
public class AdminFilter implements Filter {

    // Admin-only actions
    private static final String[] ADMIN_ACTIONS = {
            "new",
            "insert",
            "edit",
            "update",
            "delete"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🔒 AdminFilter Initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. Get the "action" from the URL parameter (e.g., ?action=edit)
        String action = httpRequest.getParameter("action");

        // 2. Check if this specific action requires Admin privileges
        if (isAdminAction(action)) {

            // 3. Get session and user
            HttpSession session = httpRequest.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;

            // 4. Check if user exists AND is an Admin
            // Note: Make sure your User model has .isAdmin() or check .getRole().equals("admin")
            if (user != null && "admin".equalsIgnoreCase(user.getRole())) {
                // ✅ ACCESS GRANTED
                chain.doFilter(request, response);
            } else {
                // ⛔ ACCESS DENIED
                System.out.println("⛔ Non-admin tried to access: " + action);
                // Redirect back to the list with an error message
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/student?action=list&error=Access Denied: Admin privileges required.");
            }
        } else {
            // 5. It is a public action (list, search, filter, sort)
            // Allow everyone (even normal users) to pass
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        System.out.println("🔓 AdminFilter Destroyed");
    }

    // Helper to check the array
    private boolean isAdminAction(String action) {
        // Handle null (if user just goes to /student, action is null -> treated as "list")
        if (action == null) return false;

        for (String adminAction : ADMIN_ACTIONS) {
            if (action.equals(adminAction)) {
                return true;
            }
        }
        return false;
    }
}