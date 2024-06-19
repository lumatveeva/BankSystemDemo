package com.bellintegrator.BankSystemDemo.dto;

import com.bellintegrator.BankSystemDemo.model.Account;
import com.bellintegrator.BankSystemDemo.model.Card;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CustomerForm {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String passport;
    private String password;
    private List<Account> accounts;
    private List<Card> cards;

}
