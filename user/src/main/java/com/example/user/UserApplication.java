package com.example.user;

import com.example.user.model.User;
import com.example.user.repository.UserDao;
import com.example.user.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class UserApplication implements CommandLineRunner {
    @Autowired
    UserDao userDao;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Value("${interService.communication.user.username}")
    private String username;

    @Value("${interService.communication.user.password}")
    private String password;

    @Value("${interService.communication.user.email}")
    private String email;

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user = userDao.findByUsername(username);
        if (user == null) {
            userDao.save(
                    User.builder()
                            .age(20)
                            .username(this.username)
                            .password(passwordEncoder.encode(this.password))
                            .email(this.email)
                            .authorities(Constants.INTER_SERVICE_COMMUNICATION_USER_AUTHORITIES)
                            .build()
            );
        }

    }
}
