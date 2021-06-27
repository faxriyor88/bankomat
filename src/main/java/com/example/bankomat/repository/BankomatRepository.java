package com.example.bankomat.repository;

import com.example.bankomat.entity.Bankomat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BankomatRepository extends JpaRepository<Bankomat,Integer> {

}
