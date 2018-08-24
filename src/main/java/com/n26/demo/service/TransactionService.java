package com.n26.demo.service;

import com.n26.demo.entity.TransactionDto;

import java.util.List;

public interface TransactionService {

    List<TransactionDto> getTransactionsLast60Seconds();

    void addTransaction(TransactionDto transactionDto);

    void deleteTransations();
}
