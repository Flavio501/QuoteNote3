package com.example.stark.QuoteNote;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Quote implements Serializable{

    public static final long serialVersionUID = 100L;
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

    public boolean changed;

    private List<ClienteFree> clientes = new ArrayList<ClienteFree>();

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

    public void addObserver(ClienteFree o) {
        this.clientes.add(o);
    }

    public void deleteObserver(ClienteFree o) {
        for(ClienteFree c : clientes) {
            if( o.getEmail().contains( c.getEmail() ) ){
                clientes.remove(c);
                break;
            }
        }
    }

    public void notifyObservers(Object o) {
        for(ClienteFree c : clientes) {
            c.update(this, o);
        }
        this.changed = false;
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

    public void setOldParameters(Quote q) {
        this.oldbidBig = q.bidBig;
        this.oldbidPoints = q.bidPoints;
        this.oldofferBig = q.offerBig;
        this.oldofferPoints = q.offerPoints;
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

    public void measurementsChanged() {
        setChanged();
        notifyObservers(this);
    }

    public void setChanged() {
        this.changed = true;
    }

}