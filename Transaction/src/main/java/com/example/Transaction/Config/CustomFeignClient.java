package com.example.Transaction.Config;

import com.example.Transaction.utils.Constants;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Base64;

@Configuration
public class CustomFeignClient {

        @Bean
        public RequestInterceptor basicAuthRequestInterceptor() {
            return requestTemplate -> {
                String auth = Constants.TXN_TO_USER_USERNAME + ":" + Constants.TXN_TO_USER_PASSWORD;
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
                requestTemplate.header("Authorization", "Basic " + encodedAuth);
            };
        }

}
