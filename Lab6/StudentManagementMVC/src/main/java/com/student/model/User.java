package com.student.model;

import java.sql.Timestamp;

public class User {
    // --- 1. Private Attributes ---
    private int id;
    private String username;
    private String password; // Note: Store hashed passwords here, not plain text!
    private String fullName;
    private String role;     // e.g., "admin", "user"
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp lastLogin;

    // --- 2. No-Arg Constructor ---
    public User() {
    }

    // --- 3. Parameterized Constructor ---
    // Used when creating a NEW user (ID and Dates are usually handled by DB)
    public User(String username, String password, String fullName, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.isActive = true; // Default to active for new users
    }

    // --- 4. Getters and Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    // --- 5. Utility Methods ---

    public boolean isAdmin() {
        // Case-insensitive check is safer (e.g., "Admin" vs "admin")
        return this.role != null && "admin".equalsIgnoreCase(this.role);
    }

    public boolean isUser() {
        return this.role != null && "user".equalsIgnoreCase(this.role);
    }

    // --- 6. Override toString() ---
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", lastLogin=" + lastLogin +
                '}';
        // Note: Password is deliberately excluded for security logging
    }
}