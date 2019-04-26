package com.kelin.languagefingerprintgesture.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import com.kelin.languagefingerprintgesture.utils.LocaleManager;
import com.kelin.languagefingerprintgesture.utils.PreferenceUtils;

/**
 * 作者：PengJunShan.
 *
 * 时间：On 2019-04-22.
 *
 * 描述：
 */
public class MyApplication extends Application {

  public static Context context;

  @Override
  public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    /**
     * 初始化SP
     */
    PreferenceUtils.init(context, "TRIP");

    /**
     * 初始化语言
     */
    LocaleManager.setLocale(this);
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(LocaleManager.setLocale(base));
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    LocaleManager.setLocale(this);
  }

}
