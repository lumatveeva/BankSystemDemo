package com.bellintegrator.BankSystemDemo.mappers;

import com.bellintegrator.BankSystemDemo.dto.AccountForm;
import com.bellintegrator.BankSystemDemo.dto.CustomerForm;
import com.bellintegrator.BankSystemDemo.model.Account;
import com.bellintegrator.BankSystemDemo.model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toCustomer(CustomerForm form);
    CustomerForm toCustomerForm(Customer customer);
    List<CustomerForm> toListCustomerForm(List<Customer> customerList);
}
