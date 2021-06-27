package com.example.bankomat.custom;

import com.example.bankomat.entity.Card;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Card.class)
public interface CardCustom {

}
