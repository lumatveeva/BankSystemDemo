package com.bellintegrator.BankSystemDemo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "card")
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type")
    private CardType cardType;

    private BigInteger balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_system")
    private PaymentSystem paymentSystem;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name ="customer_id",
            referencedColumnName = "id")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "account_card",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> accounts;

    public enum Status{
        ACTIVE, LOCKED, CLOSE
    }
    public enum PaymentSystem{
        VISA, MASTERCARD
    }

}
