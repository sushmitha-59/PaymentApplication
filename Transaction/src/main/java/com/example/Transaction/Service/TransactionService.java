package com.example.Transaction.Service;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Model.User;
import com.example.Transaction.Repository.TransactionRepository;
import com.example.Transaction.Repository.WalletInterface;
import com.example.Transaction.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    WalletInterface walletInterface;

    @Autowired
    CustomeUserDetailsService customeUserDetailsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public String transact(String senderId, String receiverID, Long amount, String message) throws JsonProcessingException {

        Transaction transaction = Transaction.builder()
                .senderId(senderId)
                .receiverId(receiverID)
                .amount(amount)
                .externalTransactionId(UUID.randomUUID().toString())
                .transactionStatus(TransactionStatus.PENDING)
                .message(message)
                .build();
        //save in the db
        transactionRepository.save(transaction);
        //call the wallet service to update the wallet through feign client method
        ResponseEntity<String> responseEntity = this.walletInterface.updateWallet(senderId, receiverID, amount);

        TransactionStatus transactionStatus = null;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            transactionStatus = TransactionStatus.SUCCESSFUL;
        } else {
            transactionStatus = TransactionStatus.FAILURE;
        }

        //now we will save this transaction in the transaction table
        transactionRepository.updateTransaction(transactionStatus, transaction.getExternalTransactionId());
        //for notification service push this event to the kafka
        //first get the email addresses of the both sender and receiver
        User senderUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //get the receiverUser using UserName,receiverEmail , we have fetch it from the userDB using the receiverID
        User receiverUser = (User) customeUserDetailsService.loadUserByUsername(receiverID);

        //prepare the jsonobject to push to kafka
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("senderEmail", senderUser.getEmail());
        jsonObject.put("receiverEmail", receiverUser.getEmail());
        jsonObject.put("amount", amount);
        jsonObject.put("externalTransactionId", transaction.getExternalTransactionId());
        jsonObject.put("status", transactionStatus);

        System.out.println("objectMapper.writeValueAsString(jsonObject) is " +objectMapper.writeValueAsString(jsonObject));
        //send to the kafka topic
        kafkaTemplate.send(Constants.KAFKA_TRANSACTION_COMPLETED, objectMapper.writeValueAsString(jsonObject));
        return transaction.getExternalTransactionId();
    }
}
