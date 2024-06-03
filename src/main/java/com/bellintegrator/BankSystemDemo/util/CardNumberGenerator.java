package com.bellintegrator.BankSystemDemo.util;

import java.util.Random;

public class CardNumberGenerator {
    private static final Random RANDOM = new Random();

    public static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i > 0) {
                cardNumber.append(" ");
            }
            cardNumber.append(String.format("%04d", RANDOM.nextInt(10000)));
        }
        return cardNumber.toString();
    }
}
