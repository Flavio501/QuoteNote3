package com.example.stark.ommbc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.MyViewHolder>{
    private List<Quote> quoteList;
    AdapterView.OnItemClickListener mItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Name, timestamp, bidBig, bidPoints, offerBig, offerPoints, High, Low, Open;

        public MyViewHolder(View view) {
            super(view);
            Name = (TextView) view.findViewById(R.id.Name);
            //timestamp = (TextView) view.findViewById(R.id.timestamp);
            bidBig = (TextView) view.findViewById(R.id.bidBig);
            bidPoints = (TextView) view.findViewById(R.id.bidPoints);
            offerBig = (TextView) view.findViewById(R.id.offerBig);
            offerPoints = (TextView) view.findViewById(R.id.offerPoints);
            //High = (TextView) view.findViewById(R.id.High);
            //Low = (TextView) view.findViewById(R.id.Low);
            //Open = (TextView) view.findViewById(R.id.Open);
            }
        }
        public QuoteAdapter(List<Quote> quoteList){
            this.quoteList = quoteList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            Quote q = quoteList.get(position);
            holder.Name.setText(q.getName());
            //holder.timestamp.setText(q.getTimestamp());
            holder.bidBig.setText(q.getBidBig().toString());
            holder.bidPoints.setText(String.valueOf(q.getBidPoints()));
            holder.offerBig.setText(q.getOfferBig().toString());
            holder.offerPoints.setText(String.valueOf(q.getOfferPoints()));
            //holder.High.setText(q.getHigh().toString());
            //holder.Low.setText(q.getLow().toString());
            //holder.Open.setText(q.getOpen().toString());
        }
        @Override
        public int getItemCount() {
            return quoteList.size();
        }
}
