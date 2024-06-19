package com.bellintegrator.BankSystemDemo.dto;

import com.bellintegrator.BankSystemDemo.model.AccountType;
import lombok.Data;

import java.math.BigInteger;

@Data
public class AccountForm {
    private String number;
    private AccountType accountType;
    private BigInteger balance;

}
