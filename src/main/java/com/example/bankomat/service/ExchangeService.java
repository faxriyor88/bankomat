package com.example.bankomat.service;

import com.example.bankomat.dto.BankomatDto;
import com.example.bankomat.dto.CardDto;
import com.example.bankomat.dto.ExchangeDto;
import com.example.bankomat.entity.*;
import com.example.bankomat.entity.enums.Rolename;
import com.example.bankomat.repository.*;
import com.example.bankomat.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
    @Autowired
    JavaMailSender javaMailSender;



    //Bankomatdan pul chiqarish yoki bankomatga pul solish
    public ApiResponse payinoutbankomat(ExchangeDto bankomatDto) {
        Card card = (Card) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (card.getRole().contains(roleRepository.findByRolename(Rolename.ROLE_USER))) {
            LocalDate localDate = LocalDate.now();
            //Kartaning muddatini tekshirish
            if (localDate.isAfter(card.getExpireDate())) {
                Optional<Bankomat> optional = bankomatRepository.findById(bankomatDto.getBankomat_id());
                if (optional.isPresent()) {
                    Bankomat bankomat = optional.get();
                    Bankomat bankomat1 = optional.get();
                    // Bankomat tegishli bank karta tegishli bank bilan bir xilligini tekshirish
                    if(bankomat.getBankOfBankomat().getBankname().equals(card.getBankOfCard().getBankname())){
                        //Kartani aktivligini tekshirish
                        if (card.isEnabled()) {
                            double overallAmountUser = bankomatDto.getOverallamount();
                            //Kartadan pul chiqarish yoki kartaga pul tashlashni tekshirish
                            if (bankomatDto.isOut()) {
                                //Mijoz so'ragan pul bankomat cheklovlaridan chiqib ketmaganligini tekshirish
                                if (overallAmountUser > bankomat.getMoneySizeMin() && overallAmountUser < bankomat.getMoneySizeMax()) {
                                    // Karta ichidagi pul naqd chiqarishga yetadimi yo'qmi  tekshirish
                                    if (overallAmountUser + overallAmountUser * (bankomat.getWithdrawMoneyCommision() / 100) < card.getCardInMoney()) {
                                        double overallAmountBank = 1_000 * bankomat.getU1000S() + 5_000 * bankomat.getU5000S() + 10_000 * bankomat.getU10000S() + 50_000 * bankomat.getU50000S() + 100_000 * bankomat.getU100000S();
                                        //Bankomat mijoz so'ragan pulni chiqarib beraolishini tekshirish
                                        if (overallAmountBank > overallAmountUser) {
                                            boolean exchange = exchange(bankomat, (int) overallAmountUser);
                                            if (exchange) {
                                                card.setCardInMoney(card.getCardInMoney() - (int) (overallAmountUser + overallAmountUser * (bankomat.getWithdrawMoneyCommision() / 100)));
                                                cardRepository.save(card);
                                                bankomat.setMoney(bankomat.getMoney() - (int) overallAmountUser);
                                                if (bankomat.getMoney()<20_000){
                                                    sendingEmail(bankomat.getUser().getEmail());
                                                }
                                                bankomatRepository.save(bankomat);
                                                ExchangeHistory bankomatHistory = new ExchangeHistory(card, bankomat, true,
                                                        Math.abs(bankomat1.getU1000S() - bankomat.getU1000S()),
                                                        Math.abs(bankomat1.getU5000S() - bankomat.getU5000S()),
                                                        Math.abs(bankomat1.getU10000S() - bankomat.getU10000S()),
                                                        Math.abs(bankomat1.getU50000S() - bankomat.getU50000S()),
                                                        Math.abs(bankomat1.getU100000S() - bankomat.getU100000S()), (int) overallAmountUser);
                                                exchangeHistoryRepository.save(bankomatHistory);
                                                return new ApiResponse("Muvaffaqiyatli", true);
                                            } else {
                                                return new ApiResponse("Bunday kupyura yo'q", false);
                                            }
                                        } else {
                                            return new ApiResponse("Bnakomatda buncha pul yo'q", false);
                                        }
                                    } else {
                                        return new ApiResponse("Karta ichida siz so'ragan pul yo'q", false);
                                    }
                                } else {
                                    return new ApiResponse("Mijoz so'ragan pul bankomat cheklovlaridan chiqib ketgan", false);
                                }
                            } else {
                                //Karta ichidagi pul kartaga pul kiritish komissiyasi uchun yetadimi yo'qmi tekshirish
                                if (overallAmountUser * (bankomat.getPaymoneyCommision() / 100) < card.getCardInMoney()) {
                                    String uss = String.valueOf(overallAmountUser);
                                    //Mijoz kiritayotgan summa bank kupyuralariga tushishini tekshirish
                                    if (uss.substring(uss.length() - 2).equals("000")) {
                                        if (overallAmountUser >= 100_000) {
                                            bankomat.setU100000S(bankomat.getU100000S() + Integer.parseInt(uss.substring(0, 1)));
                                            bankomat.setU50000S(bankomat.getU50000S() + Integer.parseInt(uss.substring(1, 2)));
                                            bankomat.setU1000S(bankomat.getU1000S() + Integer.parseInt(uss.substring(2, 3)));
                                        } else {
                                            if (overallAmountUser >= 50_000) {
                                                bankomat.setU50000S(bankomat.getU50000S() + Integer.parseInt(uss.substring(0, 1)));
                                                bankomat.setU1000S(bankomat.getU1000S() + Integer.parseInt(uss.substring(1, 2)));
                                            } else {
                                                if (overallAmountUser >= 10_000) {
                                                    bankomat.setU10000S(bankomat.getU10000S() + Integer.parseInt(uss.substring(0, 1)));
                                                    bankomat.setU1000S(bankomat.getU1000S() + Integer.parseInt(uss.substring(1, 2)));
                                                } else {
                                                    bankomat.setU1000S(bankomat.getU1000S() + Integer.parseInt(uss.substring(0, 1)));
                                                }
                                            }
                                        }
                                        card.setCardInMoney(card.getCardInMoney() + (int) (overallAmountUser + overallAmountUser * (bankomat.getPaymoneyCommision() / 100)));
                                        cardRepository.save(card);
                                        bankomat.setMoney(bankomat.getMoney() + (int) overallAmountUser);
                                        bankomatRepository.save(bankomat);
                                        ExchangeHistory bankomatHistory = new ExchangeHistory(card, bankomat, false,
                                                Math.abs(bankomat1.getU1000S() - bankomat.getU1000S()),
                                                Math.abs(bankomat1.getU5000S() - bankomat.getU5000S()),
                                                Math.abs(bankomat1.getU10000S() - bankomat.getU10000S()),
                                                Math.abs(bankomat1.getU50000S() - bankomat.getU50000S()),
                                                Math.abs(bankomat1.getU100000S() - bankomat.getU100000S()), (int) overallAmountUser);
                                        exchangeHistoryRepository.save(bankomatHistory);
                                        return new ApiResponse("Muvaffaqiyatli", true);
                                    } else {
                                        return new ApiResponse("Mijoz kiritayotgan summa bank kupyuralariga tushmadi", false);
                                    }
                                } else {
                                    return new ApiResponse("Karta ichidagi pul kartaga pul kiritish komissiyasi uchun yetmaydi", false);
                                }
                            }
                        } else {
                            return new ApiResponse("Karta bloklangan", false);
                        }}else {
                        return new ApiResponse("Karta tegishli bank bankomat tegishli bank bilan bir xil emas",false);
                    }
                } else {
                    return new ApiResponse("Bunday bankomat topilmadi", false);
                }
            } else {
                card.setEnabled(false);
                cardRepository.save(card);
                return new ApiResponse("Kartaning muddati tugagan", false);
            }
        } else {
            return new ApiResponse("Sizda bunday huquq yo'q", false);
        }
    }


    public boolean sendingEmail(String sendingEmail){
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("Eslatma");
            simpleMailMessage.setFrom("Bankomat");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setText("");
            javaMailSender.send(simpleMailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Integer B1B(Bankomat b1b, Integer u1u) {
        if (b1b.getU1000S() >= u1u) {
            b1b.setU1000S(b1b.getU1000S() - u1u);
            return u1u;
        }
        return b1b.getU1000S();
    }

    public Integer B5B(Bankomat b5b, Integer u5u) {
        if (u5u >= 5) {
            if (b5b.getU5000S() >= 1) {
                b5b.setU5000S(b5b.getU5000S() - 1);
                return B1B(b5b, u5u - 5);
            } else {
                return B1B(b5b, u5u);
            }
        } else {
            return B1B(b5b, u5u);
        }
    }

    public Integer B10B(Bankomat b10b, Integer u10u) {
        if (b10b.getU10000S() >= u10u) {
            b10b.setU10000S(b10b.getU10000S() - u10u);
            return u10u;
        } else {
            int a = b10b.getU10000S();
            b10b.setU10000S(0);
            return B5B(b10b, 2 * (u10u - a));
        }
    }

    public Integer B50B(Bankomat b50b, Integer u50u) {
        if (u50u >= 5) {
            if (b50b.getU50000S() >= 1) {
                b50b.setU50000S(b50b.getU50000S() - 1);
                return B10B(b50b, u50u - 5);
            } else {
                return B10B(b50b, u50u);
            }
        } else {
            return B10B(b50b, u50u);
        }

    }

    public Integer B100B(Bankomat b100b, Integer u100u) {
        if (b100b.getU100000S() >= u100u) {
            b100b.setU100000S(b100b.getU100000S() - u100u);
            return u100u;
        } else {
            int a = b100b.getU100000S();
            b100b.setU100000S(0);
            return B50B(b100b, 2 * (u100u - a));
        }
    }

    public boolean exchange(Bankomat bankomat, Integer overallamount) {
        String uss = String.valueOf(overallamount);
        if (uss.substring(uss.length() - 3).equals("000")) {
            if (overallamount >= 100_000) {
                B100B(bankomat, Integer.parseInt(uss.substring(0, 1)));
                B50B(bankomat, Integer.parseInt(uss.substring(1, 2)));
                B5B(bankomat, Integer.parseInt(uss.substring(2, 3)));
            } else {
                if (overallamount >= 50_000) {
                    B50B(bankomat, Integer.parseInt(uss.substring(0, 1)));
                    B5B(bankomat, Integer.parseInt(uss.substring(1, 2)));
                } else {
                    if (overallamount >= 10_000) {
                        B10B(bankomat, Integer.parseInt(uss.substring(0, 1)));
                        B5B(bankomat, Integer.parseInt(uss.substring(1, 2)));

                    } else {
                        if (overallamount >= 5_000) {
                            B5B(bankomat, Integer.parseInt(uss.substring(0, 1)));
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
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
