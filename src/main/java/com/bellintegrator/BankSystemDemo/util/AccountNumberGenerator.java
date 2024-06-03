package com.bellintegrator.BankSystemDemo.util;

import java.util.Random;

public class AccountNumberGenerator {
    private static final Random RANDOM = new Random();

    public static String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            accountNumber.append(RANDOM.nextInt(10));
        }
        return accountNumber.toString();
    }
}
