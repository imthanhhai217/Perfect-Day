package com.example.perfectday.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.perfectday.R;
import com.example.perfectday.database.Database;
import com.example.perfectday.model.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.edtSearch)
    AutoCompleteTextView edtSearch;
    @BindView(R.id.imgBackHome)
    ImageView imgBackHome;
    String cityName;

    private ArrayList<String> listData;
    private Database dataCity;
    private List<City> listCity;

    private ArrayList<String> cityNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initData();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.search_item, cityNames);
        edtSearch.setAdapter(arrayAdapter);
        edtSearch.setThreshold(1);
        arrayAdapter.notifyDataSetChanged();

        imgBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edtSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + cityNames.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + cityNames.get(position));
                hideKeyboard();
                String newCity = parent.getItemAtPosition(position).toString();
                if (checkExitLocation(newCity)) {
                    //This location is exist
                    final Intent data = new Intent();
                    data.putExtra("cityName", newCity);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                } else {
                    //This location isn't exist
                    final Intent data = new Intent();
                    data.putExtra("newCity", newCity);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        });
    }

    private void initData() {
        dataCity = new Database(getBaseContext());
        Set<City> cities = dataCity.getUsersList();
        listCity = new ArrayList<>();
        listCity.addAll(cities);

        Intent bundle = getIntent();
        listData = bundle.getStringArrayListExtra("listName");
        Log.d(TAG, "onCreate: " + listData.size());

        cityNames = new ArrayList<>();
        for (City city : listCity) {
            cityNames.add(city.getName());
        }
    }

    private static final String TAG = "SearchActivity";

    public void hideKeyboard() {
        try {
            InputMethodManager inputmanager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputmanager != null) {
                inputmanager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception var2) {
        }

    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    public boolean checkExitLocation(String name) {
        boolean re = false;
        for (String data : listData) {
            Log.d(TAG, "checkExitLocation: data " + data + " name : " + name);
            if (data.trim().equals(name)) {
                re = true;
                break;
            }
        }
        Log.d(TAG, "checkExitLocation: " + re);
        return re;
    }
}
