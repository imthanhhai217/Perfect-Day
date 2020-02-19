package com.example.perfectday.adapter;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.example.perfectday.fragment.CurrentWeatherFragment;
import com.example.perfectday.ulti.Global;

import java.util.ArrayList;

public class CurrentWeatherAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> mListCity;
    private ArrayList<CurrentWeatherFragment> mListFragment;
    FragmentActivity mFragmentActivity;

    public CurrentWeatherAdapter(FragmentManager fm, ArrayList<String> listCity, FragmentActivity fragmentActivity) {
        super(fm);
        this.mListCity = listCity;
        this.mFragmentActivity = fragmentActivity;
        mListFragment = new ArrayList<>();
        for (int i = 0; i < listCity.size(); i++) {
            mListFragment.add(CurrentWeatherFragment.newInstance(i, mListCity.get(i)));
        }
    }

    private static final String TAG = "CurrentWeatherAdapter";

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        FragmentManager fragmentManager = ((Fragment) object).getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove((Fragment) object);
        fragmentTransaction.commit();
        super.destroyItem(container, position, object);
    }
}
