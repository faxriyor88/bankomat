package com.example.bankomat.controller;

import com.example.bankomat.dto.BankomatDto;
import com.example.bankomat.dto.ExchangeDto;
import com.example.bankomat.response.ApiResponse;
import com.example.bankomat.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
public class ExchangeController {
    @Autowired
    ExchangeService exchangeService;


    @PostMapping("/payinout")
    public ResponseEntity<?> pay(@RequestBody ExchangeDto bankomatDto) {
        ApiResponse payinoutbankomat = exchangeService.payinoutbankomat(bankomatDto);
        return ResponseEntity.status(payinoutbankomat.isStatus() ? 201 : 409).body(payinoutbankomat);
    }
    @PostMapping("/fillingbankomat")
    public ResponseEntity<?> fillingBankomat(@RequestBody BankomatDto bankomatDto) {
        ApiResponse apiResponse = exchangeService.fillingMoneyBankomat(bankomatDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/exchangehistory")
    public ResponseEntity<?> exchangehistories(@RequestParam Integer bankomat_id, boolean out) {
        return ResponseEntity.ok(exchangeService.exchangeHistories(bankomat_id, out));
    }

    @GetMapping("/exchangehistoryday")
    public ResponseEntity<?> exngehistoriesday(@RequestParam Integer bankomat_id, boolean out, Timestamp day) {
        return ResponseEntity.ok(exchangeService.exchangeHistoriesOne(bankomat_id, out, day));
    }
}
