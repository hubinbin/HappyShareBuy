package djs.com.happysharebuy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences 保存信息类
 * @author hbb
 */
public class PrefUtil {

	// 关于SharePref 操作
	private static Editor editor;
	private static SharedPreferences pref;
	private static final String APPLICATION_PREFERENCES = "HSB_data";

	/**账号*/
	public static String USER_ACCOUNT = "account";
	/**密码*/
	public static String USER_PASSWORD = "password";
	/**用户名*/
	public static String USER_NAME = "user_name";
	/**用户头像*/
	public static String USER_AVATAR = "user_avatar";
	/**用户ID*/
	public static String USER_ID = "user_id";


	/**
	 * 保存字符串Pref
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void savePref(Context context, String key, String value) {
		initEditor(context);
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 保存布尔值Pref
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void savePref(Context context, String key, boolean value) {
		initEditor(context);
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 保存整形Pref
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void savePref(Context context, String key, int value) {
		initEditor(context);
		editor.putInt(key, value);
		editor.commit();
	}

	public static void removePref(Context context, String prefKey) {
		initEditor(context);
		editor.remove(prefKey);
		editor.commit();
	}

	/**
	 * 获取pref中key对应的字符串值
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getStringPref(Context context, String key) {
		return getStringPref(context, key, "");
	}

	/**
	 * 获取pref中key对应的字符串值 制定返回默认值
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getStringPref(Context context, String key, String defaultValue) {
		initPref(context);
		return pref.getString(key, defaultValue);
	}

	/**
	 * 获取pref中key对应的布尔值
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBooleanPref(Context context, String key, boolean defaultValue) {
		initPref(context);
		return pref.getBoolean(key, defaultValue);
	}

	/**
	 * 获取pref中key对应的整形
	 *
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getIntPref(Context context, String key, int defaultValue) {
		initPref(context);
		return pref.getInt(key, defaultValue);
	}

	private static void initEditor(Context context) {
		initPref(context);
		editor = pref.edit();
	}

	private static void initPref(Context context) {
		if (null == pref)
			pref = context.getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
	}

}
