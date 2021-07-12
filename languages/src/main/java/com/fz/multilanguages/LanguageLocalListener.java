package com.fz.multilanguages;

import android.content.Context;

import java.util.Locale;


/**
 * 获取当前设置的语言监听器
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/8 10:42
 */
public interface LanguageLocalListener {

    /**
     * 获取选择设置语言
     *
     * @param context
     * @return
     */
    Locale getLanguageLocale(Context context);

}
