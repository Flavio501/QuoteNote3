package com.example.stark.ommbc;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class Main2Activity extends AppCompatActivity {
    private ClienteFree cliente;
    private List<Quote> quoteList = new ArrayList<>();
    private RecyclerView recyclerview;
    private QuoteAdapter qAdapter;
    private BigDecimal bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        qAdapter = new QuoteAdapter(quoteList);
        RecyclerView.LayoutManager qLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(qLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(qAdapter);
        recyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerview ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent myIntent = new Intent(Main2Activity.this,Problema_General.class);
                        myIntent.putExtra("Quote",quoteList.get(position));
                        myIntent.putExtra("Cliente",cliente);
                        startActivity(myIntent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        try{
            prepareQuoteData();
        }catch (Exception e){
            e.printStackTrace();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Quote genQuote(String name){
        bd = new BigDecimal("1.5");
        Quote qNew = new Quote(name, 123213,bd, 124,bd,34 ,bd,bd,bd );
        return qNew;
    }

    private void prepareQuoteData() throws Exception{

        try{
            quoteList.add(genQuote("EUR/USD"));
            quoteList.add(genQuote("EUR/JPY"));
            quoteList.add(genQuote("GBP/AUD"));

            //quoteList.add(q);
            qAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
