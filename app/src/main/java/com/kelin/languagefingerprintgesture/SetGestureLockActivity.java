package com.kelin.languagefingerprintgesture;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kelin.languagefingerprintgesture.gesture.GestureLockDisplayView;
import com.kelin.languagefingerprintgesture.gesture.GestureLockLayout;
import com.kelin.languagefingerprintgesture.gesture.GestureLockLayout.OnLockResetListener;
import com.kelin.languagefingerprintgesture.utils.Constants;
import com.kelin.languagefingerprintgesture.utils.PreferenceUtils;
import com.kelin.languagefingerprintgesture.utils.ToolUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：PengJunShan.

 * 时间：On 2019-04-22.

 * 描述：手势登录
 */
public class SetGestureLockActivity extends AppCompatActivity {

  @BindView(R.id.display_view)
  GestureLockDisplayView mLockDisplayView;
  @BindView(R.id.setting_hint)
  TextView mSettingHintText;
  @BindView(R.id.gesture_view)
  GestureLockLayout mGestureLockLayout;
  @BindView(R.id.reSet)
  TextView reSet;
  @BindView(R.id.hintTV)
  TextView hintTV;
  private Animation animation;
  private Context mContext;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gesture_login);
    ButterKnife.bind(this);
    initView();
  }

  private void initView() {
    mContext = this;
    //设置提示view 每行每列点的个数
    mLockDisplayView.setDotCount(3);
    //设置提示view 选中状态的颜色
    mLockDisplayView.setDotSelectedColor(Color.parseColor("#01367A"));
    //设置提示view 非选中状态的颜色
    mLockDisplayView.setDotUnSelectedColor(Color.parseColor("#999999"));
    //设置手势解锁view 每行每列点的个数
    mGestureLockLayout.setDotCount(3);
    //设置手势解锁view 最少连接数
    mGestureLockLayout.setMinCount(4);
    //设置手势解锁view 模式为重置密码模式
    mGestureLockLayout.setMode(GestureLockLayout.RESET_MODE);

    //初始化动画
    animation = AnimationUtils.loadAnimation(this, R.anim.shake);
    initEvents();
  }

  private void initEvents() {

    mGestureLockLayout.setOnLockResetListener(new OnLockResetListener() {
      @Override
      public void onConnectCountUnmatched(int connectCount, int minCount) {
        //连接数小于最小连接数时调用
        mSettingHintText.setText("最少连接" + minCount + "个点");
        resetGesture();
      }

      @Override
      public void onFirstPasswordFinished(List<Integer> answerList) {
        //第一次绘制手势成功时调用
        Log.e("TAG","第一次密码=" + answerList);
        mSettingHintText.setText("确认解锁图案");
        //将答案设置给提示view
        mLockDisplayView.setAnswer(answerList);
        //重置
        resetGesture();
      }

      @Override
      public void onSetPasswordFinished(boolean isMatched, List<Integer> answerList) {
        //第二次密码绘制成功时调用
        Log.e("TAG","第二次密码=" + answerList.toString());
        if (isMatched) {
          //两次答案一致，保存
          PreferenceUtils.commitString(Constants.GESTURELOCK_KEY, answerList.toString());
          setResult(RESULT_OK);
          finish();
        } else {
          hintTV.setVisibility(View.VISIBLE);
          ToolUtils.setVibrate(mContext);
          hintTV.startAnimation(animation);
          mGestureLockLayout.startAnimation(animation);
          resetGesture();
        }
      }
    });
  }

  /**
   * 重置手势布局（只是布局）
   */
  private void resetGesture() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        mGestureLockLayout.resetGesture();
      }
    }, 300);
  }

  /**
   * 重置手势布局（布局加逻辑）
   */
  @OnClick(R.id.reSet)
  public void onViewClicked() {
    mGestureLockLayout.setOnLockResetListener(null);
    mSettingHintText.setText("绘制解锁图案");
    mLockDisplayView.setAnswer(new ArrayList<Integer>());
    mGestureLockLayout.resetGesture();
    mGestureLockLayout.setMode(GestureLockLayout.RESET_MODE);
    hintTV.setVisibility(View.INVISIBLE);
    initEvents();
  }

}
