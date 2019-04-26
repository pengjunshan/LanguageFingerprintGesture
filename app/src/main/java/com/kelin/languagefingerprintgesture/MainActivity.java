package com.kelin.languagefingerprintgesture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kelin.languagefingerprintgesture.base.BaseActivity;
import com.kelin.languagefingerprintgesture.utils.Constants;
import com.kelin.languagefingerprintgesture.utils.MyToast;
import com.kelin.languagefingerprintgesture.utils.PreferenceUtils;

/**
 * @author：PengJunShan. 时间：On 2019-04-22.
 *
 * 描述：首页
 */
public class MainActivity extends BaseActivity {


  @BindView(R.id.moreLanguageBtn)
  AppCompatTextView moreLanguageBtn;
  @BindView(R.id.fingerprintLoginBtn)
  AppCompatTextView fingerprintLoginBtn;
  @BindView(R.id.loginBtn)
  AppCompatButton loginBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick({R.id.moreLanguageBtn, R.id.fingerprintLoginBtn,R.id.loginBtn})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.moreLanguageBtn:
        startActivity(LanguageActivity.class);
        break;
      case R.id.fingerprintLoginBtn:
        startActivity(LoginSettingActivity.class);
        break;

        case R.id.loginBtn:
          Intent intent = null;
          boolean isFingerprint = PreferenceUtils.getBoolean(Constants.ISFINGERPRINT_KEY, false);
          boolean isGesture = PreferenceUtils.getBoolean(Constants.ISGESTURELOCK_KEY, false);
          if(!isFingerprint && !isGesture) {
            MyToast.showToast("请先设置指纹解锁或手势解锁！");
          }else {
            intent = new Intent(MainActivity.this,GestureLockActivity.class);
            intent.putExtra("type","login");
            startActivity(intent);
          }
            break;
    }
  }
}
