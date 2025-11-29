//package com.student.util;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class CookieUtil {
//
//    /**
//     * Create and add cookie to response
//     * @param response HTTP response
//     * @param name Cookie name
//     * @param value Cookie value
//     * @param maxAge Cookie lifetime in seconds
//     */
//    public static void createCookie(HttpServletResponse response,
//                                    String name,
//                                    String value,
//                                    int maxAge) {
//        // 1. Create new Cookie with name and value
//        Cookie cookie = new Cookie(name, value);
//
//        // 2. Set maxAge (in seconds)
//        cookie.setMaxAge(maxAge);
//
//        // 3. Set path to "/" (Available to entire application)
//        cookie.setPath("/");
//
//        // 4. Set httpOnly to true (Security: prevents JavaScript access)
//        cookie.setHttpOnly(true);
//
//        // 5. Add cookie to response
//        response.addCookie(cookie);
//    }
//
//    /**
//     * Get cookie value by name
//     * @param request HTTP request
//     * @param name Cookie name
//     * @return Cookie value or null if not found
//     */
//    public static String getCookieValue(HttpServletRequest request, String name) {
//        // 1. Get all cookies from request
//        Cookie[] cookies = request.getCookies();
//
//        // Check if cookies array is not null (it is null if no cookies exist)
//        if (cookies != null) {
//            // 2. Loop through cookies
//            for (Cookie cookie : cookies) {
//                // 3. Find cookie with matching name
//                if (name.equals(cookie.getName())) {
//                    // 4. Return value
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Check if cookie exists
//     * @param request HTTP request
//     * @param name Cookie name
//     * @return true if cookie exists
//     */
//    public static boolean hasCookie(HttpServletRequest request, String name) {
//        // Reuse getCookieValue to check existence
//        return getCookieValue(request, name) != null;
//    }
//
//    /**
//     * Delete cookie by setting max age to 0
//     * @param response HTTP response
//     * @param name Cookie name to delete
//     */
//    public static void deleteCookie(HttpServletResponse response, String name) {
//        // 1. Create cookie with same name and empty value
//        Cookie cookie = new Cookie(name, "");
//
//        // 2. Set maxAge to 0 (This tells browser to delete it immediately)
//        cookie.setMaxAge(0);
//
//        // 3. Set path to "/" (Must match original cookie path to delete)
//        cookie.setPath("/");
//
//        // 4. Add to response
//        response.addCookie(cookie);
//    }
//
//    /**
//     * Update existing cookie
//     * @param response HTTP response
//     * @param name Cookie name
//     * @param newValue New cookie value
//     * @param maxAge New max age
//     */
//    public static void updateCookie(HttpServletResponse response,
//                                    String name,
//                                    String newValue,
//                                    int maxAge) {
//        // To update a cookie, we simply overwrite it by creating a new one
//        // with the same name but different value/age.
//        createCookie(response, name, newValue, maxAge);
//    }
//}