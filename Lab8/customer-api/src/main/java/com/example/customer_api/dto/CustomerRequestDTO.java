package com.example.customer_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {
    
    @NotBlank(message = "Customer code cannot be blank")
    @Size(min = 3, max = 20, message = "Customer code must be between 3 and 20 characters")
    // Regex is now perfect (matches Entity)
    @Pattern(regexp= "^C\\d{3,}$" , message = "Customer code must start with 'C' followed by at least 3 digits")
    private String customerCode; // Removed @Column

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format") 
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    // This is the Perfect Strategy: Validate String here -> Convert to Enum in Service
    @Pattern(regexp = "^(ACTIVE|INACTIVE|BANNED|PENDING)$", message = "Status must be ACTIVE, INACTIVE, BANNED, or PENDING")
    private String status;
}