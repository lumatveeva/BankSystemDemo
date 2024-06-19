package com.bellintegrator.BankSystemDemo.dto;

import com.bellintegrator.BankSystemDemo.model.Card;
import com.bellintegrator.BankSystemDemo.model.CardType;
import lombok.Data;


import java.math.BigInteger;

@Data
public class CardForm {
    private String number;
    private CardType cardType;
    private BigInteger balance;
    private Card.PaymentSystem paymentSystem;
    private Card.Status status;
}
