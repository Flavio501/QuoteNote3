package com.example.stark.QuoteNote;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RegisterController extends AppCompatActivity {

    String ip = "10.12.33.143";//CETYS
    int port = 7777;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    ClienteFree cliente;
    private Gson gson = new Gson();
    Button validate;
    EditText nombre, apellido, email, password, pwc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombre = (EditText) findViewById(R.id.text_input_edit_name);
        apellido = (EditText) findViewById(R.id.text_input_edit_last_name);
        email = (EditText) findViewById(R.id.text_input_edit_email);
        password = (EditText) findViewById(R.id.text_input_edit_password);
        validate = (Button) findViewById(R.id.menu_button_register);
        pwc = (EditText) findViewById(R.id.text_input_edit_confirm_password);

    }

    protected boolean validatePassword(String password) {

        if (!password.equals(pwc.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    //validation of email
    protected boolean validateEmail(String email) {
        return !(email == null || TextUtils.isEmpty(email)) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void validate(View view) {
        // Do something in response to button
        if (!validateEmail(email.getText().toString())) {
            email.setError("Porfavor Escribe un Email Vàlido");
            email.requestFocus();

        } else if (validatePassword(password.getText().toString())) {
            password.setError("Las Constraseñas No Coinciden");
            password.requestFocus();
        } else {
            try {
                cliente = new ClienteFree(nombre.getText().toString(),apellido.getText().toString(),
                        email.getText().toString(),password.getText().toString());
                Map<String, String> myMap = new HashMap<String, String>();

                myMap.put("request", "register");
                myMap.put("body", gson.toJson(cliente));


                new clientSenderGson(gson.toJson(myMap)).execute();


            }catch(Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(RegisterController.this, MainActivity.class);
            startActivity(intent);
        }

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
                final String response = (String)(ois.readObject());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
