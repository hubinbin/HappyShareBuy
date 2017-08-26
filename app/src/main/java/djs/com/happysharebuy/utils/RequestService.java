package djs.com.happysharebuy.utils;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import djs.com.happysharebuy.R;

/**
 * Created by bin on 2017/7/1.
 * 网络请求
 */

public class RequestService {

    private static CustomCallback customCallback;
    private static Context context;

    /**
     * 回调方法
     */
    public interface CustomCallback {
        void onSuccess(String result);
        void onFinished();
    }

    /**
     * post请求 返回jsonObject
     *
     * @param context
     * @param params
     * @param customCallback
     */
    public static void post(final Context context, RequestParams params, final CustomCallback customCallback) {
        RequestService.customCallback = customCallback;
        RequestService.context = context;
        x.http().post(params, callbackJSONObject);
    }

    /**
     * get请求 返回jsonArray
     *
     * @param context
     * @param params
     * @param customCallback
     */
    public static void getArray(final Context context, RequestParams params, final CustomCallback customCallback) {
        RequestService.customCallback = customCallback;
        RequestService.context = context;
        x.http().get(params, callbackJSONArray);
    }

    /**
     * 网络异常提示
     *
     * @param context
     * @param str
     */
    private static void TostErr(Context context, String str) {
        ToastUtils.showToast(context, str);
    }



    /**请求JsonObject */
    static Callback.CommonCallback callbackJSONObject = new Callback.CommonCallback<JSONObject>() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
            String str = jsonObject.toString();
            LogUtil.i(str);
            if (!TextUtils.isEmpty(str)) {
                customCallback.onSuccess(str);
            } else {
                TostErr(context, XmlUtils.getString(context, R.string.net_error));
            }
        }

        @Override
        public void onError(Throwable throwable, boolean b) {
            ErrorDispose.xHttpError(throwable, b);
            TostErr(context, XmlUtils.getString(context, R.string.net_error));
        }

        @Override
        public void onCancelled(CancelledException e) {

        }

        @Override
        public void onFinished() {
            customCallback.onFinished();
        }
    };


    /**请求JsonArray */
    static Callback.CommonCallback callbackJSONArray = new Callback.CommonCallback<JSONArray>() {
        @Override
        public void onSuccess(JSONArray jsonArray) {
            String str = jsonArray.toString();
            LogUtil.i(str);
            if (!TextUtils.isEmpty(str)) {
                customCallback.onSuccess(str);
            } else {
                TostErr(context, XmlUtils.getString(context, R.string.net_error));
            }
        }

        @Override
        public void onError(Throwable throwable, boolean b) {
            ErrorDispose.xHttpError(throwable, b);
            TostErr(context, XmlUtils.getString(context, R.string.net_error));
        }

        @Override
        public void onCancelled(CancelledException e) {

        }

        @Override
        public void onFinished() {
            customCallback.onFinished();
        }
    };


}
