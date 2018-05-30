package com.example.stark.QuoteNote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    EditText tEmail,tPass;
    Gson gson = new Gson();
    GsonBuilder gBuild = new GsonBuilder();
    ClienteFree cliente;

    //String ip = "10.12.47.30";
    int port = 12345;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    private Map<String,String> request = new HashMap<String,String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isTaskRoot()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        tEmail = (EditText) findViewById(R.id.text_input_edit_text);
        tPass = (EditText) findViewById(R.id.text_input_edit_text_password);
        btnLogin = (Button) findViewById(R.id.menu_button_login);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if (tEmail.getText().toString().length() == 0 || tPass.getText().toString().length() == 0) {
                        Toast.makeText(MainActivity.this, "Fill in username and password", Toast.LENGTH_SHORT).show();

                    } else {
                        Map<String, String> myMap = new HashMap<String, String>();
                        myMap.put("request", "login");
                        myMap.put("body", tEmail.getText().toString() + "," + tPass.getText().toString());

                        new requestSenderGson(myMap).execute();
                        Thread.sleep(1000);

                        if(cliente == null){
                            Toast.makeText(MainActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                        }else{
                            try{
                                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                                intent.putExtra("Cliente",cliente);
                                startActivity(intent);
                                finish();
                            }catch(Exception e){
                                Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }catch(Exception e){
                    Toast.makeText(MainActivity.this,"Try again later",Toast.LENGTH_LONG).show();
                }

            }
        });

        btnRegister = (Button) findViewById(R.id.menu_button_register);

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MyIntent = new Intent(MainActivity.this, RegisterController.class);
                startActivity(MyIntent);
            }
        });
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
                System.out.println("Message sent to the server : "+requestGson);

                //Get the return message from the server
                ois = new ObjectInputStream(socket.getInputStream());
                cliente = gson.fromJson((String)(ois.readObject()), ClienteFree.class);
                ois.close();
                Thread.sleep(1000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Toast.makeText(getApplicationContext(),"Welcome "+cliente.getName()+" !",Toast.LENGTH_LONG).show();
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(),"An error has occured...",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

