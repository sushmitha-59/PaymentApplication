package com.example.user.controller;

import com.example.user.dto.CreateUserRequest;
import com.example.user.dto.UserResponse;
import com.example.user.model.User;
import com.example.user.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/create")
    public ResponseEntity<?> create_user(@RequestBody @Valid CreateUserRequest createUserRequest) {
        try {
            UserResponse user = customUserDetailsService.saveUser(createUserRequest.to()).to();
            user.setMessage("User got created successfully");
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(user);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataIntegrityViolationException) {
                throw (DataIntegrityViolationException) cause;
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/profile_info")
    public ResponseEntity<?> profile_info() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //get the User details from the db again. After the authorization, User details might have been updated , so to get the
        //fresh data get the User from db again
        try {
            user = customUserDetailsService.getUserById(user.getId());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(user.to());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(
                    "Unable to get the User profile info" + e.getMessage()
            );
        }
    }

    //this will return json type
    //to be used by other services for the authentication purpose
    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDetails returnUser(@PathVariable("username")
                                  @NotBlank(message = "pathVariable username should not be blank.") String username) {
        try {
            return customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
