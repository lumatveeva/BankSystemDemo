package com.bellintegrator.BankSystemDemo.mappers;

import com.bellintegrator.BankSystemDemo.dto.CardDTO;
import com.bellintegrator.BankSystemDemo.model.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    Card toCard(CardDTO form);

    CardDTO toCardForm(Card card);
}
