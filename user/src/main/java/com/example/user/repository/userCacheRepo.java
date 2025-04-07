package com.example.user.repository;

import com.example.user.model.User;
import com.example.user.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class userCacheRepo {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public String UserKeyFormat(Integer id) {
        return "User" + id;
    }

    //set the User profile info in the cache
    public void SetUser(User user) {
        this.redisTemplate.opsForValue().set(
                UserKeyFormat(user.getId()),//key will be User+id
                user,
                Constants.USER_CACHE_EXPIRY,
                TimeUnit.MINUTES
        );
    }

    //get the User profile info from the redis
    public User getUser(Integer id) {
        //get key first
        return (User) this.redisTemplate.opsForValue().get(
                UserKeyFormat(id)
        );

    }
}
