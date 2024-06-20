package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.dto.CustomerForm;
import com.bellintegrator.BankSystemDemo.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
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


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Получение страницы пользователь по его ID")
    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение страницы пользователя",
            content = @Content(
                    mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @GetMapping("/{id}")
    public String findByCustomerId(@PathVariable("id") UUID id,
                                   Model model) {
        model.addAttribute("customer", customerService.findCustomerFormByCustomerId(id));
        return "customer/byId";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Получение страницы всех пользователей зарегистрированных в приложении")
    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение всех пользователей",
            content = @Content(
                    mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @GetMapping("/all")
    public String findAll(Model model) {

        model.addAttribute("customers", customerService.findAllCustomerForm());
        return "customer/allCustomers";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Отображение страницы обновления пользователя")
    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение страницы обновления",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/update")
    public String update(@Parameter(description = "ID пользователя", required = true)
                         @RequestParam("customerId") UUID customerId,
                         Model model) {
        model.addAttribute("customer", customerService.findCustomerFormByCustomerId(customerId));
        return "customer/updateCustomer";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Обновление информации пользователя")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу пользователя после успешного обновления"
    )
    @ApiResponse(responseCode = "400", description = "Неверные данные")
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @PostMapping("/update")
    public String updateCustomer(@Parameter(description = "ID пользователя", required = true)
                                 @RequestParam("customerId") UUID customerId,
                                 @Parameter(description = "Форма с обновленной информацией о пользователе", required = true)
                                 @ModelAttribute("customer") @Valid CustomerForm customerForm,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "customer/updateCustomer";
        }
        customerService.updateCustomer(customerId, customerForm);
        return "redirect:/customer/" + customerId;
    }

}
