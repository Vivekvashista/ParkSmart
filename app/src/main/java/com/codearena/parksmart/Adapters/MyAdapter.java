package com.codearena.parksmart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codearena.parksmart.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;
    String[] names;
    int[] images;
    OnItemListener onItemListener;

    public MyAdapter(Context v, String[] a, int[] imag, OnItemListener onItemListener) {
        context = v;
        names = a;
        images = imag;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_layout_file,parent,false);
        return new MyViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text1.setText(names[position]);
        holder.image.setImageResource(images[position]);

    }

    @Override
    public int getItemCount() {
        return names.length;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView text1;
        ImageView image;
        OnItemListener onItemListener;

        public MyViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            text1 = itemView.findViewById(R.id.recycler_text1_id);
            image = itemView.findViewById(R.id.recycler_image_id);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClicked(int position);
    }

}
