package com.example.stark.QuoteNote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main2Activity extends AppCompatActivity {
    private ClienteFree cliente;
    private List<Quote> quoteList = new ArrayList<>();
    Quote tempQuote;
    private RecyclerView recyclerview;
    private QuoteAdapter qAdapter;
    private Gson gson = new Gson();
    private Map<String,String> request = new HashMap<String,String>();
    AlertDialog.Builder builder;
    int sleepTime = 5000;
    //String ip = "192.168.100.10";
    //String ip = "200.79.141.229";
    String ip = "10.12.47.30";
    int port = 12345;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cliente = (ClienteFree) getIntent().getSerializableExtra("Cliente");
        //System.out.println("Cliente "+cliente.getName()+" recibido de Main");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        builder = new AlertDialog.Builder(Main2Activity.this);


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
                            Thread.sleep(sleepTime);
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
        else if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                cliente = (ClienteFree) data.getSerializableExtra("cliente");
                System.out.println("Pip Margin: "+ cliente.getPipChange() +" Pips");
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
                            Thread.sleep(sleepTime);
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
                            Thread.sleep(sleepTime);
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
        if (id == R.id.action_filter) {
            //Toast.makeText(getApplicationContext(), "Búsqueda", Toast.LENGTH_SHORT).show();
            builder.setTitle("Enter Quote name: ");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    try{
                        for(Quote q : quoteList){
                            if(q.getName().contains(input.getText().toString().toUpperCase())){
                                tempQuote = q;
                                break;
                            }
                        }
                        Intent myIntent = new Intent(Main2Activity.this,Problema_General.class);
                        myIntent.putExtra("Quote",quoteList.get(quoteList.indexOf(tempQuote)));
                        myIntent.putExtra("Cliente",cliente);
                        startActivityForResult(myIntent,1);
                }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Main2Activity.this,"Please enter a valid Quote",Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.action_settings) {
            Intent setttingsIntent = new Intent(Main2Activity.this,Settings.class);
            setttingsIntent.putExtra("Cliente",cliente);
            startActivityForResult(setttingsIntent,2);
        }
        return super.onOptionsItemSelected(item);
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
                //socket = new Socket(ip, port);
                socket = new Socket("app2.quotenote.com",port);

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
