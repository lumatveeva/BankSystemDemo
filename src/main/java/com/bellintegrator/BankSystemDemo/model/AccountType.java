package com.bellintegrator.BankSystemDemo.model;

public enum AccountType {
    DEPOSIT, DEBIT, CREDIT;
    public CardType getCorrespondingCardType() {
        switch (this) {
            case DEBIT:
                return CardType.DEBIT;
            case CREDIT:
                return CardType.CREDIT;
            default:
                throw new IllegalArgumentException("Не найдено соответсвующего типа банковской карты для счета: " + this);
        }
    }
}
