package com.example.bankomat.repository;

import com.example.bankomat.custom.CardCustom;
import com.example.bankomat.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.validation.constraints.Size;
import java.util.Optional;

@RepositoryRestResource(path = "card",excerptProjection = CardCustom.class)
public interface CardRepository extends JpaRepository<Card,Integer> {
    Optional<Card> findBySpecialNumber(@Size(min = 16, max = 16) String specialNumber);
    Optional<Card> findBySpecialNumberAndPincode(@Size(min = 16, max = 16) String specialNumber, @Size(min = 4, max = 4) String pincode);

}
