package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.dto.AccountForm;
import com.bellintegrator.BankSystemDemo.dto.CustomerForm;
import com.bellintegrator.BankSystemDemo.exceptions.CardNotFoundException;
import com.bellintegrator.BankSystemDemo.mappers.AccountMapper;
import com.bellintegrator.BankSystemDemo.mappers.CustomerMapper;
import com.bellintegrator.BankSystemDemo.model.*;
import com.bellintegrator.BankSystemDemo.repository.CardRepository;
import com.bellintegrator.BankSystemDemo.util.CardNumberGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CardService {
    private final AccountService accountService;
    public final CardRepository cardRepository;
    private final CustomerService customerService;
    private final AccountMapper accountMapper;
    private final CustomerMapper customerMapper;

    public List<Card> findAll(){
        return cardRepository.findAll();
    }
    public List<Card> getCardsByCustomerId(UUID id) {
        return cardRepository.findByCustomerId(id);
    }
    public Card findById(UUID id){
        return cardRepository.findById(id).orElseThrow(()-> new CardNotFoundException("Card not found"));
    }

    @Transactional
    public void createCard(UUID id, Card card){

        AccountType accountType = selectAccountType(card);

        Account account = new Account();
        account.setAccountType(accountType);
        account.setCustomer(card.getCustomer());
        AccountForm accountForm = accountMapper.toAccountForm(account);
        accountService.createAccount(accountForm, id);

        CustomerForm customerForm = customerService.findCustomerFormByCustomerId(id);
        Customer customer = customerMapper.toCustomer(customerForm);

        card.setCustomer(customer);
        card.setAccount(account);
        card.setNumber(CardNumberGenerator.generateCardNumber());
        card.setStatus(Card.Status.ACTIVE);
        card.setBalance(0);

        cardRepository.save(card);
    }

    @Transactional
    public void updateBalance(Integer balance, UUID cardId) {

        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Account not found"));
        card.setBalance(balance + card.getBalance());
        Account account = card.getAccount();
        accountService.updateBalance(balance, account.getId());
        cardRepository.save(card);
    }

    @Transactional
    public void withdrawMoney(Integer balance, UUID cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Account not found"));
        Integer currentBalance = card.getBalance();
        if(balance > currentBalance){
            throw new IllegalArgumentException("На карте недостаточно средств");
        }
        card.setBalance(currentBalance - balance);
        Account account = card.getAccount();
        accountService.updateBalance(balance, account.getId());
        cardRepository.save(card);
    }


    private static AccountType selectAccountType(Card card) {
        AccountType accountType = switch (card.getCardType()) {
            case CREDIT -> AccountType.CREDIT;
            case DEBIT -> AccountType.DEBIT;
        };

        return accountType;
    }

    @Transactional
    public void lockCard(UUID cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Card not found"));
        card.setStatus(Card.Status.LOCKED);
        cardRepository.save(card);
    }

    @Transactional
    public void save(UUID cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(()-> new CardNotFoundException("Card not found"));
        cardRepository.save(card);
    }

    @Transactional
    public void closeCard(UUID cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Card not found"));
        card.setStatus(Card.Status.CLOSE);
        cardRepository.save(card);
    }
}
