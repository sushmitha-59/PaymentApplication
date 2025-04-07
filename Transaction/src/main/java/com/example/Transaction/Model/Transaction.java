package com.example.Transaction.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@Entity @ToString @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String externalTransactionId;

    private String senderId;

    private Long amount;

    private String receiverId;

    @Enumerated(value=EnumType.STRING)
    private TransactionStatus transactionStatus;

    private String message;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

}
