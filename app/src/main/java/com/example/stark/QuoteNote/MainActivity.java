package com.example.stark.QuoteNote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    EditText tEmail,tPass;
    Gson gson = new Gson();
    GsonBuilder gBuild = new GsonBuilder();
    ClienteFree cliente = new ClienteFree("Flavio","Moreno","test@test.com","test");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tEmail = (EditText) findViewById(R.id.text_input_edit_text);
        tPass = (EditText) findViewById(R.id.text_input_edit_text_password);
        btnLogin = (Button) findViewById(R.id.menu_button_login);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    Evaluar si el correo y contra existen
                */

                //if(tEmail.getText().toString().length() == 0 || tPass.getText().toString().length() == 0){
                  //  Toast.makeText(MainActivity.this,"Ingresa un Correo",Toast.LENGTH_LONG).show();
                //}else{

                    //Map<String,String> logRequest;

                    Intent MyIntent = new Intent(MainActivity.this,Main2Activity.class);
                    MyIntent.putExtra("Cliente",cliente);
                    startActivity(MyIntent);
                    finish(); //Block back button
                }

           // }
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



}

