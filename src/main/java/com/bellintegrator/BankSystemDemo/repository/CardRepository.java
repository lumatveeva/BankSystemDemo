package com.bellintegrator.BankSystemDemo.repository;

import com.bellintegrator.BankSystemDemo.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
}
