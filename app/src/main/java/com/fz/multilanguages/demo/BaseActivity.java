package com.fz.multilanguages.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        Context context = MultiLanguage.attachContext(newBase);
//        super.attachBaseContext(context);
//        try {
//            Resources resources = context.getResources();
//            Configuration configuration = resources.getConfiguration();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                applyOverrideConfiguration(configuration);
//                return;
//            }
//            applyOverrideConfiguration(configuration);
//            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//        } catch (Exception ignored) {
//        }
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
