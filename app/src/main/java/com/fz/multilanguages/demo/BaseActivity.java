package com.fz.multilanguages.demo;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    public void applyOverrideConfiguration(Configuration configuration) {
        if (configuration != null) {
            configuration.setTo(getBaseContext().getResources().getConfiguration());
        }
        super.applyOverrideConfiguration(configuration);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
