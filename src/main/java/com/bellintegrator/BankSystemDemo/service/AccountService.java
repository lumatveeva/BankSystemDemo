package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.exceptions.AccountNotFoundException;
import com.bellintegrator.BankSystemDemo.exceptions.CardNotFoundException;
import com.bellintegrator.BankSystemDemo.exceptions.CustomerNotFoundException;
import com.bellintegrator.BankSystemDemo.exceptions.InvalidBalanceException;
import com.bellintegrator.BankSystemDemo.model.*;
import com.bellintegrator.BankSystemDemo.repository.AccountRepository;
import com.bellintegrator.BankSystemDemo.repository.CardRepository;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import com.bellintegrator.BankSystemDemo.util.AccountNumberGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
    private final CustomerService customerService;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;


    public List<Account> findAll() {
        return accountRepository.findAll();
    }
    public Account findById(UUID id){
         return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }
    public List<Account> allAccountsByCustomerId(UUID id){
        return accountRepository.findByCustomerId(id);
    }

    @Transactional
    public Account createAccount(Account account, UUID customerId){

        Customer customer = customerService.findById(customerId);

        account.setCustomer(customer);
        account.setNumber(AccountNumberGenerator.generateAccountNumber());
        account.setBalance(0);
        accountRepository.save(account);
        return account;
    }

    @Transactional
    public void updateBalance(Integer balance, UUID accountId){
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        account.setBalance(balance + account.getBalance());
        accountRepository.save(account);
    }

    @Transactional
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    public void addCardToAccount(UUID accountId, UUID cardId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        Card card = cardRepository.findById(cardId).orElseThrow(()->new CardNotFoundException("Card not found"));

        account.addCard(card);
        card.setAccount(account);

        accountRepository.save(account);
        cardRepository.save(card);

    }
}
