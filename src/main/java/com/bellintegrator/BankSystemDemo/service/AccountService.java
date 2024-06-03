package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.exceptions.AccountNotFoundException;
import com.bellintegrator.BankSystemDemo.exceptions.CustomerNotFoundException;
import com.bellintegrator.BankSystemDemo.exceptions.InvalidBalanceException;
import com.bellintegrator.BankSystemDemo.model.*;
import com.bellintegrator.BankSystemDemo.repository.AccountRepository;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;


    public List<Account> findAll() {
        return accountRepository.findAll();
    }
    public Account findById(UUID id){
         return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }
    public List<Account> allAccountsByCustomerId(UUID id){
        return accountRepository.findByCustomerId(id);
    }

    public void createAccount(Account account, UUID id){

        AccountType accountType;

        switch (account.getAccountType()) {
            case CREDIT_CARD:
                accountType = AccountType.CREDIT_CARD;
                break;
            case DEBIT_CARD:
                accountType = AccountType.DEBIT_CARD;
                break;
            case DEPOSIT:
                accountType = AccountType.DEPOSIT;
                break;
            default:
                throw new RuntimeException("Unsupported card type");
        }


        Customer customer = customerRepository.findById(id).orElseThrow(()-> new CustomerNotFoundException("Customer not found"));

        account.setAccountType(accountType);
        account.setCustomer(customer);
        accountRepository.save(account);
    }

    public void updateBalance(BigInteger balance, UUID number){
        if (balance.compareTo(BigInteger.ZERO) < 0) {
            throw new InvalidBalanceException("Balance cannot be negative");
        }
        Account account = accountRepository.findById(number).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        account.setBalance(balance);
        accountRepository.save(account);
    }

}
