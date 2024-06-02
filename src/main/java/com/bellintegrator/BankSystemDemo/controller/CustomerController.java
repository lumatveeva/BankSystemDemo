package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

    @GetMapping("/{id}")
    public String findByCustomerId(@PathVariable("id") UUID id,
                                        Model model){
        model.addAttribute("customer", customerService.findById(id));
        return "customer/byId";
    }

    @GetMapping("/all")
    public String findAll(Model model){
        model.addAttribute("customers", customerService.findAllCustomer());
        return "customer/allCustomers";
    }
    @GetMapping("/update")
    public String update(Model model){
        model.addAttribute("customer", new Customer());
        return "customer/updateCustomer";
    }

    @PostMapping("/{id}")
    public String updateCustomer(@PathVariable("id") UUID id,
                                 @ModelAttribute("customer") @Valid Customer customer,
                                 BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "customer/updateCustomer";
        }
        customerService.updateCustomer(id, customer);
        return "redirect:/customers";
    }

}
