package com.miniproject.nutritionapp.HomeActivities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miniproject.nutritionapp.R;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyViewHolder> {

    List<ModelMessage> list;
    Context context;

    public AdapterChat(List<ModelMessage> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_message,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelMessage m = list.get(position);
        if (m.isSender()){
            holder.recieved.setVisibility(View.GONE);
            holder.rtime.setVisibility(View.GONE);

            holder.sent.setText(m.getMessage());
            holder.stime.setText(m.getTime());
        }else {
            holder.sent.setVisibility(View.GONE);
            holder.stime.setVisibility(View.GONE);

            holder.recieved.setText(m.getMessage());
            holder.rtime.setText(m.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sent,recieved,stime,rtime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sent = itemView.findViewById(R.id.sent_message);
            recieved = itemView.findViewById(R.id.recieved_message);
            stime = itemView.findViewById(R.id.stime_message);
            rtime = itemView.findViewById(R.id.rtime_message);

        }
    }
}
