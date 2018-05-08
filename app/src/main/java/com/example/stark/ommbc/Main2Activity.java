package com.example.stark.ommbc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;



public class Main2Activity extends AppCompatActivity {
    private List<Quote> quoteList = new ArrayList<>();
    private RecyclerView recyclerview;
    private QuoteAdapter qAdapter;

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

        recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerview, new RecyclerTouchListener.ClickListener(){
            @Override
            public void onClick(View view, int position){
                Quote quote = quoteList.get(position);
                Toast.makeText(getApplicationContext(), quote.getName() + "fue selecionado!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void on LongClick(View view, int position){
                //add event for long click
            }
        }));

        prepareQuoteData();

    }

    private void prepareQuoteData(){
        //get quote info and displays to view

        qAdapter.notifyDataSetChange();
    }
}
