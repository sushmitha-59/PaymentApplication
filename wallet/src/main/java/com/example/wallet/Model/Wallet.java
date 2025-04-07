package com.example.wallet;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(unique = true,nullable = false)
    private String WalletId;
    private Long balance;
    


}
