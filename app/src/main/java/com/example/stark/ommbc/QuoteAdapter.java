package com.example.stark.ommbc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class QuoteAdapter RecyclerView.Adapter<QuoteAdapter.MyViewHolder>{
    private List<Quote> quoteList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Name, timestamp, bidBig, bidPoints, offerBig, offerPoints, High, Low, Open;

        public MyViewHolder(View view) {
            super(view);
            Name = (TextView) view.findViewById(R.id.Name);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            bidBig = (TextView) view.findViewById(R.id.bidBig);
            bidPoints = (TextView) view.findViewById(R.id.bidPoints);
            offerBig = (TextView) view.findViewById(R.id.offerBig);
            offerPoints = (TextView) view.findViewById(R.id.offerPoints);
            High = (TextView) view.findViewById(R.id.High);
            Low = (TextView) view.findViewById(R.id.Low);
            Open = (TextView) view.findViewById(R.id.Open);
        }

        public QuoteAdapter(List<Quote> quoteList){
            this.quoteList = quoteList;
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            Quote q = quoteList.get(position);
            holder.Name.setText(quote.getName());
            holder.timestamp.setText(quote.getTimestamp());
            holder.bidBig.setText(quote.getBidBig());
            holder.bidPoints.setText(quote.getBidPoints);
            holder.offerBig.setText(quote.getOfferBig);
            holder.offerPoints.setText(quote.getOfferPoints());
            holder.High.setText(quote.getHigh());
            holder.Low.setText(quote.getLow());
            hodler.Open.setText(quote.getOpen());
        }

        @Override
        public int getItemCount(){
            return quoteList.size();
        }

    }
}
