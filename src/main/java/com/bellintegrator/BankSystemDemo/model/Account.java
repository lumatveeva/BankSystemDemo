package com.bellintegrator.BankSystemDemo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
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
    private BigInteger balance;

    @ManyToOne
    @JoinColumn(name ="customer_id",
            referencedColumnName = "id")
    private Customer customer;

    @ManyToMany
    private List<Card> cardsList;

}
