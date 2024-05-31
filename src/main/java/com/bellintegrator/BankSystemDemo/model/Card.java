package com.bellintegrator.BankSystemDemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigInteger;

import java.util.UUID;

@Entity
@Table(name = "card")
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID number;

    @Enumerated(EnumType.STRING)
    private Type type;
    private BigInteger balance;

    @Enumerated(EnumType.STRING)
    private PaymentSystem paymentSystem;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name ="user_id",
    referencedColumnName = "id")
    private User user;

    public enum Status{
        ACTIVE, LOCKED, CLOSE
    }
    public enum PaymentSystem{
        VISA, MASTERCARD
    }

}
