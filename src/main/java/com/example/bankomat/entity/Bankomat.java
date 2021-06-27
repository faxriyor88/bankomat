package com.example.bankomat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Bankomat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private CardType cardType;

    private Integer moneySizeMax=1_000_000;
    private Integer moneySizeMin=1_000;
    private String bankName="NBU";
    private float withdrawMoneyCommision=1;
    private float paymoneyCommision= 0.5f;
    private Integer money;
    private String regionname;
    private String districtname;
    private String homenumber;
    private Integer U1000S;
    private Integer U5000S;
    private Integer U10000S;
    private Integer U50000S;
    private Integer U100000S;
    @ManyToOne
    private User user;
    @CreationTimestamp
    private Timestamp createdAt;
    @CreatedBy
    private UUID createdBy;
    @LastModifiedBy
    private UUID updatedBy;
}
