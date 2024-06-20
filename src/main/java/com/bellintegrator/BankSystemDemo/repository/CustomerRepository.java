package com.bellintegrator.BankSystemDemo.repository;

import com.bellintegrator.BankSystemDemo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findCustomerByEmail(String email);

    Optional<Customer> findByPassport(String passport);

}
