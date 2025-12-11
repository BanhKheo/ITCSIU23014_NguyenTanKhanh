package com.example.customer_api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.customer_api.dto.CustomerRequestDTO;
import com.example.customer_api.dto.CustomerResponseDTO;
import com.example.customer_api.dto.CustomerUpdateDTO;
import com.example.customer_api.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/customers")
@CrossOrigin(origins = "*") // In production, replace "*" with your frontend URL
public class CustomerRestControllerV2 {
    
    private final CustomerService customerService;

    @Autowired
    public CustomerRestControllerV2(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    // 1. GET ALL CUSTOMERS with Pagination
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomersV2() {
        // In a real app, you might have a different Service method for V2.
        // Here, we just modify the response manually to demonstrate the versioning.
        List<CustomerResponseDTO> customers = customerService.getAllCustomers(0, 10, "id", "asc").getContent();
        
        customers.forEach(c -> c.setFullName("V2: " + c.getFullName().toUpperCase())); // V2 Logic
        
        return ResponseEntity.ok(customers);
    }
    
    // 2. GET CUSTOMER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.getCustomerById(id);
        // Returns HTTP 200 (OK)
        return ResponseEntity.ok(customer);
    }
    
    // 3. CREATE NEW CUSTOMER
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO requestDTO) {
        CustomerResponseDTO newCustomer = customerService.createCustomer(requestDTO);
        // Returns HTTP 201 (Created)
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }
    
    // 4. UPDATE CUSTOMER
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id, 
            @Valid @RequestBody CustomerRequestDTO requestDTO) {
        
        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(id, requestDTO);
        // Returns HTTP 200 (OK)
        return ResponseEntity.ok(updatedCustomer);
    }
    
    // 5. DELETE CUSTOMER
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        // Returns HTTP 200 (OK) with a message
        return ResponseEntity.ok("Customer deleted successfully with ID: " + id);
    }

    // 6. SEARCH CUSTOMERS
    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponseDTO>> searchCustomers(
            @RequestParam String keyword) {
        List<CustomerResponseDTO> customers = customerService.searchCustomers(keyword);
        return ResponseEntity.ok(customers);
    }

    // 7. FILTER CUSTOMERS BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CustomerResponseDTO>> getCustomersByStatus(
            @PathVariable String status) {
        List<CustomerResponseDTO> customers = customerService.getCustomersByStatus(status);
        return ResponseEntity.ok(customers);
    }

    // 8. ADVANCED SEARCH
    @GetMapping("/advanced-search")
    public ResponseEntity<List<CustomerResponseDTO>> advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status) {
        // Implementation
        List<CustomerResponseDTO> customers = customerService.advancedSearch(name, email, status);
        return ResponseEntity.ok(customers);
    }

    // 9. PARTIAL UPDATE CUSTOMER
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> partialUpdateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerUpdateDTO updateDTO) {
        
        CustomerResponseDTO updated = customerService.partialUpdateCustomer(id, updateDTO);
        return ResponseEntity.ok(updated);
    }



    


}