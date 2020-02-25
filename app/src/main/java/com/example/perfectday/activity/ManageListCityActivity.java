package com.example.perfectday.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.perfectday.R;
import com.example.perfectday.adapter.HistoryAdapter;
import com.example.perfectday.asynctask.RequestWeatherAsync;
import com.example.perfectday.database.HistoryDatabase;
import com.example.perfectday.model.CurrentWeather;
import com.example.perfectday.model.HistoryModel;
import com.example.perfectday.ulti.DeletionSwipeHelper;
import com.example.perfectday.ulti.Global;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class ManageListCityActivity extends AppCompatActivity implements DeletionSwipeHelper.OnSwipeListener {

    private static final String TAG = "ManageListCityActivity";

    @BindView(R.id.imgBackHome)
    ImageView imgBackHome;
    @BindView(R.id.rvListCity)
    RecyclerView rvListCity;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;

    RequestWeatherAsync requestWeatherAsync;
    ArrayList<String> listData;
    static ArrayList<HistoryModel> mListHistory;
    HistoryAdapter mHistoryAdapter;

    public final int REQUEST_SEARCH_CITY = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manage_list_city_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ButterKnife.bind(this);

        mListHistory = new ArrayList<>();
        Intent bundle = getIntent();
        listData = bundle.getStringArrayListExtra("listName");
        Log.d(TAG, "onCreate: " + listData.size());

        loadData();

        if (mListHistory.size() > 0)
            Log.d(TAG, "onCreate: " + mListHistory.get(0).getCityName());


        imgBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageListCityActivity.this, SearchActivity.class);
                intent.putStringArrayListExtra("listName", listData);
                startActivityForResult(intent, REQUEST_SEARCH_CITY);
            }
        });
    }

    //History adapter item click listener
    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            String name = listData.get(position);
            final Intent intent = new Intent();
            intent.putExtra("cityName", name);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    public void loadData() {
        for (int i = 0; i < listData.size(); i++) {
            loadDataByCityName(listData.get(i));
        }
    }

    ManageListCityActivity manageListCityActivity = this;

    public HistoryModel loadDataByCityName(String cityName) {

        HistoryModel model = new HistoryModel();
        String url = Global.LINK_API + Global.CURRENT_WEATHER_CITY_NAME + cityName.trim() + Global.API_KEY;
        Log.d(TAG, "loadDataByCityName: " + url);
        requestWeatherAsync = (RequestWeatherAsync) new RequestWeatherAsync() {
            @Override
            protected void onPostExecute(CurrentWeather currentWeather) {
                super.onPostExecute(currentWeather);
                if (currentWeather.getCod() == 200) {
                    model.setCityName(currentWeather.getName());
                    model.setTemp(currentWeather.getMain().getTemp());
                    model.setHumidity(currentWeather.getMain().getHumidity());
                    model.setDeg(currentWeather.getWind().getDeg());
                    model.setSpeed(currentWeather.getWind().getSpeed());
                    model.setImgName(currentWeather.getWeather().get(0).getIcon());
                    model.setCountry(currentWeather.getSys().getCountry());
                    manageListCityActivity.getResponse(model);
                } else {
                    Toasty.error(getBaseContext(), "Lỗi tải dữ liệu").show();
                }
            }
        }.execute(url);
        return model;
    }

    //TODO get response
    public void getResponse(HistoryModel historyModel) {
        mListHistory.add(historyModel);
        mHistoryAdapter = new HistoryAdapter(mListHistory, this);
        rvListCity.setAdapter(mHistoryAdapter);
        mHistoryAdapter.notifyDataSetChanged();
        mHistoryAdapter.setItemClickListener(vOnClickListener);

        ItemTouchHelper.Callback itemCallback = new DeletionSwipeHelper(0, ItemTouchHelper.START, this, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemCallback);
        itemTouchHelper.attachToRecyclerView(rvListCity);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEARCH_CITY) {
            if (resultCode == RESULT_OK) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int position) {
        HistoryAdapter.MyViewHolder myViewHolder = (HistoryAdapter.MyViewHolder) viewHolder;
        myViewHolder.removeItem(position);
        listData.remove(position);

        //Update to database
        HistoryDatabase historyDatabase = new HistoryDatabase(getApplicationContext());
        historyDatabase.updateData(listData);

        mHistoryAdapter.notifyDataSetChanged();
    }

}
