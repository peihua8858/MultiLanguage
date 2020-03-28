package com.fz.multilanguages.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fz.multilanguages.demo.utils.LocalManageUtil;
import com.im.sdk.IMSdk;

public class MainActivity extends BaseActivity {

    private Button startNewActivity;
    private Button startNewIntentService;
    private Button startSettingActivity;
    private Button startNewService,startNewIm;
    //
    private TextView tvSystemLanguage;
    private TextView tvUserSelectLanguage;
    private TextView tvValue;
    private TextView tvValue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //
        setValue();
    }

    private void initView() {
        startNewActivity = findViewById(R.id.btn_1);
        startNewIntentService = findViewById(R.id.btn_2);
        startSettingActivity = findViewById(R.id.btn_3);
        startNewService = findViewById(R.id.btn_4);
        startNewIm = findViewById(R.id.btn_5);
        //
        tvSystemLanguage = findViewById(R.id.tv_system_language);
        tvUserSelectLanguage = findViewById(R.id.tv_user_select);
        tvValue = findViewById(R.id.tv_3);
        tvValue2 = findViewById(R.id.tv_4);
        //
        startNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivity.enter(MainActivity.this);
            }
        });
        //
        startSettingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.enter(MainActivity.this);
            }
        });


        startNewIntentService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyIntentServices.class);
                startService(intent);
            }
        });

        startNewService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
            }
        });
        startNewIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IMSdk.create(MainActivity.this)
                            .setEmail("ding@qq.com")
                            .setNickName("ding")
                            .setWebSiteId("58cb59bfcea4a6b42ab46c04")
                            .setLogEnabled(true)
                            .setRelease(false)
                            .setCountryCode("us")
                            .setLanguage("en")
                            .setWebUrlLoadingView((context1, title, url) -> {
                                return true;
                            })
                            .startup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @SuppressLint("StringFormatInvalid")
    private void setValue() {
        String string = getString(R.string.system_language,
                LocalManageUtil.getSystemLocale(this).getDisplayLanguage());
        tvSystemLanguage.setText(string);
        //
        tvUserSelectLanguage.setText(getString(R.string.user_select_language,
                LocalManageUtil.getSelectLanguage(this)));
        //
        tvValue.setText(getString(R.string.tv3_value));
        //
        tvValue2.setText(getApplicationContext().getString(R.string.tv3_value));
    }

    public static void reStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
