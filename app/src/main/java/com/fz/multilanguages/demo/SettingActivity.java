package com.fz.multilanguages.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fz.multilanguages.demo.utils.LocalManageUtil;

public class SettingActivity extends BaseActivity {
    private TextView mUserSelect;

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mUserSelect = findViewById(R.id.tv_user_select);
        mUserSelect.setText(getString(R.string.user_select_language,
                LocalManageUtil.getSelectLanguage(this)));
        setClick();
    }

    public static void enter(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    private void selectLanguage(int select) {
        LocalManageUtil.saveSelectLanguage(this, select);
        MainActivity.reStart(this);
    }

    private void setClick() {
        //跟随系统
        findViewById(R.id.btn_auto).setOnClickListener(v -> selectLanguage(0));
        //简体中文
        findViewById(R.id.btn_cn).setOnClickListener(v -> selectLanguage(1));
        //繁体中文
        findViewById(R.id.btn_traditional).setOnClickListener(v -> selectLanguage(2));
        //english
        findViewById(R.id.btn_en).setOnClickListener(v -> selectLanguage(3));
    }
}
