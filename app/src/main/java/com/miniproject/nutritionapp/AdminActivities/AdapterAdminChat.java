package com.miniproject.nutritionapp.AdminActivities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miniproject.nutritionapp.R;

import java.util.List;

import me.fahmisdk6.avatarview.AvatarView;

public class  AdapterAdminChat extends RecyclerView.Adapter< AdapterAdminChat.MyViewHolder> {

    List< ModelAdminChat> list;
    Context context;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public  AdapterAdminChat(Context context, List< ModelAdminChat> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_adminchat,parent,false);
        return new MyViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         ModelAdminChat m =list.get(position);

        holder.mName.setText(m.getChatName());
        holder.mLM.setText(m.getChatId());
        holder.mDP.bind(m.getChatName(),null);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mName,mLM;
        LinearLayout mLL;
        AvatarView mDP;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);

            mLL = itemView.findViewById(R.id.chatlist_container);

            mName = itemView.findViewById(R.id.chatlist_topicname);
            mLM = itemView.findViewById(R.id.chatlist_lm);
            mDP = itemView.findViewById(R.id.chatlist_dp);

            mLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(getAdapterPosition());
                }
            });

        }
    }
}

