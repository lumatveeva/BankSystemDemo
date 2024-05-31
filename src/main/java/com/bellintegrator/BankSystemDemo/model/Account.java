package com.bellintegrator.BankSystemDemo.model;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID number;
    @Enumerated(EnumType.STRING)
    private Type type;
    private BigInteger balance;

    @ManyToOne
    @JoinColumn(name ="user_id",
            referencedColumnName = "id")
    private User user;

}
