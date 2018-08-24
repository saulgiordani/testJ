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
        List<TransactionDto> transactionDtos = initMockData();

        assertTrue("sum incorrect", new BigDecimal("104.55").equals(calculateSum(transactionDtos)));
        assertTrue("avg incorrect", new BigDecimal("10.46").equals(
                calculateAvg(new Long(transactionDtos.size()), calculateSum(transactionDtos))));
        assertTrue("max incorrect", new BigDecimal("10.76").equals(calculateMax(transactionDtos)));
        assertTrue("min incorrect", new BigDecimal("10.24").equals(calculateMin(transactionDtos)));
    }

    @Test
    public void testGetTransactionsLast60Seconds() {
        List<TransactionDto> result =  new ArrayList<>();

        Instant instant = LocalDateTime.now(ZoneId.of("UTC")).toInstant(OffsetDateTime.now().getOffset());
        Timestamp now = Timestamp.from(instant);

        for (TransactionDto transactionDto : initMockData()) {
            if (CommonHelper.getDateDiff(now.getTime(), transactionDto.getTimestamp().getTime(), TimeUnit.SECONDS) <= 60) {
                result.add(transactionDto);
            }
        }
        assertTrue("size should be 9, but was: " + result.size(), result.size() == 9);
    }

    private BigDecimal calculateSum(List<TransactionDto> transactions) {
        BigDecimal sum = transactions.stream().map(TransactionDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return setScale(sum);
    }

    private BigDecimal calculateAvg(long size, BigDecimal sum) {
        return setScale(sum.divide(BigDecimal.valueOf(new Long(size))));
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

    private List<TransactionDto> initMockData() {
        List<TransactionDto> transactionDtos = new ArrayList<>();

        String timestampOk = ZonedDateTime.now(ZoneId.of("UTC")).toString().replace("[UTC]", "");
        String timestampNoOk = ZonedDateTime.now(ZoneId.of("UTC")).minusSeconds(70).toString().replace("[UTC]", "");

        transactionDtos.add(new TransactionDto("10.755", timestampOk)); //max
        transactionDtos.add(new TransactionDto("10.435", timestampOk));
        transactionDtos.add(new TransactionDto("10.455", timestampOk));
        transactionDtos.add(new TransactionDto("10.435", timestampOk));
        transactionDtos.add(new TransactionDto("10.455", timestampOk));
        transactionDtos.add(new TransactionDto("10.435", timestampOk));
        transactionDtos.add(new TransactionDto("10.455", timestampOk));
        transactionDtos.add(new TransactionDto("10.435", timestampOk));
        transactionDtos.add(new TransactionDto("10.455", timestampOk));
        transactionDtos.add(new TransactionDto("10.235", timestampNoOk)); //min

        return transactionDtos;
    }
}
