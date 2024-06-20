package com.bellintegrator.BankSystemDemo.model;

import jakarta.persistence.*;
import lombok.Data;

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

    private Integer balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_system")
    private PaymentSystem paymentSystem;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "customer_id",
            referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id")
    private Account account;

    public enum Status {
        ACTIVE, LOCKED, CLOSE
    }

    public enum PaymentSystem {
        VISA, MASTERCARD
    }

}
