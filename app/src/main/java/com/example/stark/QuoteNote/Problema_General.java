package com.example.stark.QuoteNote;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.*;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Problema_General extends AppCompatActivity{

    int fav = 0;
    TextView Name, bidBig, bidPoints, offerBig, offerPoints, High, Low, Open;
    private Gson gson = new Gson();
    private Map<String,String> subsRequest = new HashMap<String,String>();

    //String ip = "192.168.100.10";
    //String ip = "10.12.47.30";
    String ip = "10.12.33.143";//CETYS

    int port = 7777;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Quote quote;
    ClienteFree cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema__general);
        //Recuperar Quote y Cliente
        quote = (Quote) getIntent().getSerializableExtra("Quote");
        cliente = (ClienteFree) getIntent().getSerializableExtra("Cliente");
        cliente = new ClienteFree("Flavio","Moreno","test@test.com","test");

        //Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Quote Details");
        //TextView espacio_texto = (TextView)findViewById(R.id.espacio_texto);
        //espacio_texto.setText(quote.returnParams());

        Name = (TextView) findViewById(R.id.NameD);
        bidBig = (TextView) findViewById(R.id.bidBigD);
        bidPoints = (TextView) findViewById(R.id.bidPointsD);
        offerBig = (TextView) findViewById(R.id.offerBigD);
        offerPoints = (TextView) findViewById(R.id.offerPointsD);
        High = (TextView) findViewById(R.id.HighD);
        Low = (TextView) findViewById(R.id.LowD);
        Open = (TextView) findViewById(R.id.OpenD);

        Name.setText(quote.getName());
        bidBig.setText("Bid Big: " + quote.getBidBig().toString());
        bidPoints.setText("Bid Points: " + String.valueOf(quote.getBidPoints()));
        offerBig.setText("Offer Big: " + quote.getOfferBig().toString());
        offerPoints.setText("Offer Points: " + String.valueOf(quote.getOfferPoints()));
        High.setText("Day High: " + quote.getHigh().toString());
        Low.setText("Day Low: " + quote.getLow().toString());
        Open.setText("Day Open: " + quote.getOpen().toString());

        System.out.println("Firebase Token: " + FirebaseInstanceId.getInstance().getToken());

        final FloatingActionButton favorite = (FloatingActionButton) findViewById(R.id.favorite);

        try{
            for(Quote q : cliente.subscriptions){
                if(cliente.subscriptions.size() == 0 || cliente.subscriptions == null){
                    break;
                }
                else if(q.getName() == quote.getName()){
                    fav = 1;
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fav == 0) {
                    //llamar Subscribe a este Quote
                    try{
                        subsRequest.put("request","subscribe");
                        subsRequest.put("quote",gson.toJson(quote));
                        subsRequest.put("client",gson.toJson(cliente));

                        new Thread (new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    new requestSenderGson(subsRequest).execute();
                                }catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite1));
                        fav = 1;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    //llamar Unsubscribe de este quote
                    try{
                        subsRequest.put("request","unsubscribe");
                        subsRequest.put("quote",gson.toJson(quote));
                        subsRequest.put("client",gson.toJson(cliente));

                        new Thread (new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    new requestSenderGson(subsRequest).execute();
                                }catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite0));
                        fav = 0;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("cliente", cliente);
        setResult(RESULT_OK, intent);
        finish();
    }

    public class requestSenderGson extends AsyncTask<Void,Void,Void> {

        String requestGson;

        public requestSenderGson(String message){
            this.requestGson = message;
        }

        public requestSenderGson(Map<String,String> map){
            this.requestGson = gson.toJson(map);
        }

        @Override
        protected Void doInBackground(Void...voids) {
            try
            {
                socket = new Socket(ip, port);

                //Send the message to the server
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(requestGson);
                oos.flush();
                System.out.println("Message sent to the server : "+ subsRequest.get("request"));

                //Get the return message from the server
                ois = new ObjectInputStream(socket.getInputStream());
                switch(subsRequest.get("request")){
                    case "subscribe":

                        cliente = gson.fromJson((String)(ois.readObject()), ClienteFree.class);
                        ois.close();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Subscribed to "+quote.getName()+"!", Toast.LENGTH_LONG).show();
                            }
                        });
                        System.out.println("Message received from the server : " + cliente.getName() );
                        break;
                    case "unsubscribe":

                        cliente = gson.fromJson((String)(ois.readObject()), ClienteFree.class);
                        ois.close();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Unsubscribed from "+quote.getName()+"!", Toast.LENGTH_LONG).show();
                            }
                        });
                        System.out.println("Message received from the server : " + cliente.getName());
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
