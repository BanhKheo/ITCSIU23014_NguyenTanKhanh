package com.student.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

// Intercept ALL requests
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    // Public URLs that don't require authentication
    // Note: We include static files (.css, .js) so the login page looks good!
    private static final String[] PUBLIC_URLS = {
            "/login",
            "/register", // If you have a register page
            ".css",
            ".js",
            ".png",
            ".jpg",
            ".jpeg"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🔒 AuthFilter Initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 1. Cast to HTTP objects to access Session and URL data
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 2. Extract the specific path requested (e.g., "/dashboard" or "/login")
        // RequestURI = "/StudentManagement/dashboard"
        // ContextPath = "/StudentManagement"
        // Path = "/dashboard"
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // 3. Check if this is a Public URL
        if (isPublicUrl(path)) {
            // It's public, let them pass without checking session
            chain.doFilter(request, response);
            return;
        }

        // 4. Check if user is logged in
        // getSession(false) means: Get current session, but DO NOT create a new one if none exists.
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (isLoggedIn) {
            // 5. User is valid, allow request to proceed to Controller/JSP

            // (Optional) Prevent browser back-button caching of secured pages
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
            httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0
            httpResponse.setDateHeader("Expires", 0); // Proxies

            chain.doFilter(request, response);
        } else {
            // 6. User is NOT logged in, kick them to the login page
            // We use sendRedirect because we want the URL to change
            System.out.println("⛔ Access Denied for path: " + path);
            httpResponse.sendRedirect(contextPath + "/login?error=Please login first");
        }
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }

    // Helper method to check against the PUBLIC_URLS array
    private boolean isPublicUrl(String path) {
        for (String publicUrl : PUBLIC_URLS) {
            // If path matches exactly (e.g., "/login")
            // OR path ends with extension (e.g., "style.css")
            if (path.equals(publicUrl) || path.endsWith(publicUrl)) {
                return true;
            }
        }
        return false;
    }
}