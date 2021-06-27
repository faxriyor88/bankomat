package com.example.bankomat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ExchangeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Card card;
    @ManyToOne
    private Bankomat bankomat;
    private boolean out=false;
    private Integer U1000S=0;
    private Integer U5000S=0;
    private Integer U10000S=0;
    private Integer U50000S=0;
    private Integer U100000S=0;
    private Integer overallamount=0;
    @CreationTimestamp
    private Timestamp createdAt;
    @CreatedBy
    private UUID createdBy;
    @LastModifiedBy
    private UUID updatedBy;

    public ExchangeHistory(Card card, Bankomat bankomat, boolean out, Integer u1000S, Integer u5000S, Integer u10000S, Integer u50000S, Integer u100000S, Integer overallamount) {
        this.card = card;
        this.bankomat = bankomat;
        this.out = out;
        U1000S = u1000S;
        U5000S = u5000S;
        U10000S = u10000S;
        U50000S = u50000S;
        U100000S = u100000S;
        this.overallamount = overallamount;

    }
}
