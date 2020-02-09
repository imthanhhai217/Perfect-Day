package com.example.perfectday.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.perfectday.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WindFragment extends Fragment {


    @BindView(R.id.tvSpeed)
    TextView tvSpeed;
    @BindView(R.id.tvFeelsLike)
    TextView tvFeelsLike;
    @BindView(R.id.tvHumidity)
    TextView tvHumidity;
    @BindView(R.id.tvPressure)
    TextView tvPressure;

    public WindFragment() {
        // Required empty public constructor
    }

    public static WindFragment newInstance(String speed, String feelsLike, String humidity, String pressure) {

        Bundle args = new Bundle();
        WindFragment fragment = new WindFragment();
        args.putString("speed", speed);
        args.putString("feelsLike", feelsLike);
        args.putString("humidity", humidity);
        args.putString("pressure", pressure);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wind, container, false);
        ButterKnife.bind(this, view);
        loadData();
        return view;
    }

    public void loadData() {
        String speed = getArguments().getString("speed");
        tvSpeed.setText(speed);
        String feelsLike = getArguments().getString("feelsLike");
        tvFeelsLike.setText(feelsLike);
        String humidity = getArguments().getString("humidity");
        tvHumidity.setText(humidity);
        String pressure = getArguments().getString("pressure");
        tvPressure.setText(pressure);
    }

}
