package djs.com.happysharebuy.utils;

import android.content.Context;

import org.xutils.http.RequestParams;

/**
 * Created by bin on 2017/7/1.
 * 请求参数类
 */

public class CustomRequestParams {

//    public CustomRequestParams(Context context) {
//        super(HttpUrlUtils.url);
//        int perid = PrefUtil.getIntPref(context, PrefUtil.USER_ID, 0);
//        this.addBodyParameter("perid", perid + "");
//    }
//
//    public CustomRequestParams() {
//        super(HttpUrlUtils.url);
//    }

    /**
     * 不带ID的参数
     * @return
     */
    public static RequestParams getParams(){
        RequestParams params = new RequestParams(HttpUrlUtils.url);
        return params;
    }

    /**
     * 带ID的参数
     * @param context
     * @return
     */
    public static RequestParams getParams(Context context){
        RequestParams params = new RequestParams(HttpUrlUtils.url);
        int perid = PrefUtil.getIntPref(context, PrefUtil.USER_ID, 0);
        params.addBodyParameter("perid", perid + "");
        return params;
    }
}
