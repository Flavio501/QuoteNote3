package com.example.stark.QuoteNote;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class Main2Activity extends AppCompatActivity {
    private ClienteFree cliente;
    private List<Quote> quoteList = new ArrayList<>();
    private RecyclerView recyclerview;
    private QuoteAdapter qAdapter;
    private BigDecimal bd;
    private API api = new API();
    private Gson gson = new Gson();

    //MessageSender messager = new MessageSender();
    String ip = "192.168.100.10";
    int port = 7777;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        qAdapter = new QuoteAdapter(quoteList);
        RecyclerView.LayoutManager qLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(qLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(qAdapter);
        recyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerview ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent myIntent = new Intent(Main2Activity.this,Problema_General.class);
                        myIntent.putExtra("Quote",quoteList.get(position));
                        myIntent.putExtra("Cliente",cliente);
                        startActivity(myIntent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        try{
            new Thread (new Runnable() {
                @Override
                public void run() {
                    //new quoteReceiverGson().execute();
                    //Toast.makeText(getApplicationContext(), "Object received", Toast.LENGTH_LONG).show();

                    new requestSenderGson("Quotes").execute();
                }
            }).start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
            new Thread (new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try{
                            new requestSenderGson("Quotes").execute();
                            Thread.sleep(3000);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            new Thread (new Runnable() {
                @Override
                public void run() {
                    while(true){
                            try{
                                new requestSenderGson("Quotes").execute();
                                Thread.sleep(3000);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                    }

                }
            }).start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter)
            Toast.makeText(getApplicationContext(), "Búsqueda", Toast.LENGTH_SHORT).show();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class quoteReceiverGson extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void...voids) {
            try {
                Socket s = new Socket(ip, port);
                ConnectionHandler con = new ConnectionHandler(s);
                quoteList = con.receiveQuoteGson();
                //qAdapter.notifyDataSetChanged();
                runOnUiThread(new Runnable() {
                    public void run() {
                        qAdapter.updateList(quoteList);
                        Toast.makeText(getApplicationContext(), "Quotes updated", Toast.LENGTH_LONG).show();
                    }
                });
                //con.stopInput();

            } catch (Exception e) {
                System.out.println(e);
            }
            return null;
        }
    }

    public class requestSenderGson extends AsyncTask<Void,Void,Void> {

        String request;

        public requestSenderGson(String message){
            this.request = message;
        }

        @Override
        protected Void doInBackground(Void...voids) {
            try
            {
                socket = new Socket(ip, port);

                //Send the message to the server
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(request);
                oos.flush();
                System.out.println("Message sent to the server : "+request);

                //Get the return message from the server
                ois = new ObjectInputStream(socket.getInputStream());
                switch(request){
                    case "Quotes":
                        Type quoteListType = new TypeToken<List<Quote>>(){}.getType();
                        quoteList = gson.fromJson((String)(ois.readObject()), quoteListType);
                        ois.close();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                qAdapter.updateList(quoteList);
                                //Toast.makeText(getApplicationContext(), "Quotes updated", Toast.LENGTH_LONG).show();
                            }
                        });
                        System.out.println("Message received from the server : " + quoteList.getClass() );
                        break;
                    case"login":
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class ConnectionHandler {
        private Socket clientSocket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public ConnectionHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            String clientAddress = clientSocket.getInetAddress().toString()
                    .substring(1);
            System.out.println("Connected to " + clientAddress);
            try {
                //oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public ConnectionHandler(Socket clientSocket, ObjectInputStream oi, ObjectOutputStream oo) {
            this.clientSocket = clientSocket;
            this.ois = oi;
            this.oos = oo;
            String clientAddress = clientSocket.getInetAddress().toString()
                    .substring(1);
            System.out.println("Connected to " + clientAddress);
        }

        public void stopInput() {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void stopConnection() {
            try {
                clientSocket.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void sendObject(Object o) {
            try {
                //oos = (ObjectOutputStream) clientSocket.getOutputStream();
                oos.writeObject(o);
                oos.flush();
                oos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public ClienteFree receiveClient() throws Exception{
            try {
                //ois = (ObjectInputStream) clientSocket.getInputStream();
                ClienteFree cliente = (ClienteFree)(ois.readObject());
                //ois.close();
                return cliente;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public List<Quote> receiveQuoteGson() throws Exception{
            try {
                //ois = new ObjectInputStream(socket.getInputStream());
                Type quoteListType = new TypeToken<List<Quote>>(){}.getType();
                List<Quote> newQuotes = gson.fromJson((String)(ois.readObject()), quoteListType);
                ois.close();
                return newQuotes;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void stopOutput() {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
