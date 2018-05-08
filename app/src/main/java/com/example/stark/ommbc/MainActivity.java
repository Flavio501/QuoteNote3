package com.example.stark.ommbc;


        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.view.View.OnClickListener;


public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.menu_button_login);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    Evaluar si el correo y contra existen
                */
                Intent MyIntent = new Intent(MainActivity.this, List.class);
                //Intent MyIntent = new Intent(MainActivity.this,Activity_Tabbed.class);
                startActivity(MyIntent);
                finish(); //Block back button

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


}

