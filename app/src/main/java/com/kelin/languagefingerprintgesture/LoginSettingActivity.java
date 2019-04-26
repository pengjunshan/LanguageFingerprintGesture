package com.kelin.languagefingerprintgesture;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kelin.languagefingerprintgesture.FingerprintDialogFragment.OnFingerprintSetting;
import com.kelin.languagefingerprintgesture.base.BaseActivity;
import com.kelin.languagefingerprintgesture.utils.Constants;
import com.kelin.languagefingerprintgesture.utils.MyToast;
import com.kelin.languagefingerprintgesture.utils.PreferenceUtils;
import com.kelin.languagefingerprintgesture.utils.ToolUtils;
import javax.crypto.Cipher;

public class LoginSettingActivity extends BaseActivity {

  @BindView(R.id.changeGesture)
  AppCompatTextView changeGesture;
  @BindView(R.id.fingerprintImg)
  ImageView fingerprintImg;
  @BindView(R.id.gestureImg)
  ImageView gestureImg;
  @BindView(R.id.fingerprintCL)
  ConstraintLayout fingerprintCL;
  @BindView(R.id.gestureLockCL)
  ConstraintLayout gestureLockCL;
  @BindView(R.id.fingerprintTV)
  AppCompatTextView fingerprintTV;

  private final int SETGESTURELOCK = 100;
  private Context mContext;
  private Boolean isFingerprint, isGesture;
  private Cipher cipher;
  private FingerprintDialogFragment dialogFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login_setting);
    ButterKnife.bind(this);
    initView();
  }

  private void initView() {
    mContext = this;
    isFingerprint = PreferenceUtils.getBoolean(Constants.ISFINGERPRINT_KEY, false);
    isGesture = PreferenceUtils.getBoolean(Constants.ISGESTURELOCK_KEY, false);
    if (ToolUtils.supportFingerprint(this)) {
      /**
       * 生成一个对称加密的key
       */
      ToolUtils.initKey();

      /**
       * 生成一个Cipher对象
       */
      cipher = ToolUtils.initCipher();
      if (isFingerprint) {
        fingerprintImg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.open));
        fingerprintTV.setVisibility(View.VISIBLE);
      }
    } else {
      fingerprintCL.setVisibility(View.GONE);
    }

    if (isGesture) {
      gestureImg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.open));
      changeGesture.setVisibility(View.VISIBLE);
    }

  }

  @OnClick({R.id.fingerprintImg, R.id.gestureImg, R.id.changeGesture})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.fingerprintImg:
        if (isFingerprint) {
          showDeleteDialog();
        } else {
          showFingerPrintDialog(cipher);
        }
        break;

      case R.id.gestureImg:
        if (isGesture) {
          gestureImg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.close));
          PreferenceUtils.commitBoolean(Constants.ISGESTURELOCK_KEY, false);
          changeGesture.setVisibility(View.GONE);
          isGesture = false;
        } else {
          startActivityForResult(SetGestureLockActivity.class, null, SETGESTURELOCK);
        }
        break;

      case R.id.changeGesture:
        Intent intent = new Intent(mContext, GestureLockActivity.class);
        intent.putExtra("type", "change");
        startActivity(intent);
        break;
    }
  }


  private void showFingerPrintDialog(Cipher cipher) {
    dialogFragment = new FingerprintDialogFragment();
    dialogFragment.setCipher(cipher);
    dialogFragment.show(getSupportFragmentManager(), "fingerprint");

    dialogFragment.setOnFingerprintSetting(new OnFingerprintSetting() {
      @Override
      public void onFingerprint(boolean isSucceed) {
        if (isSucceed) {
          isFingerprint = true;
          PreferenceUtils.commitBoolean(Constants.ISFINGERPRINT_KEY, true);
          MyToast.showToastLong("指纹设置成功！");
          fingerprintImg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.open));
          fingerprintTV.setVisibility(View.VISIBLE);
        } else {
          MyToast.showToastLong("指纹设置失败！");
        }
      }
    });
  }

  private void showDeleteDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("南网商旅");
    builder.setMessage("是否关闭指纹登录？");
    builder.setNegativeButton("取消", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    builder.setPositiveButton("确定", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        fingerprintImg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.close));
        fingerprintTV.setVisibility(View.GONE);
        changeGesture.setVisibility(View.GONE);
        isFingerprint = false;
        PreferenceUtils.commitBoolean(Constants.ISFINGERPRINT_KEY, false);
        dialog.dismiss();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case SETGESTURELOCK:
          isGesture = true;
          gestureImg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.open));
          changeGesture.setVisibility(View.VISIBLE);
          PreferenceUtils.commitBoolean(Constants.ISGESTURELOCK_KEY, true);
          break;
      }
    }
  }

}
