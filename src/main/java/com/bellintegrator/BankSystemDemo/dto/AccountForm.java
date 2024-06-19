package com.bellintegrator.BankSystemDemo.dto;

import com.bellintegrator.BankSystemDemo.model.AccountType;
import com.bellintegrator.BankSystemDemo.model.Card;
import com.bellintegrator.BankSystemDemo.model.Customer;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Data
public class AccountForm {
    private UUID id;
    private String number;
    private AccountType accountType;
    private BigInteger balance;
    private Customer customer;
    private List<Card> cards;

}
