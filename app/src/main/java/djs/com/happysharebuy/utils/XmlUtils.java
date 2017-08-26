package djs.com.happysharebuy.utils;

import android.content.Context;

/**
 * Created by hbb on 2017/6/28.
 * 获取xml字符串工具
 */

public class XmlUtils {

    /**
     * 获取xml string
     * @param context
     * @param id
     * @return
     */
    public static String getString (Context context , int id){
        if (context == null || id == 0){
            return "";
        }
        String str = context.getResources().getString(id);
        return  str;
    }

}
