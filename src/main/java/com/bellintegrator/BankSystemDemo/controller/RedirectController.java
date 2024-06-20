package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.security.MyCustomerDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class RedirectController {

    @Operation(summary = "Перенаправление пользователя после входа")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу пользователя после успешного входа")
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/redirect")
    public String redirectAfterLogin(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyCustomerDetail userDetails = (MyCustomerDetail) authentication.getPrincipal();
        UUID customerId = userDetails.getCustomer().getId();

        // Добавляем ID пользователя в RedirectAttributes
        redirectAttributes.addAttribute("id", customerId);
        return "redirect:/customer/{id}";
    }
}
