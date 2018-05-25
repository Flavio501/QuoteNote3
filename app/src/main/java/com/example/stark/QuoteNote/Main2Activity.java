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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main2Activity extends AppCompatActivity {
    private ClienteFree cliente;
    private List<Quote> quoteList = new ArrayList<>();
    private RecyclerView recyclerview;
    private QuoteAdapter qAdapter;
    private Gson gson = new Gson();
    private Map<String,String> request = new HashMap<String,String>();
    //String ip = "192.168.100.10";
    String ip = "10.12.47.30";
    int port = 8888;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cliente = (ClienteFree) getIntent().getSerializableExtra("Cliente");
        System.out.println("Cliente "+cliente.getName()+" recibido de Main");
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
                        startActivityForResult(myIntent,1);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        try{
            request.put("request","quotes");
            request.put("body","");

            new Thread (new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try{
                            new requestSenderGson(request).execute();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                cliente = (ClienteFree) data.getSerializableExtra("cliente");
                System.out.println("List size: "+ cliente.subscriptions.size());
            }
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
                            new requestSenderGson(request).execute();
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
                            new requestSenderGson(request).execute();
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
            Intent intent = new Intent(Main2Activity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Thread quoteThread = new Thread (new Runnable() {
        @Override
        public void run() {
            while(true){
                try{
                    new requestSenderGson(request).execute();
                    Thread.sleep(3000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    });

    public class requestSenderGson extends AsyncTask<Void,Void,Void> {

        String requestGson;

        public requestSenderGson(String message){
            this.requestGson = message;
        }

        public requestSenderGson(Map<String,String> map){
            this.requestGson = gson.toJson(map);
        }
        //public requestSenderGson(Map<String, Map<String, String> > treemap){
        //    this.requestGson = gson.toJson(treemap);
        //}

        @Override
        protected Void doInBackground(Void...voids) {
            try
            {
                socket = new Socket(ip, port);

                //Send the message to the server
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(requestGson);
                oos.flush();
                System.out.println("Message sent to the server : "+request);

                //Get the return message from the server
                ois = new ObjectInputStream(socket.getInputStream());
                switch(request.get("request")){
                    case "quotes":
                        Type quoteListType = new TypeToken<List<Quote>>(){}.getType();
                        //Type quoteSubsMap = new TypeToken< Map<String, Map<String,String> > >(){}.getType();
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
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
