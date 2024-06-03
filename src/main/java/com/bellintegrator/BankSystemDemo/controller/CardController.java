package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.model.Account;
import com.bellintegrator.BankSystemDemo.model.Card;
import com.bellintegrator.BankSystemDemo.model.CardType;
import com.bellintegrator.BankSystemDemo.service.CardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;

    @GetMapping("/customer/{id}")
    public String findCardsByCustomerId(Model model, @PathVariable("id") UUID id){
        model.addAttribute("cards", cardService.getCardsByCustomerId(id));
        return "cards/allCardsByCustomer";
    }

    @GetMapping("/all")
    public String allCards(Model model){
        model.addAttribute("cards", cardService.findAll());
        return "cards/allCards";
    }
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") String id,
                           Model model){
        model.addAttribute("card", cardService.findById(UUID.fromString(id)));
        return "cards/byId";
    }
    @GetMapping("/new")
    public String createCard(@RequestParam("userId") UUID userId, Model model){
        model.addAttribute("card", new Card());
        model.addAttribute("userId", userId);
        model.addAttribute("cardTypes", CardType.values());
        model.addAttribute("paymentsSystem", Card.PaymentSystem.values());
        return "cards/new";
    }
    @PostMapping("/addCard")
    public String addACard(@ModelAttribute("card") @Valid Card card,
                           @RequestParam("userId") UUID userId,
                           BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("cardTypes", CardType.values());
            model.addAttribute("paymentsSystem", Card.PaymentSystem.values());
            return "cards/new";
        }
        cardService.createCard(userId, card);
        return "redirect:/customer/" + userId;
    }

    @GetMapping("/putMoney")
    public String putMoney(Model model){
        model.addAttribute("account", new Account());
        return "cards/putMoney";
    }

    @PostMapping("/updateBalance")
    public String updateBalance(@ModelAttribute("number") UUID number,
                                @ModelAttribute("balance") BigInteger balance,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "cards/putMoney";
        }
        cardService.updateBalance(balance,number);
        return "redirect:/cards";
    }
}
