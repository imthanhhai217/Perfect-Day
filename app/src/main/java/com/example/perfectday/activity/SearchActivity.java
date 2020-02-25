package com.example.perfectday.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectday.R;
import com.example.perfectday.asynctask.RequestWeatherAsync;
import com.example.perfectday.database.Database;
import com.example.perfectday.model.City;
import com.example.perfectday.model.CurrentWeather;
import com.example.perfectday.ulti.FlowLayout;
import com.example.perfectday.ulti.Global;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.edtSearch)
    AutoCompleteTextView edtSearch;
    @BindView(R.id.imgBackHome)
    ImageView imgBackHome;
    @BindView(R.id.fl)
    FlowLayout flowLayout;
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
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    String newCity = edtSearch.getText().toString();
                    checkLocationIsReal(newCity);
                }
                return false;
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
        addDataToFlowLayout();
    }

    public void addDataToFlowLayout() {
        for (int i = 0; i < 30; i++) {
            flowLayout.addView(createTextView(cityNames.get(i))
                    , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public View createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setBackgroundResource(R.drawable.bg_flow);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkExitLocation(textView.getText().toString().trim())) {
                    //This location is exist
                    final Intent data = new Intent();
                    data.putExtra("cityName", textView.getText().toString().trim());
                    setResult(Activity.RESULT_OK, data);
                    finish();
                } else {
                    //This location isn't exist
                    final Intent data = new Intent();
                    data.putExtra("newCity", textView.getText().toString().trim());
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        });
        return textView;
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
            if (data.trim().equals(name) || data.trim().equalsIgnoreCase(name)) {
                re = true;
                break;
            }
        }
        Log.d(TAG, "checkExitLocation: " + re);
        return re;
    }

    RequestWeatherAsync requestWeatherAsync;

    public void checkLocationIsReal(String cityName) {
        requestWeatherAsync = (RequestWeatherAsync) new RequestWeatherAsync() {
            @Override
            protected void onPostExecute(CurrentWeather currentWeather) {
                super.onPostExecute(currentWeather);
                if (currentWeather.getCod() == 200) {
                    if (checkExitLocation(currentWeather.getName())) {
                        //This location is exist
                        final Intent data = new Intent();
                        data.putExtra("cityName", currentWeather.getName());
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    } else {
                        //This location isn't exist
                        final Intent data = new Intent();
                        data.putExtra("newCity", currentWeather.getName());
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    }
                } else {
                    Toasty.error(getBaseContext(), "Vị trí không tồn tại vui lòng thử lại !").show();
                    edtSearch.setText("");
                }
            }
        }.execute(Global.LINK_API + Global.CURRENT_WEATHER_CITY_NAME + cityName + Global.API_KEY);
    }
}
