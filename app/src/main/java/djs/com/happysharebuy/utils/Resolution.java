package djs.com.happysharebuy.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * 
 *  @author hbb
 *获得屏幕分辨率
 */
public class Resolution {

	private static int width;
	private static int height;
	private static float density;
	private static int densityDpi;

	private static void getWidthHeight( Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;     // 屏幕宽度（像素）
		height = metric.heightPixels;   // 屏幕高度（像素）
		density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
		densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）

	}
	/**
	 * 得倒屏幕的宽
	 * @param context
	 * @return
	 */
	public static int getWidth (Activity context) {
		getWidthHeight(context);
		return width;

	}
	/**
	 * 得倒屏幕的高
	 * @param context
	 * @return
	 */
	public static int getHeight (Activity context) {
		getWidthHeight(context);
		return height;

	}
}
