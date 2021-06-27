package com.example.bankomat.repository;

import com.example.bankomat.custom.BankomatHistoryCustom;

import com.example.bankomat.entity.Bankomat;
import com.example.bankomat.entity.ExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.sql.Timestamp;
import java.util.List;


@RepositoryRestResource(path = "bankomathistory",excerptProjection = BankomatHistoryCustom.class)
public interface ExchangeHistoryRepository extends JpaRepository<ExchangeHistory,Integer> {
    //Umumiy kirim-chiqimlar
    List<ExchangeHistory> findByBankomatAndOut(Bankomat bankomat, boolean out);

    //Kunlik kirim-chiqim
    List<ExchangeHistory> findByBankomatAndOutAndCreatedAt(Bankomat bankomat, boolean out, Timestamp createdAt);
}
