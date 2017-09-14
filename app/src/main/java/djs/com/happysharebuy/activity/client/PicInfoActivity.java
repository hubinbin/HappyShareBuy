package djs.com.happysharebuy.activity.client;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseActivity;

/**
 * Created by bin on 2017/9/14.
 * 图片信息界面
 */
@ContentView(R.layout.pic_info_activity)
public class PicInfoActivity extends BaseActivity{

    @ViewInject(R.id.title_bar_back)
    private LinearLayout back;
    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        back.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.picture_info);


    }

    @Override
    protected void logic() {

    }
}
