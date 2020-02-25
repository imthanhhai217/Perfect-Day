package com.example.perfectday.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.perfectday.R;
import com.example.perfectday.model.HistoryModel;
import com.example.perfectday.ulti.Global;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private static final String TAG = "HistoryAdapter";
    ArrayList<HistoryModel> mListHistory;
    private Context mContext;

    private View.OnClickListener itemClickListener;

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public HistoryAdapter(ArrayList<HistoryModel> listHistory, Context mContext) {
        this.mListHistory = listHistory;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_history, parent, false);
        this.mContext = parent.getContext();
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HistoryModel model = mListHistory.get(position);
        Log.d(TAG, "onBindViewHolder: " + model.getCityName());
        holder.tvCityName.setText(model.getCityName() + "\n -" + model.getCountry()+"-");
        holder.tvTemp.setText(model.getTemp() + Global.DEGREES);
        holder.tvFullWind.setText(formatWind(model.getHumidity(), model.getDeg(), model.getSpeed()));

        Log.d(TAG, "onBindViewHolder: " + Global.getImageLink(model.getImgName()));
        Glide.with(mContext)
                .load(Global.getImageLink(model.getImgName()))
                .centerCrop()
                .placeholder(R.drawable.loading)
                .into(holder.imgIcon);
    }

    private String formatWind(String humidity, String direction, String speed) {
        if (direction != null) {
            return "Độ ẩm : " + humidity + "% | " + direction + " | " + speed;
        } else {
            return "Độ ẩm : " + humidity + "% | " + speed;
        }

    }

    @Override
    public int getItemCount() {
        return mListHistory.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCityName, tvTemp, tvFullWind;
        ImageView imgIcon;
        LinearLayout mainItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvFullWind = itemView.findViewById(R.id.tvFullWind);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            mainItem = itemView.findViewById(R.id.mainItem);
            itemView.setTag(this);
            itemView.setOnClickListener(itemClickListener);
        }

        public void removeItem(int position) {
            mListHistory.remove(position);
            notifyItemRemoved(position);
        }

    }

}
