package com.example.wallet.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User implements UserDetails{

    @Column(nullable = false,unique = true)
    private String username; //this is nothing but MobileNumber
    private String password;
    private String authorities;
    private String email;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Arrays.stream(this.authorities.split("::"))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}

