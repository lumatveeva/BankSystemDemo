package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.model.Card;
import com.bellintegrator.BankSystemDemo.model.CardType;
import com.bellintegrator.BankSystemDemo.service.CardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
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

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") UUID id,
                           Model model){
        model.addAttribute("card", cardService.findById(id));
        return "cards/byId";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/new")
    public String createCard(@RequestParam("customerId") UUID customerId, Model model){
        model.addAttribute("card", new Card());
        model.addAttribute("customerId", customerId);
        model.addAttribute("cardTypes", CardType.values());
        model.addAttribute("paymentsSystem", Card.PaymentSystem.values());
        return "cards/new";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/addCard")
    public String addACard(@ModelAttribute("card") @Valid Card card,
                           @RequestParam("customerId") UUID customerId,
                           BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("cardTypes", CardType.values());
            model.addAttribute("paymentsSystem", Card.PaymentSystem.values());
            return "cards/new";
        }
        cardService.createCard(customerId, card);
        return "redirect:/customer/" + customerId;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/putMoney")
    public String putMoney(@RequestParam("cardId") UUID cardId,
                           Model model){
        model.addAttribute("card", cardService.findById(cardId));
        return "cards/putMoney";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/updateBalance")
    public String updateBalance(@RequestParam("cardId") UUID cardId,
                                @RequestParam("balance") Integer balance,
                                Model model){

        if (balance == null || balance <= 0) {
            model.addAttribute("card", cardService.findById(cardId));
            model.addAttribute("balanceError", "Введите корректный баланс");
            return "cards/putMoney";
        }
        cardService.updateBalance(balance,cardId);
        Card updatedCard = cardService.findById(cardId);
        UUID idCardCustomer = updatedCard.getCustomer().getId();
        return "redirect:/customer/" + idCardCustomer;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/lock")
    public String lockCard(@RequestParam("cardId") UUID cardId,
                           Model model){
        cardService.lockCard(cardId);
        Card card = cardService.findById(cardId);
        model.addAttribute("customerId", card.getCustomer().getId());
        return "cards/lockCard";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/close")
    public String closeCard(@RequestParam("cardId") UUID cardId,
                           Model model){
        cardService.closeCard(cardId);
        Card card = cardService.findById(cardId);
        model.addAttribute("customerId", card.getCustomer().getId());
        return "cards/lockCard";
    }
}
