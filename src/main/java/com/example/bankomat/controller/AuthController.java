package com.example.bankomat.controller;

import com.example.bankomat.dto.BankomatDto;
import com.example.bankomat.dto.CardDto;
import com.example.bankomat.dto.ExchangeDto;
import com.example.bankomat.dto.LoginDto;
import com.example.bankomat.entity.ExchangeHistory;
import com.example.bankomat.jwt.JwtProwider;
import com.example.bankomat.response.ApiResponse;
import com.example.bankomat.service.AuthService;
import com.example.bankomat.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProwider jwtProwider;
    @Autowired
    AuthService authService;
    @Autowired
    ExchangeService exchangeService;


    //Login qilib kirish
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            String token = jwtProwider.generateToken(loginDto.getUsername());
            return ResponseEntity.status(201).body(token);
        } catch (Exception c) {
            return ResponseEntity.status(403).body(new ApiResponse("Login yoki parol xato", false));
        }
    }

    @PostMapping("/payinout")
    public ResponseEntity<?> pay(@RequestBody ExchangeDto bankomatDto) {
        ApiResponse payinoutbankomat = authService.payinoutbankomat(bankomatDto);
        return ResponseEntity.status(payinoutbankomat.isStatus() ? 201 : 409).body(payinoutbankomat);
    }

    @PostMapping("/unblockedcard")
    public ResponseEntity<?> unblock(@RequestBody CardDto cardDto) {
        ApiResponse apiResponse = exchangeService.unBlockedCard(cardDto);

        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
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
