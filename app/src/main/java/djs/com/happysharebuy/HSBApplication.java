package djs.com.happysharebuy;

import android.app.Application;

import com.google.gson.Gson;

import org.xutils.x;

/**
 * Created by hbb on 2017/6/28.
 * 全局application
 */

public class HSBApplication extends Application {

    private boolean isDebug = true;

    private static Gson gson;

    private static HSBApplication Instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDebug(isDebug);

        gson = new Gson();
    }

    public static synchronized HSBApplication getInstance() {
        return Instance;
    }


    public static synchronized Gson getGson() {
        return gson;
    }

}
