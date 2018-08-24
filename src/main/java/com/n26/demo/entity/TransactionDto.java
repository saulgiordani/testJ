package com.n26.demo.entity;

import com.n26.demo.Helper.CommonHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionDto {
    private BigDecimal amount;
    private Timestamp timestamp;

    public TransactionDto(String amount, String timestampString) {
        setAmount(new BigDecimal(amount));

        Timestamp timestamp = CommonHelper.formatISO8601ToTimestamp(timestampString);
        setTimestamp(timestamp);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
