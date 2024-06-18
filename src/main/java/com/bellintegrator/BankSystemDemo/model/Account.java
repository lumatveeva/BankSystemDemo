package com.bellintegrator.BankSystemDemo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "account")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String number;
    @Enumerated(EnumType.STRING)

    @Column(name = "account_type")
    private AccountType accountType;
    private Integer balance;

    @ManyToOne
    @JoinColumn(name ="customer_id",
            referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "account")
    private List<Card> cards;

    // Метод для добавления карты с проверкой типа счета
    public void addCard(Card card) {
        if (accountType == AccountType.CREDIT && cards != null && !cards.isEmpty()) {
            throw new IllegalArgumentException("К кредитному счету может быть прикреплена только одна карта");
        }
        if(accountType == AccountType.DEPOSIT){
            throw new IllegalArgumentException("К депозитному счету нельзя прикрепить карту");
        }
        if (cards == null) {
            cards = new ArrayList<>();
        }
        cards.add(card);
    }

}
