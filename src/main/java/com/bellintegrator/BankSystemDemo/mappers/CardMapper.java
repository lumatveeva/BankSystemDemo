package com.bellintegrator.BankSystemDemo.mappers;

import com.bellintegrator.BankSystemDemo.dto.CardForm;
import com.bellintegrator.BankSystemDemo.model.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    Card toCard(CardForm form);

    CardForm toCardForm(Card card);
}
