package com.fz.multilanguages;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import androidx.annotation.Keep;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * 多语言设置
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/8 10:36
 */
public class MultiLanguage {
    private MultiLanguage() {
        throw new AssertionError();
    }

    private static LanguageLocalListener languageLocalListener;

    public static void initialize(LanguageLocalListener listener) {
        languageLocalListener = listener;
    }

    public static Context attachContext(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return updateResources(context, getLanguageLocale(context));
            } else {
                return updateResourcesLegacy(context, getLanguageLocale(context));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Context updateResources(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static Context updateResourcesLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }


    /**
     * 设置语言类型
     */
    public static void changeLanguage(Context context) {
        Resources resources = context.getApplicationContext().getResources();
        Configuration config = resources.getConfiguration();
        Locale locale = getLanguageLocale(context);
        config.setLocale(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            config.setLocales(localeList);
            context.getApplicationContext().createConfigurationContext(config);
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


    /**
     * @param context
     */
    public static void onConfigurationChanged(Context context) {
        attachContext(context);
        changeLanguage(context);
    }


    /**
     * 获取选择的语言
     *
     * @param context
     * @return
     */
    private static Locale getLanguageLocale(Context context) {
        if (languageLocalListener != null) {
            return languageLocalListener.getLanguageLocale(context);
        }
        return Locale.ENGLISH;
    }

    /**
     * 获取系统语言
     *
     * @param newConfig
     * @return
     */
    public static Locale getSystemLocal(Configuration newConfig) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = newConfig.getLocales().get(0);
        } else {
            locale = newConfig.locale;
        }
        return locale;
    }

    /**
     * 获取系统语言
     *
     * @param resources
     * @return
     */
    public static Locale getSystemLocal(Resources resources) {
        if (resources != null) {
            return getSystemLocal(resources.getConfiguration());
        }
        return getSystemLocal();
    }

    /**
     * 获取系统语言
     *
     * @return
     */
    public static Locale getSystemLocal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().get(0);
        } else {
            return Locale.getDefault();
        }
    }

    /**
     * 获取系统语言
     *
     * @param context
     * @return
     */
    public static Locale getSystemLocal(Context context) {
        if (context != null) {
            return getSystemLocal(context.getResources());
        }
        return getSystemLocal();
    }
}
