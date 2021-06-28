package com.example.bankomat.entity;

import com.example.bankomat.entity.template.AbsEntityListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class Bankomat extends AbsEntityListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private CardType cardType;

    private Integer moneySizeMax;
    private Integer moneySizeMin=1_000;
    @ManyToOne
    private Banks bankOfBankomat;
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

}
