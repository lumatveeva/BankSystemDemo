package com.bellintegrator.BankSystemDemo.controller;


import com.bellintegrator.BankSystemDemo.model.Account;
import com.bellintegrator.BankSystemDemo.model.AccountType;
import com.bellintegrator.BankSystemDemo.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

    @GetMapping("/all")
    public String allAccounts(Model model){
        model.addAttribute("accounts", accountService.findAll());
        return "accounts/allAccounts";
    }
    @GetMapping("customer/{id}")
    public String findAllByCustomerId(Model model, @PathVariable("id") UUID id){
        model.addAttribute("accounts", accountService.allAccountsByCustomerId(id));
        return "accounts/allAccountsByCustomer";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") UUID id,
                           Model model){
        model.addAttribute("account", accountService.findById(id));
        return "accounts/byId";
    }

    @GetMapping("/new")
    public String createAccount(Model model){
        model.addAttribute("account", new Account());
        model.addAttribute("accountTypes", AccountType.values());
        return "accounts/new";
    }
    @PostMapping("")
    public String addAccount(@ModelAttribute("account") @Valid Account account,
                             BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "accounts/new";
        }
        accountService.createAccount(account);
        return "redirect:/accounts";
    }

    @GetMapping("/putMoney")
    public String putMoney(Model model){
        model.addAttribute("account", new Account());
        return "accounts/putMoney";
    }

    @PostMapping("/updateBalance")
    public String updateBalance(@ModelAttribute("number") UUID number,
                                @ModelAttribute("balance")BigInteger balance,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "accounts/putMoney";
        }
        accountService.updateBalance(balance,number);
        return "redirect:/accounts";
    }
}
