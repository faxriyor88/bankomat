package com.example.bankomat.service;

import com.example.bankomat.dto.CardDto;
import com.example.bankomat.entity.Card;
import com.example.bankomat.entity.enums.Rolename;
import com.example.bankomat.repository.*;
import com.example.bankomat.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class OperationCardService {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BankomatRepository bankomatRepository;
    @Autowired
    CompletionMoneyBankomatRepository completionMoneyBankomatRepository;
    @Autowired
    ExchangeHistoryRepository exchangeHistoryRepository;
    @Autowired
    ExchangeService exchangeService;


    //Kartani blokdan chiqarish
    public ApiResponse unBlockedCard(CardDto cardDto) {
        if (exchangeService.role(Rolename.ROLE_RESPONSIBLE)) {
            Optional<Card> cardOptional = cardRepository.findBySpecialNumberAndPincode(cardDto.getSpecialNumber(), cardDto.getPassword());
            if (cardOptional.isPresent()) {
                Card card = cardOptional.get();
                card.setEnabled(true);
                card.setBlockedCount(0);
                cardRepository.save(card);
                return new ApiResponse("Card aktivlashtirildi", true);
            } else {
                return new ApiResponse("Bunday card topilmadi", false);
            }

        } else {
            return new ApiResponse("Sizda bu operatsiyani bajarish uchun huquq yo'q", false);
        }

    }
}
