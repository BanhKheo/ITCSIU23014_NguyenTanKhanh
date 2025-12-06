package com.example.productmanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.productmanagement.repository.ProductRepository;
import com.example.productmanagement.service.ProductService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private ProductRepository productRepository; // Direct repo access allowed for simple read-only dashboard tasks
    
    @Autowired
    private ProductService productService;

    @GetMapping
    public String showDashboard(Model model) {
        // General Stats
        model.addAttribute("totalProducts", productRepository.count());
        model.addAttribute("totalValue", productRepository.calculateTotalValue());
        model.addAttribute("avgPrice", productRepository.calculateAveragePrice());
        
        // Low Stock
        model.addAttribute("lowStockProducts", productRepository.findLowStockProducts(5));

        // Category Stats Map
        List<String> categories = productService.getAllCategories();
        Map<String, Long> catStats = new HashMap<>();
        for (String cat : categories) {
            catStats.put(cat, productRepository.countByCategory(cat));
        }
        model.addAttribute("categoryStats", catStats);

        return "dashboard";
    }
}
