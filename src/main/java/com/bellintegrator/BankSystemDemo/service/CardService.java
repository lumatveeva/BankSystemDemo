package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.exceptions.AccountNotFoundException;
import com.bellintegrator.BankSystemDemo.exceptions.CardNotFoundException;
import com.bellintegrator.BankSystemDemo.exceptions.CustomerNotFoundException;
import com.bellintegrator.BankSystemDemo.exceptions.InvalidBalanceException;
import com.bellintegrator.BankSystemDemo.model.*;
import com.bellintegrator.BankSystemDemo.repository.AccountRepository;
import com.bellintegrator.BankSystemDemo.repository.CardRepository;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CardService {
    private final AccountRepository accountRepository;
    public final CardRepository cardRepository;
    private final CustomerRepository customerRepository;

    public List<Card> findAll(){
        return cardRepository.findAll();
    }
    public List<Card> getCardsByCustomerId(UUID id) {
        return cardRepository.findByCustomerId(id);
    }
    public Card findById(UUID id){
        return cardRepository.findById(id).orElseThrow(()-> new CardNotFoundException("Card not found"));
    }

    public void createCard(UUID id, Card card){

        AccountType accountType;

            switch (card.getCardType()) {
            case CREDIT:
                accountType = AccountType.CREDIT_CARD;
                break;
            case DEBIT:
                accountType = AccountType.DEBIT_CARD;
                break;
            default:
                throw new RuntimeException("Unsupported card type");
        }
        List<Card> existingCards = cardRepository.findByCustomerId(card.getCustomer().getId());
        if (accountType == AccountType.CREDIT_CARD && existingCards.stream().anyMatch(c -> c.getCardType() == CardType.CREDIT)) {
            throw new RuntimeException("Credit card account can only have one credit card linked to it");
        }
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new CustomerNotFoundException("Customer not found"));

        Account account = new Account();
            account.setAccountType(accountType);
            account.setCustomer(card.getCustomer());
        Account savedAccount = accountRepository.save(account);
            card.setCustomer(customer);
            card.setAccountList(List.of(savedAccount));

            cardRepository.save(card);
    }

    public void updateBalance(BigInteger balance, UUID number) {
        if (balance.compareTo(BigInteger.ZERO) < 0) {
            throw new InvalidBalanceException("Balance cannot be negative");
        }
        Card card = cardRepository.findById(number).orElseThrow(() -> new CardNotFoundException("Account not found"));
        card.setBalance(balance);
        cardRepository.save(card);
    }
}
