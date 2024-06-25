
package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.dto.AccountDTO;
import com.bellintegrator.BankSystemDemo.dto.CardDTO;
import com.bellintegrator.BankSystemDemo.exceptions.AccountNotFoundException;
import com.bellintegrator.BankSystemDemo.exceptions.CardNotFoundException;
import com.bellintegrator.BankSystemDemo.mappers.AccountMapper;
import com.bellintegrator.BankSystemDemo.mappers.CardMapper;
import com.bellintegrator.BankSystemDemo.mappers.CustomerMapper;
import com.bellintegrator.BankSystemDemo.model.Account;
import com.bellintegrator.BankSystemDemo.model.AccountType;
import com.bellintegrator.BankSystemDemo.model.Card;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.AccountRepository;
import com.bellintegrator.BankSystemDemo.repository.CardRepository;
import com.bellintegrator.BankSystemDemo.util.AccountNumberGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
    private final CustomerService customerService;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final AccountMapper accountMapper;
    private final CustomerMapper customerMapper;
    private final CardMapper cardMapper;


    public List<AccountDTO> findAllAccountForm() {
        return accountMapper.toListAccountForm(accountRepository.findAll());
    }

    public AccountDTO findAccountFormByAccountId(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return accountMapper.toAccountForm(account);
    }
    public Account findAccountById(UUID accountId){
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    public List<AccountDTO> allAccountsFormByCustomerId(UUID id) {
        return accountMapper.toListAccountForm(accountRepository.findByCustomerId(id));

    }

    @Transactional
    public Account createAccount(AccountDTO accountDTO, UUID customerId) {

        Customer customer = customerMapper.toCustomer(customerService.findCustomerFormByCustomerId(customerId));
        Account newAccount = accountMapper.toAccount(accountDTO);
        newAccount.setCustomer(customer);
        newAccount.setNumber(AccountNumberGenerator.generateAccountNumber());
        newAccount.setBalance(0);
        return accountRepository.save(newAccount);
    }

    @Transactional
    public void updateBalance(Integer balance, UUID accountId) {

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
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Card not found"));
        CardDTO cardDTO = cardMapper.toCardForm(card);

        addCard(account, cardDTO);
        card.setAccount(account);

        accountRepository.save(account);
        cardRepository.save(card);

    }

    private void addCard(Account account, CardDTO cardDTO) {
        Card card = cardMapper.toCard(cardDTO);
        if (account.getAccountType() == AccountType.CREDIT && !account.getCards().isEmpty()) {
            throw new IllegalArgumentException("К кредитному счету может быть прикреплена только одна карта");
        }
        if (account.getAccountType() == AccountType.DEPOSIT) {
            throw new IllegalArgumentException("К депозитному счету нельзя прикрепить карту");
        }
        if(account.getAccountType().getCorrespondingCardType() != cardDTO.getCardType()){
            throw new IllegalArgumentException("Тип карты и банковского счета должны быть одинаковыми");
        }
        account.getCards().add(card);
    }

}
