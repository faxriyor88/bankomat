package com.example.bankomat.service;

import com.example.bankomat.dto.BankomatDto;
import com.example.bankomat.dto.CardDto;
import com.example.bankomat.entity.*;
import com.example.bankomat.entity.enums.Rolename;
import com.example.bankomat.repository.*;
import com.example.bankomat.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ExchangeService implements UserDetailsService {
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

    //Kartani blokdan chiqarish
    public ApiResponse unBlockedCard(CardDto cardDto) {
        if (role(Rolename.ROLE_RESPONSIBLE)) {
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

    //Bankomatni kupyura bilan to'ldirish
    public ApiResponse fillingMoneyBankomat(BankomatDto bankomatDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRoles().contains(roleRepository.findByRolename(Rolename.ROLE_RESPONSIBLE))) {
            Optional<Bankomat> bankomat = bankomatRepository.findById(bankomatDto.getBankomat_id());
            if (bankomat.isPresent()) {
                if (bankomat.get().getUser().getUuid().equals(user.getUuid())) {
                    Bankomat bankomat1 = bankomat.get();
                    bankomat1.setU1000S(bankomat1.getU1000S() + bankomatDto.getU1000S());
                    bankomat1.setU5000S(bankomat1.getU5000S() + bankomatDto.getU5000S());
                    bankomat1.setU10000S(bankomat1.getU10000S() + bankomatDto.getU10000S());
                    bankomat1.setU50000S(bankomat1.getU50000S() + bankomatDto.getU50000S());
                    bankomat1.setU100000S(bankomat1.getU100000S() + bankomatDto.getU100000S());
                    Integer overallAmount = bankomatDto.getU1000S() + bankomatDto.getU5000S() + bankomatDto.getU10000S() + bankomatDto.getU50000S() + bankomatDto.getU100000S();
                    bankomat1.setMoney(bankomat1.getMoney() + overallAmount);
                    bankomatRepository.save(bankomat1);
                    CompletionMoneyBankomat completionMoneyBankomat = new CompletionMoneyBankomat(bankomat1, user, bankomatDto.getU1000S(), bankomatDto.getU5000S(), bankomatDto.getU10000S(), bankomatDto.getU50000S(), bankomatDto.getU100000S(), overallAmount);
                    completionMoneyBankomatRepository.save(completionMoneyBankomat);
                    return new ApiResponse("Bankomatni to'ldirish muvaffaqiyatli", true);
                } else {
                    return new ApiResponse("Siz bu bankomatga mas'ul emassiz", false);
                }
            } else {
                return new ApiResponse("Bunday bankomat topilmadi", false);
            }
        } else {
            return new ApiResponse("Sizda bunday huquq yo'q", true);
        }

    }

    //Umumiy kirim-chiqimlar
    public List<ExchangeHistory> exchangeHistories(Integer bankomat_id, boolean out) {
        if (role(Rolename.ROLE_DIRECTOR)) {
            Optional<Bankomat> bankomat = bankomatRepository.findById(bankomat_id);
            if (bankomat.isPresent()) {
                return exchangeHistoryRepository.findByBankomatAndOut(bankomat.get(), out);
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    //Kunlik kirim-chiqimlar
    public List<ExchangeHistory> exchangeHistoriesOne(Integer bankomat_id, boolean out, Timestamp day) {
        if (role(Rolename.ROLE_DIRECTOR)) {
            Optional<Bankomat> bankomat = bankomatRepository.findById(bankomat_id);
            if (bankomat.isPresent()) {
                return exchangeHistoryRepository.findByBankomatAndOutAndCreatedAt(bankomat.get(), out, day);
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    public boolean role(Rolename rolename) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRoles().contains(roleRepository.findByRolename(rolename))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String specialnumber) throws UsernameNotFoundException {
        Optional<Card> card = cardRepository.findBySpecialNumber(specialnumber);
        return card.orElseThrow(() -> new UsernameNotFoundException("Bunday specialnumber topilmadi"));
    }
}
