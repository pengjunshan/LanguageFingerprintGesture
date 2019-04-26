package com.kelin.languagefingerprintgesture.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import java.util.Locale;

public class LocaleManager {

    /**
     * 中文
     */
    public static final String LANGUAGE_CHINESE = "zh";

    /**
     * 英文
     */
    public static final String LANGUAGE_ENGLISH = "en";

    /**
     * 初始化语言设置
     */
    public static Context setLocale(Context c) {
        return updateResources(c, getLanguage());
    }

    /**
     * 设置语言
     * @param c
     * @param language
     * @return
     */
    public static Context setNewLocale(Context c, String language) {
        persistLanguage(language);
        return updateResources(c, language);
    }

    /**
     * 得到语言设置
     * @return
     */
    public static String getLanguage() {
        return PreferenceUtils.getString(Constants.LANGUAGE_KEY, LANGUAGE_CHINESE);
    }

    /**
     * 存储设置的语言
     * @param language
     */
    @SuppressLint("ApplySharedPref")
    private static void persistLanguage(String language) {
        PreferenceUtils.commitString(Constants.LANGUAGE_KEY, language);
    }

    /**
     * 更新Locale
     * 适配8.0
     * @param language
     * @return
     */
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context = context.createConfigurationContext(configuration);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        }
        return context;
    }

}