package com.example.perfectday.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.perfectday.R;
import com.example.perfectday.model.Day;
import com.example.perfectday.ulti.Global;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    private static final String TAG = "ForecastAdapter";
    private ArrayList<Day> mDays;
    private Context mContext;

    public ForecastAdapter(ArrayList<Day> mDays, Context context) {
        this.mDays = mDays;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_forecast, parent, false);
        mContext = parent.getContext();
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Day day = mDays.get(position);
        Log.d(TAG, "onBindViewHolder: forecast : " + mDays.get(position).getDate());
        holder.tvTemp.setText(day.getTemp() + Global.DEGREES + "\n" + day.getDescription());
        holder.tvDate.setText(day.getHours());
        Glide.with(mContext).load(Global.getImageLink(day.getIcon())).into(holder.imgIcon);
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        ImageView imgIcon;
        TextView tvTemp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvTemp = itemView.findViewById(R.id.tvTemp);
        }
    }
}
