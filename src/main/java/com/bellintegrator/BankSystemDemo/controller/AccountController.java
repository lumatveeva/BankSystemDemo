package com.bellintegrator.BankSystemDemo.controller;


import com.bellintegrator.BankSystemDemo.dto.AccountForm;
import com.bellintegrator.BankSystemDemo.model.Account;
import com.bellintegrator.BankSystemDemo.model.AccountType;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.service.AccountService;
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
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Отображение всех счетов")
    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение списка всех счетов",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/all")
    public String allAccounts(Model model) {
        model.addAttribute("accounts", accountService.findAllAccountForm());
        return "accounts/allAccounts";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Отображение всех счетов пользователя по его ID")
    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение списка всех счетов пользователя",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("customer/{id}")
    public String findAllByCustomerId(@Parameter(description = "ID пользователя", required = true)
                                      @PathVariable("id") UUID id,
                                      Model model) {
        model.addAttribute("accounts", accountService.allAccountsFormByCustomerId(id));
        return "accounts/allAccountsByCustomer";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Отображение информации о счете по его ID")
    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение информации о счете",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/{id}")
    public String findById(@Parameter(description = "ID счета", required = true)
                           @PathVariable("id") UUID id,
                           Model model) {
        model.addAttribute("account", accountService.findAccountFormByAccountId(id));
        return "accounts/byId";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Создание нового счета")
    @ApiResponse(
            responseCode = "200",
            description = "Страница создания нового счета успешно загружена",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/new")
    public String createAccount(@Parameter(description = "ID пользователя", required = true)
                                @RequestParam("customerId") UUID customerId,
                                Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("customerId", customerId);
        model.addAttribute("accountTypes", AccountType.values());
        return "accounts/new";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Добавление нового счета")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу пользователя после успешного добавления счета"
    )
    @ApiResponse(responseCode = "400", description = "Неверные данные")
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @PostMapping("/addAccount")
    public String addAccount(@Parameter(description = "Данные нового счета", required = true)
                             @ModelAttribute("account") @Valid AccountForm accountForm,
                             @Parameter(description = "ID пользователя", required = true)
                             @RequestParam("customerId") UUID customerId,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountTypes", AccountType.values());
            return "accounts/new";
        }
        accountService.createAccount(accountForm, customerId);
        return "redirect:/customer/" + customerId;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Пополнение счета")
    @ApiResponse(
            responseCode = "200",
            description = "Страница пополнения счета успешно загружена",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/putMoney")
    public String putMoney(@Parameter(description = "ID счета", required = true)
                           @RequestParam("accountId") UUID accountId,
                           Model model) {
        model.addAttribute("account", accountService.findAccountFormByAccountId(accountId));
        return "accounts/putMoney";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Обновление баланса счета")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу пользователя после успешного обновления баланса"
    )
    @ApiResponse(responseCode = "400", description = "Неверные данные")
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @PostMapping("/updateBalance")
    public String updateBalance(@Parameter(description = "ID счета", required = true)
                                @RequestParam("accountId") UUID accountId,
                                @Parameter(description = "Новый баланс", required = true)
                                @RequestParam("balance") Integer balance,
                                Model model) {

        if (balance == null || balance <= 0) {
            model.addAttribute("account", accountService.findAccountFormByAccountId(accountId));
            model.addAttribute("balanceError", "Введите корректный баланс");
            return "accounts/putMoney";
        }
        accountService.updateBalance(balance, accountId);
        UUID idAccountCustomer = accountService.findAccountFormByAccountId(accountId).getCustomer().getId();
        return "redirect:/customer/" + idAccountCustomer;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Страница добавления карты к счету")
    @ApiResponse(
            responseCode = "200",
            description = "Страница добавления карты к счету успешно загружена",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/addCardToAccount")
    public String getAddCardToAccountPage(@Parameter(description = "ID счета", required = true)
                                          @RequestParam("accountId") UUID accountId,
                                          Model model) {
        model.addAttribute("account", accountService.findAccountFormByAccountId(accountId));
        Customer ownerAccount = accountService.findAccountFormByAccountId(accountId).getCustomer();
        model.addAttribute("cardsList", ownerAccount.getCards());
        return "accounts/addCardToAccount";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Добавление карты к счету")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу пользователя после успешного добавления карты к счету"
    )
    @ApiResponse(responseCode = "400", description = "Неверные данные")
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @PostMapping("/addCardToAccount")
    public String addCardToAccount(@Parameter(description = "ID счета", required = true)
                                   @RequestParam("accountId") UUID accountId,
                                   @Parameter(description = "ID карты", required = true)
                                   @RequestParam("cardId") UUID cardId,
                                   Model model) {
        Customer ownerAccount = accountService.findAccountFormByAccountId(accountId).getCustomer();
        try {
            accountService.addCardToAccount(accountId, cardId);
            return "redirect:/customer/" + ownerAccount.getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("customer", ownerAccount);
            return "error";
        }
    }
}
