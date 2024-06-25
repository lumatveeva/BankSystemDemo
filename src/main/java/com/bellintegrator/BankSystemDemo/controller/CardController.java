package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.dto.CardDTO;
import com.bellintegrator.BankSystemDemo.model.Card;
import com.bellintegrator.BankSystemDemo.model.CardType;
import com.bellintegrator.BankSystemDemo.service.CardService;
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
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Отображение всех карт пользователя по его ID")
    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение списка всех карт пользователя",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/customer/{id}")
    public String findCardsByCustomerId(@Parameter(description = "ID пользователя", required = true)
                                        @PathVariable("id") UUID id,
                                        Model model) {
        model.addAttribute("cards", cardService.getCardsByCustomerId(id));
        return "cards/allCardsByCustomer";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение списка всех карт",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @GetMapping("/all")
    public String allCards(Model model) {
        model.addAttribute("cards", cardService.findAll());
        return "cards/allCards";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Отображение информации о карте по ее ID")
    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение информации о карте",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/{id}")
    public String findById(@Parameter(description = "ID карты", required = true)
                           @PathVariable("id") UUID id,
                           Model model) {
        model.addAttribute("card", cardService.findById(id));
        return "cards/byId";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Создание новой карты")
    @ApiResponse(
            responseCode = "200",
            description = "Страница создания новой карты успешно загружена",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/new")
    public String createCard(@Parameter(description = "ID пользователя", required = true)
                             @RequestParam("customerId") UUID customerId, Model model) {
        model.addAttribute("card", new Card());
        model.addAttribute("customerId", customerId);
        model.addAttribute("cardTypes", CardType.values());
        model.addAttribute("paymentsSystem", Card.PaymentSystem.values());
        return "cards/new";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Добавление новой карты")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу пользователя после успешного добавления карты"
    )
    @ApiResponse(responseCode = "400", description = "Неверные данные")
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @PostMapping("/addCard")
    public String addACard(@Parameter(description = "Данные новой карты", required = true)
                           @ModelAttribute("card") @Valid Card card,
                           @Parameter(description = "ID пользователя", required = true)
                           @RequestParam("customerId") UUID customerId,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cardTypes", CardType.values());
            model.addAttribute("paymentsSystem", Card.PaymentSystem.values());
            return "cards/new";
        }
        cardService.createCard(customerId, card);
        return "redirect:/customer/" + customerId;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Пополнение карты")
    @ApiResponse(
            responseCode = "200",
            description = "Страница пополнения карты успешно загружена",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/putMoney")
    public String putMoney(@Parameter(description = "ID карты", required = true)
                           @RequestParam("cardId") UUID cardId,
                           Model model) {
        model.addAttribute("card", cardService.findById(cardId));
        return "cards/putMoney";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Обновление баланса карты")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу пользователя после успешного обновления баланса"
    )
    @ApiResponse(responseCode = "400", description = "Неверные данные")
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @PostMapping("/updateBalance")
    public String updateBalance(@Parameter(description = "ID карты", required = true)
                                @RequestParam("cardId") UUID cardId,
                                @Parameter(description = "Новый баланс", required = true)
                                @RequestParam("balance") Integer balance,
                                Model model) {

        if (balance == null || balance <= 0) {
            model.addAttribute("card", cardService.findById(cardId));
            model.addAttribute("balanceError", "Введите корректный баланс");
            return "cards/putMoney";
        }
        cardService.updateBalance(balance, cardId);
        Card updatedCard = cardService.findById(cardId);
        UUID idCardCustomer = updatedCard.getCustomer().getId();
        return "redirect:/customer/" + idCardCustomer;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Страница снятия денег с карты")
    @ApiResponse(
            responseCode = "200",
            description = "Страница снятия денег с карты успешно загружена",
            content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @GetMapping("/withdraw")
    public String getWithdrawPage(@Parameter(description = "ID карты", required = true)
                                  @RequestParam("cardId") UUID cardId,
                                  Model model) {
        model.addAttribute("card", cardService.findById(cardId));
        return "/cards/withdraw";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Снятие денег с карты")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу пользователя после успешного снятия денег с карты"
    )
    @ApiResponse(responseCode = "400", description = "Неверные данные")
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @PostMapping("/withdraw")
    public String withdrawMoney(@Parameter(description = "ID карты", required = true)
                                @RequestParam("cardId") UUID cardId,
                                @Parameter(description = "Сумма снятия", required = true)
                                @RequestParam("balance") Integer balance,
                                Model model) {
        if (balance == null || balance <= 0) {
            model.addAttribute("card", cardService.findById(cardId));
            model.addAttribute("balanceError", "Введите корректный баланс");
            return "cards/putMoney";
        }
        ;
        cardService.withdrawMoney(balance, cardId);
        Card updatedCard = cardService.findById(cardId);
        UUID idCardCustomer = updatedCard.getCustomer().getId();
        return "redirect:/customer/" + idCardCustomer;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Блокировка карты")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу блокировки карты после успешной блокировки"
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @PostMapping("/lock")
    public String lockCard(@Parameter(description = "ID карты", required = true)
                           @RequestParam("cardId") UUID cardId,
                           Model model) {
        cardService.lockCard(cardId);
        Card card = cardService.findById(cardId);
        model.addAttribute("customerId", card.getCustomer().getId());
        return "cards/lockCard";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Закрытие карты")
    @ApiResponse(
            responseCode = "302",
            description = "Перенаправление на страницу блокировки карты после успешного закрытия карты"
    )
    @ApiResponse(responseCode = "403", description = "Запрещено")
    @PostMapping("/close")
    public String closeCard(@Parameter(description = "ID карты", required = true)
                            @RequestParam("cardId") UUID cardId,
                            Model model) {
        cardService.closeCard(cardId);
        Card card = cardService.findById(cardId);
        model.addAttribute("customerId", card.getCustomer().getId());
        return "cards/lockCard";
    }
}
