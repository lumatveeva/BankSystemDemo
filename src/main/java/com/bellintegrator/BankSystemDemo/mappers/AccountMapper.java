package com.bellintegrator.BankSystemDemo.mappers;

import com.bellintegrator.BankSystemDemo.dto.AccountDTO;
import com.bellintegrator.BankSystemDemo.model.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(AccountDTO form);

    AccountDTO toAccountForm(Account account);

    List<AccountDTO> toListAccountForm(List<Account> accountList);
}
