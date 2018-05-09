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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class Main2Activity extends AppCompatActivity {
    private List<Quote> quoteList = new ArrayList<>();
    private RecyclerView recyclerview;
    private QuoteAdapter qAdapter;
    private Quote q;
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

        try{
        prepareQuoteData();
        }catch (Exception e){
            e.printStackTrace();
        }
        /*recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerview, new RecyclerTouchListener.ClickListener(){
            @Override
            public void onClick(View view, int position){
                Quote quote = quoteList.get(position);
                Toast.makeText(getApplicationContext(), quote.getName() + "fue selecionado!", Toast.LENGTH_SHORT).show();
            }
        }));
        prepareQuoteData();
        */
    }

    private Quote genQuote(String name){
        bd = new BigDecimal("1.5");
        Quote qNew = new Quote(name, 123213,bd, 124,bd,34 ,bd,bd,bd );
        return qNew;
    }

    private void prepareQuoteData() throws Exception{

        // Aqui se crashea, tal vez por los parsings, checa como agregar un quote a la lista
        try{
            //bd = new BigDecimal("1.5");
            //q = new Quote("EUR/USD", 123213,bd, 124,bd,34 ,bd,bd,bd );
            //quoteList.add(q);
            //q = genQuote();
            quoteList.add(genQuote("EUR/USD"));
            quoteList.add(genQuote("EUR/JPY"));
            quoteList.add(genQuote("GBP/AUD"));

            //quoteList.add(q);
            qAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
            //Quote qu = new Quote(
              //      "EUR/USD",
                //    Long.parseLong("1234124123"),
                  //  new BigDecimal("1.55"),
                    //Integer.parseInt("123"),
                    //new BigDecimal("2.11"),
                    //Integer.parseInt("123"),
                    //new BigDecimal("2.14"),
                    //new BigDecimal("2.15"),
                    //new BigDecimal("2.15"));
            //quoteList.add(qu);

     /*    quoteList.add(new Quote(
                "EUR/GBP",
                Long.parseLong("1.22"),
                new BigDecimal("1.22"),
                Integer.parseInt("200"),
                new BigDecimal("200"),
                Integer.parseInt("200"),
                new BigDecimal("200"),
                new BigDecimal("200"),
                new BigDecimal("200")));
                */
    }
}
