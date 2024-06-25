package com.bellintegrator.BankSystemDemo.mappers;

import com.bellintegrator.BankSystemDemo.dto.CustomerDTO;
import com.bellintegrator.BankSystemDemo.model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toCustomer(CustomerDTO form);

    CustomerDTO toCustomerForm(Customer customer);

    List<CustomerDTO> toListCustomerForm(List<Customer> customerList);
}
