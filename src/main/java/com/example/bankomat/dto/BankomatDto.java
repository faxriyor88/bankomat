package com.example.bankomat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankomatDto {
    private Integer bankomat_id;
    private Integer U1000S;
    private Integer U5000S;
    private Integer U10000S;
    private Integer U50000S;
    private Integer U100000S;
}
