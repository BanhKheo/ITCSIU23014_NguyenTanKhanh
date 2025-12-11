package com.example.customer_api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode; // Important to avoid Lombok recursion errors
import org.springframework.hateoas.RepresentationModel; // <--- Import this
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false) // <--- Add this
public class CustomerResponseDTO extends RepresentationModel<CustomerResponseDTO> { // <--- Extend this
    private Long id;
    private String customerCode;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String status;
}