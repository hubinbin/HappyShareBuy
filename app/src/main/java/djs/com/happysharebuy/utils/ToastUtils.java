package djs.com.happysharebuy.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hbb on 2017/5/19.
 * Toast工具类
 */

public class ToastUtils {

    /**
     * 提示TOAST
     * @param context
     * @param msg
     */
    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,300).show();
    }
}
