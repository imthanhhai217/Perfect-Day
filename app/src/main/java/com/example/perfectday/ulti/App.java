package com.example.perfectday.ulti;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.stetho.Stetho;

public class App extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(getApplicationContext());
    }
}
