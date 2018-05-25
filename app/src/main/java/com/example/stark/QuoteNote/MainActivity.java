package com.example.stark.QuoteNote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
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
    String ip = "10.12.33.143";//CETYS
    int port = 7777;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    ClienteFree cliente;
    private Gson gson = new Gson();

    Button btnLogin, btnRegister;//menu_button_register;

    EditText etPassword, etMail;

    String password, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPassword =(EditText) findViewById(R.id.text_input_edit_text_password);
        etMail =(EditText) findViewById(R.id.text_input_edit_text);
        btnLogin = (Button) findViewById(R.id.menu_button_login);


        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    if (etMail.getText().toString().length() == 0 || etPassword.getText().toString().length() == 0) {
                        Toast.makeText(MainActivity.this, "Fill in username and password", Toast.LENGTH_SHORT).show();

                    } else {
                        Map<String, String> myMap = new HashMap<String, String>();
                        myMap.put("request", "login");
                        myMap.put("body", etMail.getText().toString() + "," + etPassword.getText().toString());
                        //myMap.put("body", etMail.getText().toString() + "," + etPassword.getText().toString());

                        new clientSenderGson(gson.toJson(myMap)).execute();

                        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                        intent.putExtra("cliente",cliente);
                        startActivity(intent);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
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



    public class clientSenderGson extends AsyncTask<Void,Void,Void> {

        String request;

        public clientSenderGson(String message){
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
                cliente = gson.fromJson((String)(ois.readObject()), ClienteFree.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Bienvenido! "+cliente.getName(),Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}

