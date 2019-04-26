package com.kelin.languagefingerprintgesture;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject;
import android.support.v4.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

@TargetApi(23)
public class FingerprintDialogFragment extends DialogFragment {

//    private FingerprintManager fingerprintManager;
    private FingerprintManagerCompat fingerprintManagerCompat;

    private CancellationSignal mCancellationSignal;

    private Cipher mCipher;

    private Context mActivity;

    private TextView errorMsg;

    /**
     * 标识是否是用户主动取消的认证。
     */
    private boolean isSelfCancelled;

    public void setCipher(Cipher cipher) {
        mCipher = cipher;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        fingerprintManager = getContext().getSystemService(FingerprintManager.class);
        fingerprintManagerCompat =  FingerprintManagerCompat.from(mActivity);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fingerprint_dialog, container, false);
        errorMsg = v.findViewById(R.id.error_msg);
        TextView cancel = v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                stopListening();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开始指纹认证监听
        startListening(mCipher);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 停止指纹认证监听
        stopListening();
    }

    private void startListening(Cipher cipher) {
        isSelfCancelled = false;
        mCancellationSignal = new CancellationSignal();
        FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);

//        fingerprintManager.authenticate(new FingerprintManager.CryptoObject(cipher), mCancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode, CharSequence errString) {
//                if (!isSelfCancelled) {
//                    errorMsg.setText(errString);
//                    if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
//                        Toast.makeText(mActivity, errString, Toast.LENGTH_SHORT).show();
//                        dismiss();
//                    }
//                }
//            }
//
//            @Override
//            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//                errorMsg.setText(helpString);
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
//                Toast.makeText(mActivity, "指纹认证成功", Toast.LENGTH_SHORT).show();
//                mActivity.onAuthenticated();
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                errorMsg.setText("指纹认证失败，请再试一次");
//            }
//        }, null);

        fingerprintManagerCompat.authenticate(cryptoObject, 0, mCancellationSignal, new MyCallBack(), null);

    }

    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {

        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            if (!isSelfCancelled) {
                errorMsg.setText(errString);
                Log.e("TAG", "errMsgId="+errMsgId);
                Toast.makeText(mActivity, "errMsgId="+errMsgId, Toast.LENGTH_SHORT).show();
                if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                    Log.e("TAG", ""+errString);
                    dismiss();
                }
            }
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            errorMsg.setText("指纹认证失败，请再试一次");
            Log.e("TAG", "onAuthenticationFailed");
        }

        //错误时提示帮助，比如说指纹错误，我们将显示在界面上 让用户知道情况
        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            errorMsg.setText(helpString);
            Log.e("TAG", "helpString="+helpString);
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            CryptoObject cryptoObject = result.getCryptoObject();
            try {
                byte[] bytes = cryptoObject.getCipher().doFinal();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            if(onFingerprintSetting!=null) {
                onFingerprintSetting.onFingerprint(true);
            }
            dismiss();
        }
    }

    private void stopListening() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
            isSelfCancelled = true;
        }
    }

    private OnFingerprintSetting onFingerprintSetting;

    public void setOnFingerprintSetting(
        OnFingerprintSetting onFingerprintSetting) {
        this.onFingerprintSetting = onFingerprintSetting;
    }

    public interface OnFingerprintSetting{
        void onFingerprint(boolean isSucceed);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopListening();
    }
}
