package com.bellintegrator.BankSystemDemo.mappers;

import com.bellintegrator.BankSystemDemo.dto.CustomerForm;
import com.bellintegrator.BankSystemDemo.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toCustomer(CustomerForm form);
    CustomerForm toCustomerForm(Customer customer);
}
