package com.example.dustapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView state_img;
        TextView dustValue;
        TextView dustName;

        MyViewHolder(View v){
            super(v);
            state_img = v.findViewById(R.id.state_img);
            dustValue = v.findViewById(R.id.dustValue);
            dustName = v.findViewById(R.id.dustName);
        }
    }

    private ArrayList<ListInfo> ListInfoArrayList;
    MyAdapter(ArrayList<ListInfo> ListInfoArrayList){
        this.ListInfoArrayList = ListInfoArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_dust, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        myViewHolder.state_img.setImageResource(ListInfoArrayList.get(i).drawableId);
        myViewHolder.dustValue.setText(ListInfoArrayList.get(i).dustValue);
        myViewHolder.dustName.setText(ListInfoArrayList.get(i).dustName);
    }

    @Override
    public int getItemCount() {
        return ListInfoArrayList.size();
    }
}