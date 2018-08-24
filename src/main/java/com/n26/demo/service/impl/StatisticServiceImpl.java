package com.n26.demo.service.impl;

import com.google.gson.Gson;
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
        BigDecimal sum = calculateSum(transactions);
        statistics.put("sum", sum);
        statistics.put("avg", calculateAvg(transactions.size(), sum));
        statistics.put("max", calculateMax(transactions));
        statistics.put("min", calculateMin(transactions));
        statistics.put("count", new Long(transactions.size()));
        return new Gson().toJson(statistics);
    }

    private BigDecimal calculateSum(List<TransactionDto> transactions) {
        BigDecimal sum = transactions.stream().map(TransactionDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return setScale(sum);
    }

    private BigDecimal calculateAvg(long size, BigDecimal sum) {
        return setScale(sum.divide(BigDecimal.valueOf(size)));
    }

    private BigDecimal calculateMax(List<TransactionDto> transactions) {
        Optional<BigDecimal> max = transactions.stream()
                .map(t -> t.getAmount())
                .max(Comparator.naturalOrder());
        return setScale(max.get());
    }

    private BigDecimal calculateMin(List<TransactionDto> transactions) {
        Optional<BigDecimal> min = transactions.stream()
                .map(t -> t.getAmount())
                .min(Comparator.naturalOrder());
        return setScale(min.get());
    }

    private BigDecimal setScale(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
