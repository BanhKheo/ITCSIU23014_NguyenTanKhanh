package com.example.customer_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.customer_api.entity.Customer;
import com.example.customer_api.entity.CustomerStatus;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // TODO: Add method to find by customer code
    List<Customer> findByCustomerCode(String customerCode);
    
    // TODO: Add method to find by email
    List<Customer> findByEmail(String email);
    
    // TODO: Add method to check if customer code exists
    boolean existsByCustomerCode(String customerCode);
    
    // TODO: Add method to check if email exists
    boolean existsByEmail(String email);
    

    @Query("SELECT c FROM Customer c WHERE " +
       "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Customer> searchCustomers(@Param("keyword") String keyword);

   
    // filter by enum status
    List<Customer> findByStatus(CustomerStatus status);


    // advanced search
    List<Customer> findByFullNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndStatus(
        String fullName, String email, CustomerStatus status);

    

}
