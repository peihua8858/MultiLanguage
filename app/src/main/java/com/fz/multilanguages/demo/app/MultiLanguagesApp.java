package com.fz.multilanguages.demo.app;

import android.app.Application;
import android.content.Context;

import com.fz.multilanguages.LanguageLocalListener;
import com.fz.multilanguages.MultiLanguage;
import com.fz.multilanguages.demo.utils.LocalManageUtil;
import com.socks.library.KLog;

import java.util.Locale;

public class MultiLanguagesApp extends Application {
//    @Override
//    protected void attachBaseContext(Context base) {
//        //第一次进入app时保存系统选择语言(为了选择随系统语言时使用，如果不保存，切换语言后就拿不到了）
////        LocalManageUtil.saveSystemCurrentLanguage(base);
//        super.attachBaseContext(MultiLanguage.attachContext(base));
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        //用户在系统设置页面切换语言时保存系统选择语言(为了选择随系统语言时使用，如果不保存，切换语言后就拿不到了）
////        LocalManageUtil.saveSystemCurrentLanguage(getApplicationContext(), newConfig);
//        MultiLanguage.onConfigurationChanged(getApplicationContext());
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiLanguage.initialize(new LanguageLocalListener() {
            @Override
            public Locale getLanguageLocale(Context context) {
                return LocalManageUtil.getSetLanguageLocale(context);
            }
        });
        KLog.d(">>>>>>>>>>>>>>");
    }
//
//    private Configuration mOverrideConfiguration;
//    private Resources mResources;
////
//
//    @Override
//    public Resources getResources() {
//        return getResourcesInternal();
//    }
//
//    private Resources getResourcesInternal() {
//        if (mResources == null) {
//            if (mOverrideConfiguration == null) {
//                mResources = super.getResources();
//            } else {
//                final Context resContext = createConfigurationContext(mOverrideConfiguration);
//                mResources = resContext.getResources();
//            }
//        }
//        return mResources;
//    }
//
//    /**
//     * app 修改多语言统一入口
//     *
//     * @author dingpeihua
//     * @date 2019/10/29 10:39
//     * @version 1.0
//     */
//    public void changeLanguage(Context context) {
//        //因引导页修改语言不能重启APP，所以需要修改Application 语言和国家，否则导致进入APP之后，某些资源未改变
//        //此处切换7.0以上无效
//        mOverrideConfiguration = new Configuration(context.getResources().getConfiguration());
//        mResources = context.getResources();
//    }
}
