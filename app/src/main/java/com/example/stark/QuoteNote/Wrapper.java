package com.example.stark.QuoteNote;

import java.math.BigDecimal;

public class Wrapper {
    public String Name;
    public long timestamp;
    public BigDecimal bidBig;
    public int bidPoints;
    public BigDecimal offerBig;
    public int offerPoints;
    public BigDecimal High;
    public BigDecimal Low;
    public BigDecimal Open;

    public Wrapper(
            String Name,
            long timestamp,
            BigDecimal bidBig,
            int bidPoints,
            BigDecimal offerBig,
            int offerPoints,
            BigDecimal High,
            BigDecimal Low,
            BigDecimal Open) {
        this.Name = Name;
        this.timestamp = timestamp;
        this.bidBig = bidBig;
        this.bidPoints = bidPoints;
        this.offerBig = offerBig;
        this.offerPoints = offerPoints;
        this.High = High;
        this.Low = Low;
        this.Open = Open;
    }
}
