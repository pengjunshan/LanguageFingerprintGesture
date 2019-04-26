package com.kelin.languagefingerprintgesture;

import android.os.Bundle;
import com.kelin.languagefingerprintgesture.base.BaseActivity;

/**
 * @author：PengJunShan.

 * 时间：On 2019-04-22.

 * 描述：指纹登录
 */
public class FingerprintLoginActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fingerprint_login);
  }
}
