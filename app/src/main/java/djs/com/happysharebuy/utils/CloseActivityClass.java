package djs.com.happysharebuy.utils;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 *activity管理类
 * @author hbb
 */
public class CloseActivityClass {
	public static List<Activity> activityList = new ArrayList<Activity>();

	public static void exitClient(Context ctx)
	{
		// 关闭所有Activity
		for (int i = 0; i < activityList.size(); i++)
		{
			if (null != activityList.get(i))
			{
				activityList.get(i).finish();
			}
		}
		//		ActivityManager activityMgr = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE );
		//		activityMgr.restartPackage(ctx.getPackageName());
		//		System.exit(0);
		if (activityList != null) {
			activityList.clear();
		}
	}

	/**
	 * 移除activity
	 * @param activity
	 */
	public static void removeActivity (Activity activity) {
		if (activityList != null) {
			if (activityList.contains(activity)) {
				activityList.remove(activity);
			}
		}
	}
}
