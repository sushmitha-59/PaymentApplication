package com.example.user.model;

import com.example.user.dto.UserResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @Column(nullable = false,unique = true)
    private String email;
    private Integer age;

    @CreationTimestamp
    private Date CreatedOn;
    @UpdateTimestamp
    private Date UpdatedOn;

    @Column(nullable = false,unique = true)
    private String username; //this is nothing but MobileNumber
    private String password;
    private String authorities;

    //preparing User response
    public UserResponse to() {
        return UserResponse.builder()
                .mobile(this.username)
                .createdOn(this.CreatedOn)
                .updatedOn(this.UpdatedOn)
                .age(this.age)
                .email(this.email)
                .name(this.name)
                .id(this.id)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(this.authorities.split("::"))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
