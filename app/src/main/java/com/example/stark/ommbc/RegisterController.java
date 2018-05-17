package com.example.stark.ommbc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterController extends AppCompatActivity {
    Button validate;
    EditText email, password;
    ClienteFree cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.text_input_edit_email);
        password = (EditText) findViewById(R.id.text_input_edit_password);
        validate = (Button) findViewById(R.id.menu_button_register);

    }

    protected boolean validatePassword(String password) {
        EditText pwc = (EditText) findViewById(R.id.text_input_edit_confirm_password);

        if (!password.equals(pwc.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    //vali
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

            Intent intent = new Intent(RegisterController.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(RegisterController.this, "Se ha creado la cuenta!", Toast.LENGTH_LONG).show();
        }

    }
}
