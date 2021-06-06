package com.codearena.parksmart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codearena.parksmart.R;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder>
{
    Context context;
    String[] bookingids;
    String[] Address;
    String[] dateTime;
    String[] status;
    OnBookingItemListener onBookingItemListener;

    public BookingAdapter(Context context, String[] ids, String[] add, String[] dt, String[] status, OnBookingItemListener listener)
    {
        this.context = context;
        this.bookingids = ids;
        this.Address= add;
        this.dateTime = dt;
        this.status = status;
        this.onBookingItemListener = listener;
    }
    @NonNull
    @Override
    public BookingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.bookings_layout_file,parent,false);
        return new MyViewHolder(view, onBookingItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.MyViewHolder holder, int position) {
        holder.bookingid.setText(bookingids[position]);
        holder.add.setText(Address[position]);
        if(status[position].equalsIgnoreCase("success")) {
            holder.stat.setBackgroundResource(R.drawable.online_circle);
            holder.dt.setText("Success on "+dateTime[position]);
        }
        else {
            holder.stat.setBackgroundResource(R.drawable.offline_circle);
            holder.dt.setText("Pending on "+dateTime[position]);
        }
    }

    @Override
    public int getItemCount() {
        return bookingids.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView bookingid, add, dt, stat;
        OnBookingItemListener clickListener;
        public MyViewHolder(@NonNull View itemView, OnBookingItemListener obtl)
        {
            super(itemView);
            bookingid = itemView.findViewById(R.id.txt_booking_id);
            add = itemView.findViewById(R.id.txt_address_id);
            dt = itemView.findViewById(R.id.txt_date_time_id);
            stat = itemView.findViewById(R.id.txt_status_id);

            this.clickListener = obtl;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }
    public interface OnBookingItemListener
    {
        void onItemClick(int position);
    }
}
