package com.example.notification_service.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.notification.from-password}")
    private String password;
    @Value("${spring.notification.from-username}")
    private String username;
    @Bean
    JavaMailSenderImpl getJavaMailSenderImpl(){
        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        Properties properties=javaMailSender.getJavaMailProperties();
        properties.put("mail.debug", true);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);

        return javaMailSender;
    }

}
