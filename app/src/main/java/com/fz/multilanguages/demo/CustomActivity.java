package com.fz.multilanguages.demo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fz.multilanguages.MultiLanguage;

public class CustomActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = MultiLanguage.attachContext(newBase);
        super.attachBaseContext(context);

        try {
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            if (Build.VERSION.SDK_INT >= 24) {
                this.applyOverrideConfiguration(configuration);
                return;
            }
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        } catch (Exception var5) {
        }
    }
}
