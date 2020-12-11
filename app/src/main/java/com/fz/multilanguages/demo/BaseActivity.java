package com.fz.multilanguages.demo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
        test1(null);
    }

    protected boolean test1(View view) {
        return view != null;
    }

    protected ViewGroup clickItem(View view) {
        System.out.println(">>>>>>>>>>>>>>>>>>" + view.getClass().getName());
        if (view instanceof LinearLayout) {
            return (LinearLayout) view;
        }
        if (view instanceof ViewGroup) {
            return (ViewGroup) view;
        }
        return null;
    }
}
