package com.example.stark.QuoteNote;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ClienteFree implements Serializable{

    private static final long serialVersionUID = 501L;
    private String firebaseToken;
    public final static String AUTH_KEY_FCM = "AAAAbrKfQIw:APA91bERoKBYAq9CkG9mClGHrtp1GBHkhiGetdZ_MsquUZTRVyIkCOtX9KGxujCe5tGbAsZ3Xsj7j-sxf8v3mGWiZ2lbLeHY7ATv_pSl8uN9mAkviaBZwUGBOHJnDwa-hYa8MquhfRba";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    private int clientID;
    private String Name;
    private String LastName;
    private String Email;
    private String Password;
    private int pipChange = 4;
    private int maxSubs = 10;

    List<Quote> subscriptions = new ArrayList<Quote>(maxSubs);

    public ClienteFree(String name, String last, String email,String pass) {
        this.clientID = (int) Math.ceil(Math.random()*1000);
        this.firebaseToken = FirebaseInstanceId.getInstance().getToken();
        this.Name = name;
        this.LastName = last;
        this.Email = email;
        this.Password = pass;
    }

    public void addQuote(Quote q) {
        if(subscriptions.size() < maxSubs) {
            subscriptions.add(q);
        }
        else {
            System.out.println("Upgrade your account");
        }
    }

    public void removeQuote(Quote q) {
        try {
            for(Quote quote : subscriptions){
                if(q.getName().contains(quote.getName())){
                    subscriptions.remove(quote);
                    break;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Quote observable, Object arg){
        if(arg == null) {
            System.out.println("Null Argument");
        }else
        {
            for(Quote q : subscriptions) {
                if( (q).getName().equals( ((Quote) arg).getName() ) ) {
                    subscriptions.set(subscriptions.indexOf(q), (Quote) arg);
                }
            }

            if(evaluateChange(arg)){
                setOldValues(arg);
                try {
                    pushFCMNotification(this.firebaseToken,arg);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void pushFCMNotification(String DeviceIdKey, Object q) throws Exception {

        String authKey = AUTH_KEY_FCM; // You FCM AUTH key
        String FMCurl = API_URL_FCM;

        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + authKey);
        conn.setRequestProperty("Content-Type", "application/json");

        String notfnStr = "{\"body\": \"Quote has Updated\", \"title\": \""+((Quote)q).getName()+"\"}";
        String bodyStr = "{\"priority\": \"high\", \"to\": \""+ DeviceIdKey +"\", " +
                "\"notification\": " + notfnStr +"}";

        System.out.println("### input: " + bodyStr);


        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(bodyStr);
        //wr.write(data.toString());
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

    }

    public boolean evaluateChange(Object q) {
        Quote quote = (Quote) q;
        if( Math.abs(quote.getOldBidPoints() - quote.getBidPoints()) >= pipChange) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setPipChange(int pip) {
        this.pipChange = pip;
    }

    public int getPipChange() {
        return this.pipChange;
    }

    public String getName() {
        return this.Name;
    }

    public String getLastName() {
        return this.LastName;
    }

    public int getID() {
        return this.clientID;
    }

    public String getEmail() {
        return this.Email;
    }

    public String getPassword() {
        return this.Password;
    }


    public void setToken(String token) {
        this.firebaseToken = token;
    }

    public String getToken() {
        return this.firebaseToken;
    }

    public void setOldValues(Object arg) {
        if(arg == null) {
            System.out.println("Null Argument");
        }else {
            Quote quote = (Quote) arg;
            quote.setOldParameters((Quote) arg);

            for(Quote q : subscriptions) {
                if( q.getName().equals( quote.getName() ) ) {
                    subscriptions.set(subscriptions.indexOf(q), quote);
                }
            }
        }
    }
}