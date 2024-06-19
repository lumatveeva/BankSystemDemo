package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.dto.CustomerForm;
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
                                   Model model) {
        model.addAttribute("customer", customerService.findCustomerFormByCustomerId(id));
        return "customer/byId";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all")
    public String findAll(Model model) {

        model.addAttribute("customers", customerService.findAllCustomerForm());
        return "customer/allCustomers";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/update")
    public String update(@RequestParam("customerId") UUID customerId,
                         Model model) {
//        model.addAttribute("customerId", customerId);
        model.addAttribute("customer", customerService.findCustomerFormByCustomerId(customerId));
        return "customer/updateCustomer";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/update")
    public String updateCustomer(@RequestParam("customerId") UUID customerId,
                                 @ModelAttribute("customer") @Valid CustomerForm customerForm,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "customer/updateCustomer";
        }
        customerService.updateCustomer(customerId, customerForm);
        return "redirect:/customer/" + customerId;
    }

}
