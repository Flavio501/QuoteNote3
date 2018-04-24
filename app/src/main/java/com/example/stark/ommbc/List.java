package com.example.stark.ommbc;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Quotes");
        setSupportActionBar(myToolbar);
        ArrayList<String> list = new ArrayList<String>();

        /*API myAPI = new API();
        java.util.List<Observable> myquotes = new ArrayList<Observable>();
        try{
            myquotes = myAPI.parseHTML(new URL("http://webrates.truefx.com/rates/connect.html?f=html"));
        }catch(Exception e){
            e.printStackTrace();
        }*/

        /*for(Observable q : myquotes){
            list.add(((Quote) q).getName());
        }*/

        /*list.add(((Quote)myquotes.get(0)).returnParams());
        list.add(((Quote)myquotes.get(1)).returnParams());
        list.add(((Quote)myquotes.get(2)).returnParams());
        list.add(((Quote)myquotes.get(3)).returnParams());
        list.add(((Quote)myquotes.get(4)).returnParams());
        */

        list.add("EUR/USD");
        list.add("USD/JPY");
        list.add("GBP/USD");
        list.add("EUR/GBP");
        list.add("USD/CHF");
        list.add("EUR/JPY");
        list.add("EUR/CHF");
        list.add("USD/CAD");
        list.add("AUD/USD");
        list.add("GBP/JPY");


        ListView prob = (ListView)findViewById(R.id.problemas);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String> (this,R.layout.mylist, list);
        //final ArrayAdapter<Observable> adapter = new ArrayAdapter<Observable> (this,R.layout.mylist, myquotes);
        prob.setAdapter(adapter);
        prob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent pro = new Intent(List.this,Problema_General.class);
                pro.putExtra("Quote list", 0);
                String test = "Test ";
                pro.putExtra("Texto", test);
                startActivity(pro);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_filter) Toast.makeText(getApplicationContext(), "Búsqueda", Toast.LENGTH_SHORT).show();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


}
