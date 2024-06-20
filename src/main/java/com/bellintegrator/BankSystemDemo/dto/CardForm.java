package com.bellintegrator.BankSystemDemo.dto;

import com.bellintegrator.BankSystemDemo.model.Account;
import com.bellintegrator.BankSystemDemo.model.Card;
import com.bellintegrator.BankSystemDemo.model.CardType;
import com.bellintegrator.BankSystemDemo.model.Customer;
import lombok.Data;

import java.math.BigInteger;
import java.util.UUID;

@Data
public class CardForm {
    private UUID id;
    private String number;
    private CardType cardType;
    private BigInteger balance;
    private Card.PaymentSystem paymentSystem;
    private Card.Status status;
    private Customer customer;
    private Account account;
}
