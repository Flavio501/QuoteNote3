package com.example.stark.ommbc;

import java.math.BigDecimal;
import java.util.Observable;

public class Quote extends Observable{

    private String Name;
    private long timestamp;
    private BigDecimal bidBig;
    private int bidPoints;
    private BigDecimal offerBig;
    private int offerPoints;
    private BigDecimal High;
    private BigDecimal Low;
    private BigDecimal Open;

    private BigDecimal oldbidBig;
    private int oldbidPoints;
    private BigDecimal oldofferBig;
    private int oldofferPoints;

    public Quote(
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

    public Quote(Wrapper w) {
        this.Name = w.Name;
        this.timestamp = w.timestamp;
        this.bidBig = w.bidBig;
        this.bidPoints = w.bidPoints;
        this.offerBig = w.offerBig;
        this.offerPoints = w.offerPoints;
        this.High = w.High;
        this.Low = w.Low;
        this.Open = w.Open;

        this.oldbidBig = w.bidBig;
        this.oldbidPoints = w.bidPoints;
        this.oldofferBig = w.offerBig;
        this.oldofferPoints = w.offerPoints;

    }

    public void setParameters(
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

        measurementsChanged();
    }

    public void setParameters(Wrapper w) {
        this.Name = w.Name;
        this.timestamp = w.timestamp;
        this.bidBig = w.bidBig;
        this.bidPoints = w.bidPoints;
        this.offerBig = w.offerBig;
        this.offerPoints = w.offerPoints;
        this.High = w.High;
        this.Low = w.Low;
        this.Open = w.Open;

        measurementsChanged();
    }

    public void setOldParameters(Observable q) {
        this.oldbidBig = ((Quote) q).bidBig;
        this.oldbidPoints = ((Quote) q).bidPoints;
        this.oldofferBig = ((Quote) q).offerBig;
        this.oldofferPoints = ((Quote) q).offerPoints;
    }

    public String getName() {
        return this.Name;
    }
    public long getTimestamp() {
        return this.timestamp;
    }
    public BigDecimal getBidBig() {
        return this.bidBig;
    }
    public int getBidPoints() {
        return this.bidPoints;
    }
    public BigDecimal getOfferBig() {
        return this.offerBig;
    }
    public int getOfferPoints() {
        return this.offerPoints;
    }
    public BigDecimal getHigh() {
        return this.High;
    }
    public BigDecimal getLow() {
        return this.Low;
    }
    public BigDecimal getOpen() {
        return this.Open;
    }
    public BigDecimal getOldBidBig() {
        return this.oldbidBig;
    }
    public int getOldBidPoints() {
        return this.oldbidPoints;
    }
    public BigDecimal getOldOfferBig() {
        return this.oldofferBig;
    }
    public int getOldOfferPoints() {
        return this.oldofferPoints;
    }


    public void printParams() {
        System.out.println(getName() + " "
                + getTimestamp() + " "
                + getBidBig().toString()
                + getBidPoints() + " "
                + getOfferBig().toString()
                + getOfferPoints() + " "
                + getHigh() + " "
                + getLow() + " "
                + getOpen());
    }

    public String returnParams(){
        return (this.getName() + " "
                + this.getTimestamp() + " "
                + this.getBidBig().toString()
                + this.getBidPoints() + " "
                + this.getOfferBig().toString()
                + this.getOfferPoints() + " "
                + this.getHigh() + " "
                + this.getLow() + " "
                + this.getOpen());
    }

    public void measurementsChanged() {
        setChanged();
        //notifyObservers(new Wrapper(Name,timestamp,bidBig,bidPoints,offerBig,offerPoints,High,Low,Open));
        notifyObservers(this);
    }

}