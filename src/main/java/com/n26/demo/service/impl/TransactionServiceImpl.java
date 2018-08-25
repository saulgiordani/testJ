package com.n26.demo.service.impl;

import com.n26.demo.Helper.CommonHelper;
import com.n26.demo.entity.TransactionDto;
import com.n26.demo.mock.MockData;
import com.n26.demo.service.TransactionService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public List<TransactionDto> getTransactionsLast60Seconds() {
        return CommonHelper.filterTransactions(MockData.transactions);
    }

    @Override
    public void addTransaction(TransactionDto transactionDto) {
        MockData.transactions.add(transactionDto);
    }

    @Override
    public void deleteTransations() {
        MockData.transactions.clear();
    }
}
