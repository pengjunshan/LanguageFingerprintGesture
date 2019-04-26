package com.kelin.languagefingerprintgesture.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.kelin.languagefingerprintgesture.utils.LocaleManager;

/**
 * 作者：PengJunShan.
 *
 * 时间：On 2019-04-22.
 *
 * 描述：
 */
public class BaseActivity extends FragmentActivity {

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(LocaleManager.setLocale(base));
  }

  /**
   * 不携带数据的页面跳转
   */
  public void startActivity(Class<?> clz) {
    startActivity(clz, null);
  }

  /**
   * 携带数据的页面跳转
   */
  public void startActivity(Class<?> clz, Bundle bundle) {
    Intent intent = new Intent();
    intent.setClass(this, clz);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    startActivity(intent);
  }

  /**
   * 有回调的跳转
   */
  public void startActivityForResult(Class<?> cls, Bundle bundle,
      int requestCode) {
    Intent intent = new Intent();
    intent.setClass(this, cls);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    startActivityForResult(intent, requestCode);
  }

}
