package com.example.Transaction.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class CreateTransactionRequest {

    @NotBlank(message="receiverId should not be blank")
    private String receiverId;

    @NotNull(message="amount should not be blank")
    @Min(1)
    private Long amount;

    //we don't need senderid , from the authentication itself we can get the user details

    private String message ;

}
