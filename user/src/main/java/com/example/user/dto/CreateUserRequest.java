package com.example.user.dto;

import com.example.user.model.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateUserRequest {
    @NotBlank(message = "Name should not be blank.")
    private String name;
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$",
            message = "Invalid email format. Please enter a valid Gmail address ending with @gmail.com"
    )
    @NotBlank(message = "Email should not be blank.")
    private String email;
    @Min(value = 18, message = "Age should be more than 10")
    private Integer age;
    @NotBlank(message = "mobileNumber should not be blank.")
    private String mobileNumber;
    @NotBlank(message = "password should not be blank.")
    private String password;

    public User to() {
        return User.builder()
                .age(this.age)
                .username(this.mobileNumber)
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .build();
    }
}

