package com.example.stark.QuoteNote;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Settings extends AppCompatActivity {

    ClienteFree cliente;
    private Gson gson = new Gson();
    Button validate, logout;
    TextView nombre, apellido, email;
    EditText pipchange;

    private Map<String,String> subsRequest = new HashMap<String,String>();

    //String ip = "10.12.47.30";
    int port = 12345;
    Socket socket;
    ObjectInputStream ois;
    String message;
    ObjectOutputStream oos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nombre = (TextView) findViewById(R.id.text_input_edit_name2);
        apellido = (TextView) findViewById(R.id.text_input_edit_last_name2);
        email = (TextView) findViewById(R.id.text_input_edit_email2);
        pipchange = (EditText) findViewById(R.id.text_input_edit_pipchange);
        validate = (Button) findViewById(R.id.menu_button_saveChange);
        logout = (Button) findViewById(R.id.menu_button_logout);

        cliente = (ClienteFree) getIntent().getSerializableExtra("Cliente");
        nombre.setText("Name: " + cliente.getName()+ " " + cliente.getLastName());
        email.setText("Email: "  + cliente.getEmail());

        validate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    cliente.setPipChange(Integer.parseInt(pipchange.getText().toString()));
                    InputMethodManager imm = (InputMethodManager)getSystemService(Settings.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(pipchange.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    pipchange.setText("");
                    Toast.makeText(Settings.this, "Margin updated to "+cliente.getPipChange()+" pips", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    pipchange.setText("");
                    Toast.makeText(Settings.this, "Please enter only integer numbers", Toast.LENGTH_LONG).show();
                }
            }
        });

        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    subsRequest.put("request","logout");
                    subsRequest.put("body",gson.toJson(cliente));

                    new requestSenderGson(subsRequest).execute();

                    Intent newIntent = new Intent(Settings.this,MainActivity.class);
                    startActivity(newIntent);
                    finish();

                }catch (Exception e){
                    Toast.makeText(Settings.this, "Please enter only integer numbers", Toast.LENGTH_LONG).show();
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
                //socket = new Socket(ip, port);
                socket = new Socket("app2.quotenote.com",port);

                //Send the message to the server
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(requestGson);
                oos.flush();
                System.out.println("Message sent to the server : "+ subsRequest.get("request"));

                //Get the return message from the server
                ois = new ObjectInputStream(socket.getInputStream());
                switch(subsRequest.get("request")){
                    case "logout":
                        message = (String)ois.readObject();
                        ois.close();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        });
                        System.out.println("Message received from the server : " + cliente.getName() );
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}