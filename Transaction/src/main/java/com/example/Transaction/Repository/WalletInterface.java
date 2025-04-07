package com.example.Transaction.Repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("WALLET")
public interface WalletInterface {

    @PostMapping("/wallet/update")
    ResponseEntity<String> updateWallet(
            @RequestParam String senderId,
            @RequestParam String receiverId,
            @RequestParam Long amount
    ) ;
}
