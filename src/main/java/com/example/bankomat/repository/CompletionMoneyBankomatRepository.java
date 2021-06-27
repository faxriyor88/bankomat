package com.example.bankomat.repository;

import com.example.bankomat.custom.CompletionMoneyBankomatCustom;
import com.example.bankomat.entity.Bankomat;
import com.example.bankomat.entity.CompletionMoneyBankomat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "completionMoneyBankomat",excerptProjection = CompletionMoneyBankomatCustom.class)
public interface CompletionMoneyBankomatRepository extends JpaRepository<CompletionMoneyBankomat,Integer> {

    List<CompletionMoneyBankomat> findByBankomat(Bankomat bankomat);
}
