package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import com.bellintegrator.BankSystemDemo.service.RegistrationService;
import com.bellintegrator.BankSystemDemo.util.CustomerValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final CustomerValidator customerValidator;
    private final RegistrationService registrationService;
    private final CustomerRepository customerRepository;

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model){
        model.addAttribute("customer", new Customer());
        return "auth/registration";
    }
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("customer") @Valid Customer customer,
                                      BindingResult bindingResult){
        customerValidator.validate(customer, bindingResult);

        if(bindingResult.hasErrors()){
            return "auth/registration";
        }
        registrationService.register(customer);
        return "redirect:/auth/login";
    }

}
