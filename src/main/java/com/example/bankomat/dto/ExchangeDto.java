package com.example.bankomat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDto {
    private Integer bankomat_id;
    private boolean out;
    private Integer overallamount;
}
