package com.codearena.parksmart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codearena.parksmart.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>
{
    Context context;
    String[] time;
    String[] description;

    public NotificationAdapter(Context context, String[] ids, String[] add)
    {
        this.context = context;
        this.description = ids;
        this.time= add;
    }
    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notifications_layout_file,parent,false);
        return new NotificationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        holder.descr.setText(description[position]);
        holder.tim.setText(time[position]);
    }

    @Override
    public int getItemCount() {
        return description.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView descr, tim;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            descr = itemView.findViewById(R.id.label_description_id);
            tim = itemView.findViewById(R.id.label_time_id);
        }
    }
}
