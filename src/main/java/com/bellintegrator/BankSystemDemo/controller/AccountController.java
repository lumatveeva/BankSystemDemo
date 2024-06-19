package com.bellintegrator.BankSystemDemo.controller;


import com.bellintegrator.BankSystemDemo.dto.AccountForm;
import com.bellintegrator.BankSystemDemo.mappers.AccountMapper;
import com.bellintegrator.BankSystemDemo.model.Account;
import com.bellintegrator.BankSystemDemo.model.AccountType;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.service.AccountService;
import com.bellintegrator.BankSystemDemo.service.CardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    private final CardService cardService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public String allAccounts(Model model) {

        model.addAttribute("accounts", accountService.findAllAccountForm());
        return "accounts/allAccounts";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("customer/{id}")
    public String findAllByCustomerId(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("accounts", accountService.allAccountsFormByCustomerId(id));
        return "accounts/allAccountsByCustomer";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") UUID id,
                           Model model) {
        model.addAttribute("account", accountService.findAccountFormByAccountId(id));
        return "accounts/byId";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/new")
    public String createAccount(@RequestParam("customerId") UUID customerId,
                                Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("customerId", customerId);
        model.addAttribute("accountTypes", AccountType.values());
        return "accounts/new";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/addAccount")
    public String addAccount(@ModelAttribute("account") @Valid AccountForm accountForm,
                             @RequestParam("customerId") UUID customerId,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountTypes", AccountType.values());
            return "accounts/new";
        }
        accountService.createAccount(accountForm, customerId);
        return "redirect:/customer/" + customerId;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/putMoney")
    public String putMoney(@RequestParam("accountId") UUID accountId,
                           Model model) {
        model.addAttribute("account", accountService.findAccountFormByAccountId(accountId));
        return "accounts/putMoney";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/updateBalance")
    public String updateBalance(@RequestParam("accountId") UUID accountId,
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

    @GetMapping("/addCardToAccount")
    public String getAddCardToAccountPage(@RequestParam("accountId") UUID accountId,
                                          Model model){
        model.addAttribute("account", accountService.findAccountFormByAccountId(accountId));
        Customer ownerAccount = accountService.findAccountFormByAccountId(accountId).getCustomer();
        model.addAttribute("cardsList", ownerAccount.getCards());
        return "accounts/addCardToAccount";
    }

    @PostMapping("/addCardToAccount")
    public String addCardToAccount(@RequestParam("accountId") UUID accountId,
                                   @RequestParam("cardId") UUID cardId,
                                   Model model){
        Customer ownerAccount = accountService.findAccountFormByAccountId(accountId).getCustomer();
        try{
            accountService.addCardToAccount(accountId, cardId);
            return "redirect:/customer/" + ownerAccount.getId();
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("customer", ownerAccount);
            return "error";
        }

    }
}
