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
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class CompletionMoneyBankomat extends AbsEntityListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Bankomat bankomat;
    @ManyToOne
    private User user;
    private Integer U1000S=0;
    private Integer U5000S=0;
    private Integer U10000S=0;
    private Integer U50000S=0;
    private Integer U100000S=0;
    private Integer overallamount=0;
    @CreationTimestamp
    private Timestamp createdAt;


    public CompletionMoneyBankomat(Bankomat bankomat, User user, Integer u1000S, Integer u5000S, Integer u10000S, Integer u50000S, Integer u100000S, Integer overallamount) {
        this.bankomat = bankomat;
        this.user = user;
        U1000S = u1000S;
        U5000S = u5000S;
        U10000S = u10000S;
        U50000S = u50000S;
        U100000S = u100000S;
        this.overallamount = overallamount;
    }
}
