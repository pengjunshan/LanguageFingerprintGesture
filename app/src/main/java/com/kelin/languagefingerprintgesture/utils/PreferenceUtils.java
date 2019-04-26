package com.kelin.languagefingerprintgesture.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.Set;

/**
 * @author：PengJunShan.

 * 时间：On 2019-03-29.

 * 描述：sp存储
 */
public class PreferenceUtils {

	private static final String TAG = "TRIP";
	private static SharedPreferences mSharedPreferences = null;
	/**
	 * 初始化
	 */
	public static void init(Context applicationContext, String spName) {
		mSharedPreferences = applicationContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
	}

	/**
	 * apply异步提交 效率高
	 */
	public static void applyInt(String key, int value) {
		applyKeyValue(key, value);
	}

	public static void applyLong(String key, long value) {
		applyKeyValue(key, value);
	}

	public static void applyBoolean(String key, boolean value) {
		applyKeyValue(key, value);
	}

	public static void applyString(String key, String value) {
		if (null == value) {//防止传入null时无法判断存储类型
			applyKeyValue(key, "");
			return;
		}
		applyKeyValue(key, value);
	}


	/**
	 * commit同步提交 效率低
	 */
	public static boolean commitInt(String key, int value) {
		return commitKeyValue(key, value);
	}

	public static boolean commitLong(String key, long value) {
		return commitKeyValue(key, value);
	}

	public static boolean commitBoolean(String key, boolean value) {
		return commitKeyValue(key, value);
	}

	public static boolean commitString(String key, String value) {
		if (null == value) {//防止传入null时无法判断存储类型
			return commitKeyValue(key, "");
		}
		return commitKeyValue(key, value);
	}


	/**
	 * 获取值
	 */
	public static int getInt(String key, int defValue) {
		return (int) getValue(key, defValue);
	}

	public static long getLong(String key, long defValue) {
		return (long) getValue(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return (boolean) getValue(key, defValue);
	}

	public static String getString(String key, String defValue) {
		if (null == defValue) {//防止传入null时无法判断存储类型
			return (String) getValue(key, "");
		}
		return (String) getValue(key, defValue);
	}


	/**
	 * 异步提交保存
	 */
	private static void applyKeyValue(String key, Object value) {
		getEditor(key,value).apply();
	}


	/**
	 * 同步提交保存
	 */
	private static boolean commitKeyValue(String key, Object value) {
		return getEditor(key,value).commit();
	}

	/**
	 * 删除key
	 */
	public static void removeKey(String key) {
		if (mSharedPreferences != null) {
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.remove(key);
			editor.apply();
		} else {
			Log.e(TAG, "mSharedPreferences is null");
		}
	}

	/**
	 * 清楚所有数据
	 */
	public static void clear(){
		if (mSharedPreferences != null) {
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.clear();
			editor.apply();
		} else {
			Log.e(TAG, "mSharedPreferences is null");
		}
	}


	/**
	 * 统一获取处理
	 */
	private static Object getValue(String key, Object defValue) {
		if (mSharedPreferences == null) {
			Log.e(TAG, "mSharedPreferences is null");
			return defValue;
		}
		if (defValue instanceof String) {
			return mSharedPreferences.getString(key, (String) defValue);
		} else if (defValue instanceof Integer) {
			return mSharedPreferences.getInt(key, (Integer) defValue);
		} else if (defValue instanceof Long) {
			return mSharedPreferences.getLong(key, (Long) defValue);
		} else if (defValue instanceof Float) {
			return mSharedPreferences.getFloat(key, (Float) defValue);
		} else if (defValue instanceof Boolean) {
			return mSharedPreferences.getBoolean(key, (Boolean) defValue);
		} else if (defValue instanceof Set) {
			return mSharedPreferences.getStringSet(key, (Set<String>) defValue);
		}
		return defValue;
	}

	private static SharedPreferences.Editor getEditor(String key, Object value) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		if (value instanceof String) {
			editor.putString(key, (String) value);
		} else if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (Float) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		} else if (value instanceof Set) {
			editor.putStringSet(key, (Set<String>) value);
		}
		return editor;
	}

}
