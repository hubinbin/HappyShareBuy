package djs.com.happysharebuy.utils;

import android.app.Activity;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.HttpException;

/**
 *网络异常处理 
 * @author hbb
 */
public class ErrorDispose {

	/**
	 * 返回异常处理
	 * @param context
	 * @param code
	 * @param message
	 */
	public static void resultError (Activity context,int code,String message){
		if (context == null) {
			return;
		}
		//		if (!TextUtils.isEmpty(message)) {
		//			ToastUtils.showToast(context, message);
		//		}
//		switch (code) {
//		case 1:
//			ToastUtils.showToast(context, "参数错误");
//			break;
//		case 2:// 服务器异常
//			ToastUtils.showToast(context,"服务器异常请稍后再试");
//			break;
//		case 3:
//			PrefUtil.savePref(context, PrefUtil.ACTOMATIC,false);
//			Intent intent = new Intent();
//			intent.setClass(context, LoginActivity.class);
//			context.startActivity(intent);
//			context.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
//			CloseActivityClass.exitClient(context);
//			if(MainActivity.instance != null){
//				MainActivity.instance.finish();
//			}
//			break;
//
//		default:
//			ToastUtils.showToast(context, message);
//			break;
//		}
	}

	/**
	 * 网络异常处理
	 * @param ex
	 * @param isOnCallback
	 */
	public static void xHttpError (Throwable ex, boolean isOnCallback) {
		if (ex instanceof HttpException) { // 网络错误
			HttpException httpEx = (HttpException) ex;
			int responseCode = httpEx.getCode();
			String responseMsg = httpEx.getMessage();
			String errorResult = httpEx.getResult();
			LogUtil.e("code="+responseCode+"\nmsg"+responseMsg+"\nresult"+errorResult);
		}
	}

}
