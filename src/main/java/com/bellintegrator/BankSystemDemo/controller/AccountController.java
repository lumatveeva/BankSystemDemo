package com.bellintegrator.BankSystemDemo.controller;


import com.bellintegrator.BankSystemDemo.model.Account;
import com.bellintegrator.BankSystemDemo.model.AccountType;
import com.bellintegrator.BankSystemDemo.model.Card;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.service.AccountService;
import com.bellintegrator.BankSystemDemo.service.CardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
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
        model.addAttribute("accounts", accountService.findAll());
        return "accounts/allAccounts";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("customer/{id}")
    public String findAllByCustomerId(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("accounts", accountService.allAccountsByCustomerId(id));
        return "accounts/allAccountsByCustomer";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") UUID id,
                           Model model) {
        model.addAttribute("account", accountService.findById(id));
        return "accounts/byId";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/new")
    public String createAccount(@RequestParam("userId") UUID userId,
                                Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("userId", userId);
        model.addAttribute("accountTypes", AccountType.values());
        return "accounts/new";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/addAccount")
    public String addAccount(@ModelAttribute("account") @Valid Account account,
                             @RequestParam("userId") UUID userId,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountTypes", AccountType.values());
            return "accounts/new";
        }
        accountService.createAccount(account, userId);
        return "redirect:/customer/" + userId;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/putMoney")
    public String putMoney(@RequestParam("accountId") UUID accountId,
                           Model model) {
        model.addAttribute("account", accountService.findById(accountId));
        return "accounts/putMoney";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/updateBalance")
    public String updateBalance(@RequestParam("accountId") UUID accountId,
                                @RequestParam("balance") Integer balance,
                                Model model) {

        if (balance == null || balance <= 0) {
            model.addAttribute("account", accountService.findById(accountId));
            model.addAttribute("balanceError", "Введите корректный баланс");
            return "accounts/putMoney";
        }
        accountService.updateBalance(balance, accountId);
        Account updatedAccount = accountService.findById(accountId);
        UUID idAccountCustomer = updatedAccount.getCustomer().getId();
        return "redirect:/customer/" + idAccountCustomer;
    }

    @GetMapping("/addCardToAccount")
    public String getAddCardToAccountPage(@RequestParam("accountId") UUID accountId,
                                          Model model){
        model.addAttribute("account", accountService.findById(accountId));
        Customer ownerAccount = accountService.findById(accountId).getCustomer();
        model.addAttribute("cardsList", ownerAccount.getCards());
        return "accounts/addCardToAccount";
    }

    @PostMapping("/addCardToAccount")
    public String addCardToAccount(@RequestParam("accountId") UUID accountId,
                                   @RequestParam("cardId") UUID cardId,
                                   BindingResult bindingResult){
        accountService.addCardToAccount(accountId, cardId);
        Customer ownerAccount = accountService.findById(accountId).getCustomer();

        return "redirect:/customer/" + ownerAccount.getId();

    }
}
