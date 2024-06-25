package com.bellintegrator.BankSystemDemo.dto;

import com.bellintegrator.BankSystemDemo.model.Account;
import com.bellintegrator.BankSystemDemo.model.Card;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CustomerDTO {

    private UUID id;

    @NotNull(message = "Пожалуйста введите имя")
    private String firstName;

    @NotNull(message = "Пожалуйста введите фамилию")
    private String lastName;

    @Email(message = "Email должен соответствовать шаблону user@mail.com")
    @NotNull(message = "Пожалуйста введите email")
    private String email;

    @Pattern(regexp = "\\d{4} \\d{6}", message = "Номер паспорта должен соответствовать шаблону '1111 111111'")
    private String passport;

    @NotNull(message = "Пожалуйста введите пароль")
    private String password;


    private List<Account> accounts;
    private List<Card> cards;

}
