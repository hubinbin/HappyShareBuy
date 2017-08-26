package djs.com.happysharebuy.activity.main;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.utils.GPS_Interface;
import djs.com.happysharebuy.utils.GPS_Presenter;
import djs.com.happysharebuy.utils.LocationUtils;
import djs.com.happysharebuy.utils.PermissionManage;

/**
 * 主页面
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends FragmentActivity  {


    public static MainActivity instance = null;

    private LayoutInflater layoutInflater;
    @ViewInject(android.R.id.tabhost)
    private FragmentTabHost mTabHost;

    // 定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_client,
            R.drawable.tab_input,R.drawable.tab_ranking, R.drawable.tab_setting};

    // Tab选项卡的文字
    private String[] mTextviewArray;

    private ClientFragment clientFragment;
    private InputFragment inputFragment;
    private SettingFragment settingFragment;
    private RankedFragment rankedFragment;
    public String str_latitude;
    public String str_longitude;

    private GPS_Presenter gps_presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        instance = this;
        initView();
        logic();
    }

    /**
     * 初始化设置控件
     */
    private void initView() {

//        gps_presenter = new GPS_Presenter(this, this);
        mTextviewArray = getResources().getStringArray(R.array.tab_name);

        clientFragment = new ClientFragment();
        inputFragment = new InputFragment();
        rankedFragment = new RankedFragment();
        settingFragment = new SettingFragment();

        Class[] fragment = {clientFragment.getClass(),
                inputFragment.getClass(), rankedFragment.getClass(),settingFragment.getClass()};


        // 实例化布局对象
        layoutInflater = LayoutInflater.from(this);
        // 实例化TabHost对象，得到TabHost
        //		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        // 得到fragment的个数
        int count = fragment.length;
        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
//            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragment[i], null);
        }
        mTabHost.setCurrentTab(0);
        mTabHost.getTabWidget().setDividerDrawable(null);
    }

    /**
     * 逻辑处理
     */
    private void logic() {
//        if (PermissionManage.getPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {//判断是否开启定位权限
//            String location = LocationUtils.getLocation(this);
//            if (!TextUtils.isEmpty(location)) {
//                String[] strs = location.split(",");
//                str_latitude = strs[0];
//                str_longitude = strs[1];
//            }
//        }

    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.tab_imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView tabName = (TextView) view.findViewById(R.id.tab_name);
        tabName.setText(mTextviewArray[index]);
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        //释放资源
        if (gps_presenter != null) {
            gps_presenter.onDestroy();
        }

    }

//    @Override
//    public void gpsSwitchState(boolean gpsOpen) {
//        if (gpsOpen) {
//            String location = LocationUtils.getLocation(this);
//            if (!TextUtils.isEmpty(location)) {
//                String[] strs = location.split(",");
//                str_latitude = strs[0];
//                str_longitude = strs[1];
//                if (InputFragment.instance != null && location != null) {
//                    InputFragment.instance.hideLocationHError();
//                    InputFragment.instance.str_latitude = str_latitude;
//                    InputFragment.instance.str_longitude = str_longitude;
//                }
//
//            }
//        } else {
////            Toast.makeText(this, " 手机GPS 关闭", Toast.LENGTH_SHORT).show();
//        }
//    }


}
