package com.example.user.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserResponse {
    private Integer id;
    private String name;
    private String mobile;
    private String email;
    private Integer age;
    private Date createdOn;
    private Date updatedOn;
    private String message;
}

