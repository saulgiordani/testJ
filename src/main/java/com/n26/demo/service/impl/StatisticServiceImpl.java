package com.n26.demo.service.impl;

import com.google.gson.Gson;
import com.n26.demo.Helper.CommonHelper;
import com.n26.demo.entity.TransactionDto;
import com.n26.demo.service.StatisticService;
import com.n26.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private TransactionService transactionService;

    @Override
    public String getStatistics() {
        List<TransactionDto> transactions = transactionService.getTransactionsLast60Seconds();
        Map<String, Object> statistics = new HashMap<>();
        BigDecimal sum = CommonHelper.calculateSum(transactions);
        statistics.put("sum", sum);
        statistics.put("avg", CommonHelper.calculateAvg(transactions.size(), sum));
        statistics.put("max", CommonHelper.calculateMax(transactions));
        statistics.put("min", CommonHelper.calculateMin(transactions));
        statistics.put("count", new Long(transactions.size()));
        return new Gson().toJson(statistics);
    }

    private BigDecimal setScale(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
