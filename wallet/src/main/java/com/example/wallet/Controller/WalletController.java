package com.example.wallet.Controller;

import com.example.wallet.Service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    WalletService walletService;

    @PostMapping("/update")
    public ResponseEntity<String> updateWallet(@RequestParam String senderId, @RequestParam String receiverId, @RequestParam Long amount) {
        try {
            String msg = walletService.update_wallet(senderId, receiverId, amount);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(msg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            e.getCause() != null ? e.getCause().getMessage() : e.getMessage()
                    );
        }
    }
}
