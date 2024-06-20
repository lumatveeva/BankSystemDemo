package com.bellintegrator.BankSystemDemo.mappers;

import com.bellintegrator.BankSystemDemo.dto.AccountForm;
import com.bellintegrator.BankSystemDemo.model.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(AccountForm form);

    AccountForm toAccountForm(Account account);

    List<AccountForm> toListAccountForm(List<Account> accountList);
}
