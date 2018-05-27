package com.example.stark.QuoteNote;

import android.content.Intent;
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


public class Settings extends AppCompatActivity {

    ClienteFree cliente;
    private Gson gson = new Gson();
    Button validate;
    TextView nombre, apellido, email;
    EditText pipchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nombre = (TextView) findViewById(R.id.text_input_edit_name2);
        apellido = (TextView) findViewById(R.id.text_input_edit_last_name2);
        email = (TextView) findViewById(R.id.text_input_edit_email2);
        pipchange = (EditText) findViewById(R.id.text_input_edit_pipchange);
        validate = (Button) findViewById(R.id.menu_button_saveChange);

        cliente = (ClienteFree) getIntent().getSerializableExtra("Cliente");

        //Toolbar
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setTitle("Quote Details");

        nombre.setText("Name: " + cliente.getName()+ " " + cliente.getLastName());
        //apellido.setText(cliente.getLastName());
        email.setText("Email: "  + cliente.getEmail());
        //pipchange.setText(cliente.getPipChange());

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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("cliente", cliente);
        setResult(RESULT_OK, intent);
        finish();
    }
}