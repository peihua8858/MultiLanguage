package com.fz.multilanguages.demo.app;

import androidx.multidex.MultiDexApplication;

import com.fz.multilanguages.MultiLanguage;
import com.fz.multilanguages.demo.utils.LocalManageUtil;
import com.socks.library.KLog;

public class MultiLanguagesApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiLanguage.initialize(LocalManageUtil::getSetLanguageLocale);
        KLog.d(">>>>>>>>>>>>>>");
    }
}
