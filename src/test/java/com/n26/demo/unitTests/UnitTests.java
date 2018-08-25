package com.n26.demo.unitTests;

import com.n26.demo.Helper.CommonHelper;
import com.n26.demo.entity.TransactionDto;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UnitTests {

    @Test
    public void testTrsanctionDtoInitialization() throws ParseException {
        TransactionDto transactionDto = new TransactionDto("10.455", "2018-07-17T09:59:51.312Z");
        assertTrue("not bigdecimal", transactionDto.getAmount().getClass().getTypeName().equals("java.math.BigDecimal"));
        assertTrue("not timestamp", transactionDto.getTimestamp().getClass().getTypeName().equals("java.sql.Timestamp"));
    }

    @Test
    public void testCalcutationsStatistics() {
        List<TransactionDto> transactionDtos = CommonHelper.initMockData();
        List<TransactionDto> filterTransactions = CommonHelper.filterTransactions(transactionDtos);
        BigDecimal sum = CommonHelper.calculateSum(filterTransactions);

        assertTrue("sum incorrect", new BigDecimal("104.20").equals(CommonHelper.calculateSum(filterTransactions)));
        assertTrue("avg incorrect", new BigDecimal("11.58").equals(
                CommonHelper.calculateAvg(new Long(filterTransactions.size()), sum)));
        assertTrue("max incorrect", new BigDecimal("20.76").equals(CommonHelper.calculateMax(filterTransactions)));
        assertTrue("min incorrect", new BigDecimal("10.24").equals(CommonHelper.calculateMin(filterTransactions)));
    }

    @Test
    public void testGetTransactionsLast60Seconds() {
        List<TransactionDto> result =  new ArrayList<>();

        Instant instant = LocalDateTime.now(ZoneId.of("UTC")).toInstant(OffsetDateTime.now().getOffset());
        Timestamp now = Timestamp.from(instant);

        for (TransactionDto transactionDto : CommonHelper.initMockData()) {
            if (CommonHelper.getDateDiff(now.getTime(), transactionDto.getTimestamp().getTime(), TimeUnit.SECONDS) <= 60) {
                result.add(transactionDto);
            }
        }
        assertTrue("size should be 9, but was: " + result.size(), result.size() == 9);
    }
}
