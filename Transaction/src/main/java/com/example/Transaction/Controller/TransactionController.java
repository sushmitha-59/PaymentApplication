package com.example.Transaction.Controller;

import com.example.Transaction.Model.User;
import com.example.Transaction.Service.TransactionService;
import com.example.Transaction.dto.CreateTransactionRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/initiate")
    public ResponseEntity<String> initiateTransaction(@RequestBody @Valid CreateTransactionRequest createTransactionRequest) {
        //from request we are only getting the receiver id not sender id , we have to get the senderId from the authenticated user
        //from login we are getting the user.
        try {
            System.out.println("user is " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            System.out.println("user class  is " + (SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClass().getName());
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println("user is " + user.toString());
            String msg = transactionService.transact(
                    user.getUsername(),
                    createTransactionRequest.getReceiverId(),
                    createTransactionRequest.getAmount(),
                    createTransactionRequest.getMessage()
            );
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(msg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getCause().getMessage());
        }
    }
}
