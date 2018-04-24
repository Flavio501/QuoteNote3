package com.example.stark.ommbc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class Quotes extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        API myAPI = new API();
        List<Observable> myquotes = new ArrayList<Observable>();
        /*try{
            myquotes = myAPI.parseHTML(new URL("http://webrates.truefx.com/rates/connect.html?f=html"));
        }catch(Exception e){
            e.printStackTrace();
        }*/
        View rootView = inflater.inflate(R.layout.fragment_problemas, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        /*for(Observable q : myquotes){
            textView.setText(((Quote) q).returnParams());
        }*/
        textView.setText("Quote Values: ");
        return rootView;
    }

}
