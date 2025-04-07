package com.example.Transaction.Repository;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Integer> {

    @Modifying
    @Transactional
    @Query("update Transaction t set t.transactionStatus= ?1 where t.externalTransactionId= ?2")
    void updateTransaction(TransactionStatus transactionStatus, String externalTransactionId);
}
