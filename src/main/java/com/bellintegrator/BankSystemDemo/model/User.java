package com.bellintegrator.BankSystemDemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;

    @Email
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Pattern(regexp = "\\d{4} \\d{6}", message = "Номер паспорта должен соответствовать шаблону '1111 111111'")
    private String passport;
    private String password;

    @OneToMany()
    @Column(name = "account_number")
    private List<Account> accounts;

    @OneToMany(mappedBy = "number")
    @Column(name = "card_number")
    private List<Card> cards;
    
    public enum Role{
        USER, ADMIN
    }

}
