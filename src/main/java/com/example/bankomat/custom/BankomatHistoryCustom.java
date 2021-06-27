package com.example.bankomat.custom;

import com.example.bankomat.entity.ExchangeHistory;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = ExchangeHistory.class)
public interface BankomatHistoryCustom {
}
