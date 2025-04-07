package com.example.notification_service.Service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class notification_service {


    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.notification.from-username}")
    private String username;

    private String formSenderMessage(String externalTransactionId, String status, Long amount, String receiverEmail){
        if ("SUCCESSFUL".equals(status)) {
            return String.format(
                    "Hi! Your account has been debited with an amount of %d for the transaction,transaction ID: %s. Amount sent to: %s",
                    amount,
                    externalTransactionId,
                    receiverEmail
            );
        } else {
            return String.format(
                    "Hi! Your transaction to user:%s with Transaction ID: %s has failed.",
                    receiverEmail,
                    externalTransactionId
            );
        }
    }

    private String formReceiverMessage(String externalTransactionId, Long amount, String senderEmail){
        return String.format(
                "Hi! Your account has been credited with an amount of %d for the transaction,transaction ID: %s. , sent by the user: %s",
                amount,
                externalTransactionId,
                senderEmail
        );
    }

    @KafkaListener(topics = {"transaction_completed"},groupId = "notification_consumer")
    public void notify(String msg) throws ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(msg);

        String senderEmail=(String) jsonObject.get("senderEmail");
        String receiverEmail=(String) jsonObject.get("receiverEmail");
        String externalTransactionId=(String) jsonObject.get("externalTransactionId");
        String status=(String) jsonObject.get("status");
        Long amount=(Long) jsonObject.get("amount");

        //for sender,we should send mail for both debited successful and debited failure cases
        SimpleMailMessage simpleMailMessage1=new SimpleMailMessage();
        simpleMailMessage1.setFrom(username);
        simpleMailMessage1.setTo(senderEmail);
        simpleMailMessage1.setSubject("E-wallet Transaction Update");
        simpleMailMessage1.setText(formSenderMessage(externalTransactionId, status, amount,receiverEmail));
        try {
            javaMailSender.send(simpleMailMessage1);
            System.out.println("simpleMailMessage in debited case is : from:" + simpleMailMessage1.getFrom()+" ,to: "+ simpleMailMessage1.getTo()  +" and text :"+ simpleMailMessage1.getText());
        } catch (Exception e) {
            e.printStackTrace(); // log full exception
            System.out.println("Error while sending to sender: " + e.getMessage());
        }

        //for receiver ==>we should send mail for credited case
        if("SUCCESSFUL".equals(status)){
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setText( formReceiverMessage(externalTransactionId,amount,senderEmail));
            simpleMailMessage.setFrom(username);
            simpleMailMessage.setTo(receiverEmail);
            simpleMailMessage.setSubject("E-wallet Transaction Update");
            System.out.println("simpleMailMessage in credited case is : from:" + simpleMailMessage.getFrom()+" ,to: "+simpleMailMessage.getTo()  +" and text :"+simpleMailMessage.getText());
            try {
                javaMailSender.send(simpleMailMessage);
            } catch (Exception e) {
                e.printStackTrace(); // log full exception
                System.out.println("Error while sending to sender: " + e.getMessage());
            }
        }
    }
}
