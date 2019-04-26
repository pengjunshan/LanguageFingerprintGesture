package com.kelin.languagefingerprintgesture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kelin.languagefingerprintgesture.utils.LocaleManager;
import com.kelin.languagefingerprintgesture.utils.PreferenceUtils;

/**
 * @author：PengJunShan. 时间：On 2019-04-22.
 *
 * 描述：切换多语言
 */
public class LanguageActivity extends AppCompatActivity {

  @BindView(R.id.rbChinese)
  RadioButton rbChinese;
  @BindView(R.id.rbEnglish)
  RadioButton rbEnglish;
  @BindView(R.id.rgLanguages)
  RadioGroup rgLanguages;
  @BindView(R.id.commit)
  AppCompatButton commit;

  private String mLanguageType;
  private boolean languageType;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_language);
    ButterKnife.bind(this);
    initView();
  }

  private void initView() {
    /**
     * 初始化判断使用的是什么语言
     */
    if (PreferenceUtils.getBoolean("isChinese", true)) {
      rbChinese.setChecked(true);
    } else {
      rbEnglish.setChecked(true);
    }
    /**
     * 监听RadioGroup
     */
    rgLanguages.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
          case R.id.rbChinese:
            mLanguageType = LocaleManager.LANGUAGE_CHINESE;
            languageType = true;
            break;

          case R.id.rbEnglish:
            mLanguageType = LocaleManager.LANGUAGE_ENGLISH;
            languageType = false;
            break;
        }
      }
    });
  }

  @OnClick(R.id.commit)
  public void onViewClicked() {
    if (!TextUtils.isEmpty(mLanguageType)) {
      /**
       * 修改语言
       */
      LocaleManager.setNewLocale(LanguageActivity.this, mLanguageType);
      /**
       * 保存使用语言标识
       */
      PreferenceUtils.commitBoolean("isChinese", languageType);
      /**
       * 跳转到主页 杀死其它所有的页面 重新加载资源文件
       */
      Intent i = new Intent(LanguageActivity.this, MainActivity.class);
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(i);
      finish();
    }
  }

}
