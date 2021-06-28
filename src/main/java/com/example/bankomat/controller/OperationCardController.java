package com.example.bankomat.controller;

import com.example.bankomat.dto.CardDto;
import com.example.bankomat.response.ApiResponse;
import com.example.bankomat.service.OperationCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperationCardController {
    @Autowired
    OperationCardService operationCardService;
    @PostMapping("/unblockedcard")
    public ResponseEntity<?> unblock(@RequestBody CardDto cardDto) {
        ApiResponse apiResponse = operationCardService.unBlockedCard(cardDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }
}
