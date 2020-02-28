package com.example.perfectday.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfectday.R;
import com.example.perfectday.model.Day;
import com.example.perfectday.model.ExpandItem;

import java.util.ArrayList;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.MyViewHolder> {

    private static final String TAG = "ParentAdapter";
    private ArrayList<String> mListStringDay;
    private ArrayList<Day> inputDays;
    private Context mContext;
    private ArrayList<ExpandItem> items;

    public ParentAdapter(ArrayList<String> mListStringDay, ArrayList<Day> days, Context mContext) {
        this.mListStringDay = mListStringDay;
        this.inputDays = days;
        this.mContext = mContext;
        items = new ArrayList<>();
        items = getAll();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_parent, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    public ArrayList<Day> getDayByDate(String date) {
        ArrayList<Day> dates = new ArrayList<>();
        for (int i = 0; i < inputDays.size(); i++) {
            if (inputDays.get(i).getDate().equals(date)) {
                dates.add(inputDays.get(i));
            }
        }
        return dates;
    }

    public ArrayList<ExpandItem> getAll() {
        ArrayList<ExpandItem> list = new ArrayList<>();
        for (int i = 0; i < mListStringDay.size(); i++) {
            ArrayList<Day> days = getDayByDate(mListStringDay.get(i));
            ExpandItem expandItem = new ExpandItem();
            expandItem.setDays(days);
            expandItem.setExpand(false);
            list.add(expandItem);
        }
        Log.d(TAG, "getAll: " + list.size());
        return list;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ExpandItem expandItem = items.get(position);
        ArrayList<Day> list = expandItem.getDays();
        Log.d(TAG, "onBindViewHolder: list : " + list.size());
        holder.tvDate.setText(mListStringDay.get(position));

        ForecastAdapter mForecastAdapter = new ForecastAdapter(list, mContext);
        holder.rvParent.setAdapter(mForecastAdapter);
        holder.rvParent.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
        holder.rvParent.setHasFixedSize(false);
        mForecastAdapter.notifyDataSetChanged();

        holder.rvParent.setVisibility(View.GONE);

        holder.imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleArrow(v, expandItem.isExpand(), holder.rvParent);
                expandItem.setExpand(!expandItem.isExpand());
            }
        });
    }

    public static void expandCollapse(View view) {

        boolean expand = view.getVisibility() == view.GONE;
        Interpolator easeInOutQuart = PathInterpolatorCompat.create(0.77f, 0f, 0.175f, 1f);

        view.measure(
                View.MeasureSpec.makeMeasureSpec(((View) view.getParent()).getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        int height = view.getMeasuredHeight();
        int duration = (int) (height / view.getContext().getResources().getDisplayMetrics().density);

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (expand) {
//                    view.getLayoutParams().height = 1;
//                    if (interpolatedTime == 1) {
//                        view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    } else {
//                        view.getLayoutParams().height = 525;
//                    }
                    view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    view.setVisibility(View.VISIBLE);
                    view.requestLayout();
                } else {
                    if (interpolatedTime == 1) {
                        view.setVisibility(View.GONE);
                    } else {
//                        view.getLayoutParams().height = height - (int) (height * interpolatedTime);
                        view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        view.requestLayout();
                    }
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setInterpolator(easeInOutQuart);
        animation.setDuration(duration);
        view.startAnimation(animation);

    }

    public void toggleArrow(View view, boolean isExpand, RecyclerView recyclerView) {
        if (isExpand) {
            view.animate().setDuration(300).rotation(0f);
            expandCollapse(recyclerView);
        } else {
            view.animate().setDuration(300).rotation(180f);
            expandCollapse(recyclerView);
        }
    }

    @Override
    public int getItemCount() {
        return mListStringDay.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout main;
        TextView tvDate;
        RecyclerView rvParent;
        ImageView imgExpand;
        LinearLayout layoutExpand;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            rvParent = itemView.findViewById(R.id.rvParent);
            imgExpand = itemView.findViewById(R.id.imgExpand);
            layoutExpand = itemView.findViewById(R.id.layoutExpand);
            main = itemView.findViewById(R.id.main);
        }
    }
}
