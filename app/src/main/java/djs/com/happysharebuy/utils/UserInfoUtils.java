package djs.com.happysharebuy.utils;

import android.content.Context;

import djs.com.happysharebuy.entity.UserInfoBean;

/**
 * Created by bin on 2017/7/3.
 * 处理用户信息的类
 */

public class UserInfoUtils {

    /**
     * 保存用户信息
     * @param context
     * @param userInfoBean
     */
    public static void saveUserInfo(Context context, UserInfoBean userInfoBean){
        PrefUtil.savePref(context,PrefUtil.USER_ID,userInfoBean.getStatus());
        PrefUtil.savePref(context,PrefUtil.USER_NAME,userInfoBean.getMsg());
        PrefUtil.savePref(context,PrefUtil.USER_AVATAR,userInfoBean.getTximg());
    }

    /**
     * 修改头像
     * @param context
     * @param tximg
     */
    public static void modifyAvatar(Context context,String tximg){
        PrefUtil.savePref(context,PrefUtil.USER_AVATAR,tximg);
    }

}
