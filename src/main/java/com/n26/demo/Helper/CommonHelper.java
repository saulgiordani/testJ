package com.n26.demo.Helper;

import com.n26.demo.entity.TransactionDto;
import com.n26.demo.mock.MockData;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CommonHelper {

    public static Timestamp formatISO8601ToTimestamp(String toFormat) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().
                append(DateTimeFormatter.ISO_LOCAL_DATE_TIME).
                optionalStart().appendOffset("+HH", "Z").optionalEnd().
                toFormatter();

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(toFormat, formatter);
        return Timestamp.valueOf(LocalDateTime.ofInstant(offsetDateTime.toInstant(), ZoneOffset.UTC));
    }

    public static long getDateDiff(long time1, long time2, TimeUnit timeUnit) {
        long diffInMs = time1 - time2;
        return timeUnit.convert(diffInMs, TimeUnit.MILLISECONDS);
    }

    public static List<TransactionDto> initMockData() {
        List<TransactionDto> transactionDtos = new ArrayList<>();

        String timestampOk = ZonedDateTime.now(ZoneId.of("UTC")).toString().replace("[UTC]", "");
        String timestampNoOk = ZonedDateTime.now(ZoneId.of("UTC")).minusSeconds(70).toString().replace("[UTC]", "");

        transactionDtos.add(new TransactionDto("20.755", timestampOk)); //max
        transactionDtos.add(new TransactionDto("10.435", timestampOk));
        transactionDtos.add(new TransactionDto("10.455", timestampOk));
        transactionDtos.add(new TransactionDto("10.435", timestampOk));
        transactionDtos.add(new TransactionDto("10.455", timestampOk));
        transactionDtos.add(new TransactionDto("10.435", timestampOk));
        transactionDtos.add(new TransactionDto("10.455", timestampOk));
        transactionDtos.add(new TransactionDto("10.535", timestampOk));
        transactionDtos.add(new TransactionDto("10.455", timestampNoOk));
        transactionDtos.add(new TransactionDto("10.235", timestampOk)); //min

        return transactionDtos;
    }

    public static List<TransactionDto> filterTransactions(List<TransactionDto> transactionDtos) {
        List<TransactionDto> result =  new ArrayList<>();

        Instant instant = LocalDateTime.now(ZoneId.of("UTC")).toInstant(OffsetDateTime.now().getOffset());
        Timestamp now = Timestamp.from(instant);

        for (TransactionDto transactionDto : transactionDtos) {
            if (CommonHelper.getDateDiff(now.getTime(), transactionDto.getTimestamp().getTime(), TimeUnit.SECONDS) <= 60) {
                result.add(transactionDto);
            }
        }
        return result;
    }

    public static BigDecimal calculateSum(List<TransactionDto> transactions) {
        BigDecimal sum = transactions.stream().map(TransactionDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return setScale(sum);
    }

    public static BigDecimal calculateAvg(long size, BigDecimal sum) {
        return setScale(sum.divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_UP));
    }

    public static BigDecimal calculateMax(List<TransactionDto> transactions) {
        Optional<BigDecimal> max = transactions.stream()
                .map(t -> t.getAmount())
                .max(Comparator.naturalOrder());
        return setScale(max.get());
    }

    public static BigDecimal calculateMin(List<TransactionDto> transactions) {
        Optional<BigDecimal> min = transactions.stream()
                .map(t -> t.getAmount())
                .min(Comparator.naturalOrder());
        return setScale(min.get());
    }

    private static BigDecimal setScale(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
