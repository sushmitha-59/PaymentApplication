package com.example.wallet.Repository;

import com.example.wallet.Model.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet,Integer> {

    Wallet findByWalletId(String senderId);

    @Transactional
    @Modifying
    @Query("update Wallet w set w.balance=w.balance + ?2 where w.walletId=?1")
    void updateWallet(String walletId, long l);
}
