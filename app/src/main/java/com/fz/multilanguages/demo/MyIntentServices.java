package com.fz.multilanguages.demo;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyIntentServices extends IntentService {

    public MyIntentServices() {
        super("MyIntentServices");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), getString(R.string.intent_service_create), Toast.LENGTH_SHORT).show();
    }
}
