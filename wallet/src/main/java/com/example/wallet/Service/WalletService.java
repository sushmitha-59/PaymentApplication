package com.example.wallet.Service;

import com.example.wallet.Model.Wallet;
import com.example.wallet.Repository.WalletRepository;
import com.example.wallet.utils.Constants;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.simple.parser.JSONParser;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    @Autowired
    KafkaTemplate<String , String > kafkaTemplate;

    @Autowired
    WalletRepository walletRepository;

    //this will subscribe to the  users_created topic and listen to it all the time , when that topic receives any event below func gets called
    @KafkaListener(topics = {Constants.KAFKA_USER_CREATION_TOPIC},groupId = Constants.KAFKA_WALLET_CONSUMER_GROUP_ID)
    public void create_wallet(String msg){
        try{
            JSONParser jsonParser=new JSONParser();
            JSONObject jsonObject=(JSONObject) jsonParser.parse(msg);
            String MobileNumber=(String )jsonObject.get("phone");
            Wallet wallet= Wallet.builder()
                    .walletId(MobileNumber)
                    .currency("INR")
                    .balance(Constants.INITIAL_AMOUNT)
                    .build();
            walletRepository.save(wallet);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public String update_wallet( String senderId, String receiverId, Long amount) throws Exception {
        try{
            Wallet sender=walletRepository.findByWalletId(senderId);
            Wallet receiver=walletRepository.findByWalletId(receiverId);
            if(sender ==null){
                throw new Exception("Invalid senderId");
            }
            if(receiver ==null){
                throw new Exception("Invalid receiverId");
            }
            if(sender.getBalance() < amount){
                throw new Exception("Insufficient Balance");
            }
            walletRepository.updateWallet(senderId,-amount);
            walletRepository.updateWallet(receiverId,+amount);
            return "Your transaction is successful!" ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
