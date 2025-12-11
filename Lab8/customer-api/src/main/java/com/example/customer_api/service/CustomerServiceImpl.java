package com.example.customer_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.customer_api.dto.CustomerRequestDTO;
import com.example.customer_api.dto.CustomerResponseDTO;
import com.example.customer_api.dto.CustomerUpdateDTO;
import com.example.customer_api.entity.Customer;
import com.example.customer_api.entity.CustomerStatus;
import com.example.customer_api.exception.DuplicateResourceException;
import com.example.customer_api.exception.ResourceNotFoundException;
import com.example.customer_api.repository.CustomerRepository;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return convertToResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        // 1. Check for duplicate customer code
        if (customerRepository.existsByCustomerCode(requestDTO.getCustomerCode())) {
            throw new DuplicateResourceException("Customer code already exists: " + requestDTO.getCustomerCode());
        }

        // 2. Check for duplicate email
        if (customerRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + requestDTO.getEmail());
        }

        // 3. Convert and Save
        Customer customer = convertToEntity(requestDTO);
        Customer savedCustomer = customerRepository.save(customer);

        return convertToResponseDTO(savedCustomer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        // 1. Find existing customer
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        // 2. Check for duplicate email (Only if email has changed)
        if (!existingCustomer.getEmail().equals(requestDTO.getEmail()) && 
             customerRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email already taken by another customer");
        }

        // 3. Update fields
        existingCustomer.setFullName(requestDTO.getFullName());
        existingCustomer.setEmail(requestDTO.getEmail());
        existingCustomer.setPhone(requestDTO.getPhone());
        existingCustomer.setAddress(requestDTO.getAddress());
        
        // Safe conversion for Update
        if (requestDTO.getStatus() != null) {
            existingCustomer.setStatus(CustomerStatus.valueOf(requestDTO.getStatus()));
        }

        // 4. Save and return
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToResponseDTO(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    // =========================================================================
    // Helper Methods (FIXED)
    // =========================================================================

    private CustomerResponseDTO convertToResponseDTO(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setCustomerCode(customer.getCustomerCode());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        
        // FIX: Check if the ENTITY has a status, then set it to DTO
        if (customer.getStatus() != null) {
            dto.setStatus(customer.getStatus().name()); 
        }
        return dto;
    }

    private Customer convertToEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setCustomerCode(dto.getCustomerCode());
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        
        // FIX: Check if the DTO has a status, then set it to ENTITY
        if (dto.getStatus() != null) {
            customer.setStatus(CustomerStatus.valueOf(dto.getStatus()));
        }
        return customer;
    }



    // =========================================================================
    // Search Methods 
    // =========================================================================

    @Override
    public List<CustomerResponseDTO> searchCustomers(String keyword) {
        return customerRepository.searchCustomers(keyword)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    //Filter customers by status
    @Override
    public List<CustomerResponseDTO> getCustomersByStatus(String statusStr) {
        try {
            // 1. Convert String "ACTIVE" -> Enum CustomerStatus.ACTIVE
            CustomerStatus status = CustomerStatus.valueOf(statusStr.toUpperCase());
            
            // 2. Call Repository with the Enum
            List<Customer> customers = customerRepository.findByStatus(status);
            
            System.out.println("Filtered Customers: " + customers); // Debugging line
            // 3. Convert to DTOs
            return customers.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
                    
        } catch (IllegalArgumentException e) {
            // Handle case where user sends invalid status like "DELETED"
            throw new ResourceNotFoundException("Invalid status: " + statusStr);
        }
    }

    // Advanced Search
    @Override
    public List<CustomerResponseDTO> advancedSearch(String name, String email, String statusStr)
    {
        CustomerStatus status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = CustomerStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ResourceNotFoundException("Invalid status: " + statusStr);
            }
        }

        List<Customer> customers = customerRepository
                .findByFullNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndStatus(
                        name != null ? name : "",
                        email != null ? email : "",
                        status);

        return customers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    // Pagination 
    @Override
    public Page<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, String sortDir) {
        
        // 1. Configure Sorting
        // Check if sort direction is "desc", otherwise default to "asc"
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();

        // 2. Create Pageable object with Page, Size, AND Sort
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // 3. Fetch data
        return customerRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }


    @Override
    public CustomerResponseDTO partialUpdateCustomer(Long id, CustomerUpdateDTO updateDTO) {
        // 1. Find existing customer
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        // 2. Update only non-null fields
        if (updateDTO.getFullName() != null) {
            existingCustomer.setFullName(updateDTO.getFullName());
        }
        if (updateDTO.getEmail() != null) {
            // Check for duplicate email
            if (!existingCustomer.getEmail().equals(updateDTO.getEmail()) && 
                 customerRepository.existsByEmail(updateDTO.getEmail())) {
                throw new DuplicateResourceException("Email already taken by another customer");
            }
            existingCustomer.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getPhone() != null) {
            existingCustomer.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getAddress() != null) {
            existingCustomer.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getStatus() != null) {
            existingCustomer.setStatus(CustomerStatus.valueOf(updateDTO.getStatus()));
        }

        // 3. Save and return
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToResponseDTO(updatedCustomer);
    }
}