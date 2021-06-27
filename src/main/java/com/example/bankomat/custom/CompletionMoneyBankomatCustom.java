package com.example.bankomat.custom;

import com.example.bankomat.entity.CompletionMoneyBankomat;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = CompletionMoneyBankomat.class)
public interface CompletionMoneyBankomatCustom {
}
