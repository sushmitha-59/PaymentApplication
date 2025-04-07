package com.example.user.service;

import com.example.user.model.User;
import com.example.user.repository.UserDao;
import com.example.user.repository.userCacheRepo;
import com.example.user.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    userCacheRepo userCacheRepo;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByUsername(username);
    }


    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthorities(Constants.USER_AUTHORITIES);
        User user2 = userDao.save(user);

        //send this event to the kafka
        //we have to prepare the event and send to the topic , event will be in json format which kafka understands
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.KAFKA_USER_EVENT_PHONE, user.getUsername());
        jsonObject.put(Constants.KAFKA_USER_EVENT_EMAIL, user.getEmail());
        //send to the kafka using kafka template object
        //in kafka template we mention that value should be string , so kafka expects value field as string format
        //serialize it to string using objectMapper object which will convert java object to json string
        try {
            kafkaTemplate.send(Constants.KAFKA_USER_CREATED_TOPIC, objectMapper.writeValueAsString(jsonObject));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        userCacheRepo.SetUser(user2);
        return user2;
    }

    public User getUserById(Integer id) {
        try{
            User user = userCacheRepo.getUser(id);
            if (user != null) {
                System.out.println("Getting User details from redis");
                return user;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        //else store it in the redis and send back the response
        Optional<User> user2 = Optional.empty();
        try {
            user2 = userDao.findById(id);
            if(user2.isPresent()) {
                User user1 = user2.get();
                userCacheRepo.SetUser(user1);
                return user1;
            }
            else{
                throw new RuntimeException("Unable to get User with given Id");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
