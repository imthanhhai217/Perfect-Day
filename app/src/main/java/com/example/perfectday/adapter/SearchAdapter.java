package com.example.perfectday.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class SearchAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {
    public SearchAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
