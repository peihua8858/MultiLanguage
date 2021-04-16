package com.fz.multilanguages.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;

import com.fz.multilanguages.demo.utils.LocalManageUtil;
import com.fz.toast.ToastCompat;
import com.im.sdk.IMSdk;
import com.luck.picture.lib.permissions.RxPermissions;
import com.socks.library.KLog;

public class MainActivity extends BaseActivity {

    private Button startNewActivity;
    private Button startNewIntentService;
    private Button startSettingActivity;
    private Button startNewService, startNewIm;
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
        clickItem(startNewActivity);
        KLog.e("uri:" + getIntent().getData());
    }

    private void addShortcut() {
        Intent launchIntent = new Intent(this, MainActivity.class);
        launchIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        launchIntent.setData(Uri.parse("https://www.qq.com"));
        ShortcutInfoCompat pinShortcutInfo =
                new ShortcutInfoCompat.Builder(this, System.currentTimeMillis() + "")
                        .setShortLabel("测试快捷方式").setIntent(launchIntent).build();
        Intent pinnedShortcutCallbackIntent =
                ShortcutManagerCompat.createShortcutResultIntent(this, pinShortcutInfo);
        PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                pinnedShortcutCallbackIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        ShortcutManagerCompat.requestPinShortcut(this, pinShortcutInfo,
                successCallback.getIntentSender());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        KLog.e("uri:" + intent.getData());
    }

    private void initView() {
        startNewActivity = findViewById(R.id.btn_1);
        startNewIntentService = findViewById(R.id.btn_2);
        startSettingActivity = findViewById(R.id.btn_3);
        startNewService = findViewById(R.id.btn_4);
        startNewIm = findViewById(R.id.btn_5);
        findViewById(R.id.btn_6).setOnClickListener(v -> {
            new RxPermissions(this).request(Manifest.permission.INSTALL_SHORTCUT)
                    .subscribe(result -> {
                        if (result) {
                            addShortcut();
                        } else {
                            ToastCompat.showMessage("权限被拒绝。");
                        }
                    });
        });
        //
        tvSystemLanguage = findViewById(R.id.tv_system_language);
        tvUserSelectLanguage = findViewById(R.id.tv_user_select);
        tvValue = findViewById(R.id.tv_3);
        tvValue2 = findViewById(R.id.tv_4);


        //
        startNewActivity.setOnClickListener(v -> SecondActivity.enter(MainActivity.this));
        //
        startSettingActivity.setOnClickListener(v -> SettingActivity.enter(MainActivity.this));


        startNewIntentService.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyIntentServices.class);
            startService(intent);
        });

        startNewService.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyService.class);
            startService(intent);
        });
        startNewIm.setOnClickListener(v -> {
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
