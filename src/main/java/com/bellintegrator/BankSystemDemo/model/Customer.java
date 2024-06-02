package com.bellintegrator.BankSystemDemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Пожалуйста введите имя")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "Пожалуйста введите фамилию")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Email должен соответствовать шаблону user@mail.com")
    @Column(unique = true)
    @NotNull(message = "Пожалуйста введите email")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Pattern(regexp = "\\d{4} \\d{6}", message = "Номер паспорта должен соответствовать шаблону '1111 111111'")
    private String passport;

    @NotNull(message = "Пожалуйста введите пароль")
    private String password;

    @OneToMany(mappedBy = "number")
    private List<Account> accounts;

    @OneToMany(mappedBy = "number")
    private List<Card> cards;
    
    public enum Role{
        ROLE_USER, ROLE_ADMIN
    }

}
