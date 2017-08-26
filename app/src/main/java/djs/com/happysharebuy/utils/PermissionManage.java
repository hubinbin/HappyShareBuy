package djs.com.happysharebuy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by bin on 2017/7/7.
 * 全新管理
 */

public class PermissionManage {

    /**
     * 判断是否开启权限
     * @param context 上下文
     * @param permissionValue 权限值
     * @return
     */
    public static boolean getPermission(Context context,String permissionValue){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context,permissionValue);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity)context,new String[]{permissionValue},222);
                return false;
            }else{
                return true;
            }
        } else {
            return true;
        }
    }


}
