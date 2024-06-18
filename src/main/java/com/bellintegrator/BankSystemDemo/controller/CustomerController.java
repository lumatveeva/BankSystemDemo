package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.service.CustomerService;
import com.bellintegrator.BankSystemDemo.util.CustomerValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerValidator customerValidator;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}")
    public String findByCustomerId(@PathVariable("id") UUID id,
                                        Model model){
        model.addAttribute("customer", customerService.findById(id));
        return "customer/byId";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all")
    public String findAll(Model model){
        model.addAttribute("customers", customerService.findAllCustomer());
        return "customer/allCustomers";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/update")
    public String update(@RequestParam("userId") UUID userId,
                         Model model){
        model.addAttribute("userId", userId);
        model.addAttribute("customer", customerService.findById(userId));
        return "customer/updateCustomer";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/update")
    public String updateCustomer(@RequestParam("userId") UUID userId,
                                 @ModelAttribute("customer") @Valid Customer customer,
                                 BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "customer/updateCustomer";
        }
        customerService.updateCustomer(userId, customer);
        return "redirect:/customer/" + userId;
    }

}
