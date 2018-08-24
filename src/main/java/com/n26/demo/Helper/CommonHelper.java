package com.n26.demo.Helper;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
        long diffInMs = Math.abs(time1 - time2);
        return timeUnit.convert(diffInMs, TimeUnit.MILLISECONDS);
    }
}
