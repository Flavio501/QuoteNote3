package com.example.stark.ommbc;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.ImageFormat;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.*;
import java.io.*;
import java.util.*;
import android.content.pm.*;
import android.content.*;
import android.util.Log;
import android.Manifest;
import java.text.*;
import android.database.Cursor;


public class Problema_General extends AppCompatActivity{

    int fav = 0; // evaluar si el cliente esta suscrito a este quote

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema__general);
        //Recuperar Quote y Cliente
        Quote quote = (Quote) getIntent().getSerializableExtra("Quote");
        ClienteFree cliente = (ClienteFree) getIntent().getSerializableExtra("Cliente");

        //Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Quote Details");
        TextView espacio_texto = (TextView)findViewById(R.id.espacio_texto);
        espacio_texto.setText(quote.returnParams());
        FloatingActionButton help = (FloatingActionButton)findViewById(R.id.help);
        //FloatingActionButton send = (FloatingActionButton)findViewById(R.id.send);
        final FloatingActionButton favorite = (FloatingActionButton)findViewById(R.id.favorite);

        //imageView = (ImageView)findViewById(R.id.imageView);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fav==0) {
                    //llamar Subscribe a este Quote

                    favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite1));
                    fav=1;
                }
                else {
                    //llamar Unsubscribe de este quote
                    favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite0));
                    fav=0;
                }
            }
        });
        /*help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","correo@ejemplo.mx", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ayuda problema "+numero);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Necesito ayuda con el problema "+numero);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        */

    }
}
