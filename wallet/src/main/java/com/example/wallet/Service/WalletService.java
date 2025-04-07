package com.example.wallet.Service;

import com.example.wallet.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService  {
    @Autowired
    KafkaTemplate<String , String > kafkaTemplate;

    @KafkaListener(topics = {Constants.KAFKA_USER_CREATION_TOPIC},groupId = Constants.KAFKA_WALLET_CONSUMER_GROUP_ID)
    public void create_wallet(String msg){
        try{

        }catch(){

        }
    }

}
