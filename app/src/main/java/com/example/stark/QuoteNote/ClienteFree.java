package com.example.stark.QuoteNote;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ClienteFree implements Observer, Serializable{


    private static final long serialVersionUID = 501L;
    private int clientID;
    private String Name;
    private String LastName;
    private String Email;
    private String Password;
    private int pipChange = 4;
    private int maxSubs = 10;

    List<Observable> subscriptions = new ArrayList<Observable>(maxSubs);

    public ClienteFree(Observable quote) {
        this.clientID = (int) Math.ceil(Math.random()*1000);
        quote.addObserver(this);
    }

    public ClienteFree(String name, String last, String email,String pass) {
        this.clientID = (int) Math.ceil(Math.random()*1000);
        this.Name = name;
        this.LastName = last;
        this.Email = email;
        this.Password = pass;
    }

    public ClienteFree() {
        this.clientID = (int) Math.ceil(Math.random()*1000);
    }

    public void addQuote(Observable q) {
        if(subscriptions.size() < maxSubs) {
            subscriptions.add(q);
        }
        else {
            System.out.println("Upgrade your account");
        }
    }

    public void update(Observable observable, Object arg) {
        if(arg == null) {
            System.out.println("Null Argument");
        }else
        {
            for(Observable q : subscriptions) {
                if( ((Quote) q).getName().equals( ((Quote) arg).getName() ) ) {
                    subscriptions.set(subscriptions.indexOf(q), (Quote) arg);
                }
            }

            if(evaluateChange(arg)){
                setOldValues(arg);
                printParams(arg);
            }
        }
    }

    public boolean evaluateChange(Object q) {
        Quote quote = (Quote) q;
        if( Math.abs(quote.getOldBidPoints() - quote.getBidPoints()) >= pipChange) {
            return true;
        }
        else {
            //if((this.oldbidBig.doubleValue() != this.bidBig.doubleValue())) {
            //	return true;
            //}
            return false;
        }
    }

    public void setPipChange(int pip) {
        this.pipChange = pip;
    }

    public void setOldValues(Object arg) {
        if(arg == null) {
            System.out.println("Null Argument");
        }else {
            Quote quote = (Quote) arg;
            quote.setOldParameters((Quote) arg);

            for(Observable q : subscriptions) {
                if( ((Quote) q).getName().equals( quote.getName() ) ) {
                    subscriptions.set(subscriptions.indexOf(q), quote);
                }
            }
        }
    }

    public void printParams(Object arg) {
        Quote q = ((Quote) arg);
        System.out.println(
                "ID de Cliente: "
                        + this.clientID + " "
                        + q.getName() + " "
                        + q.getTimestamp() + " "
                        + q.getBidBig().toString()
                        + q.getBidPoints() + " "
                        + q.getOfferBig().toString()
                        + q.getOfferPoints() + " "
                        + q.getHigh() + " "
                        + q.getLow() + " "
                        + q.getOpen());
    }
}