package com.student.dao;

import com.student.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class UserDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "khanhdepzai16";

    // SQL Queries Constants
    // Note: I added 'AND is_active = 1' so banned users cannot login
    private static final String SQL_AUTHENTICATE =
            "SELECT * FROM users WHERE username = ? AND is_active = 1";

    private static final String SQL_UPDATE_LAST_LOGIN =
            "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";

    private static final String SQL_GET_BY_ID =
            "SELECT * FROM users WHERE id = ?";

    // Implement getConnection()
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }

    // Implement authenticate()
    public User authenticate(String username, String password) {
        try (Connection conn = getConnection();
             // Use the CONSTANT defined at the top
             PreparedStatement pstmt = conn.prepareStatement(SQL_AUTHENTICATE)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 1. Get the hashed password from DB
                    String storedHash = rs.getString("password");

                    // 2. Check password using BCrypt
                    if (BCrypt.checkpw(password, storedHash)) {

                        // 3. Use Helper method to create User object
                        User user = mapResultSetToUser(rs);

                        // Security: Clear the password hash before returning to Controller/View
                        user.setPassword(null);

                        // 4. Update last login
                        updateLastLogin(user.getId());

                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Login failed
    }

    // Implement updateLastLogin()
    private void updateLastLogin(int userId) {
        try (Connection conn = getConnection();
             // Use the CONSTANT defined at the top
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_LAST_LOGIN)) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Implement getUserById()
    public User getUserById(int id) {
        try (Connection conn = getConnection();
             // Use the CONSTANT defined at the top
             PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BY_ID)) {

            pstmt.setInt(1, id);

            // FIX: Use executeQuery() for SELECT, not executeUpdate()
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to map ResultSet to User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password")); // We map it here, but clear it in authenticate
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("is_active"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        return user;
    }
}