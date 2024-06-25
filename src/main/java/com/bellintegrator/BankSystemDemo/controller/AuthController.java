package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.dto.CustomerDTO;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import com.bellintegrator.BankSystemDemo.service.RegistrationService;
import com.bellintegrator.BankSystemDemo.util.CustomerValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Отображение страницы входа")
    @ApiResponse(
            responseCode = "200",
            description = "Страница входа успешно загружена",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }


    @Operation(summary = "Отображение страницы регистрации")
    @ApiResponse(
            responseCode = "200",
            description = "Страница регистрации успешно загружена",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "auth/registration";
    }

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу входа после успешной регистрации"
    )
    @ApiResponse(responseCode = "400", description = "Неверные данные")
    @PostMapping("/registration")
    public String performRegistration(@Parameter(description = "Данные нового пользователя", required = true)
                                      @ModelAttribute("customer") @Valid CustomerDTO customerDTO,
                                      BindingResult bindingResult) {
        customerValidator.validate(customerDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        registrationService.register(customerDTO);
        return "redirect:/auth/login";
    }

}
