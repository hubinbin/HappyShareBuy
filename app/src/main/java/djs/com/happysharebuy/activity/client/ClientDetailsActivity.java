package djs.com.happysharebuy.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.adapter.TabAdapter;
import djs.com.happysharebuy.entity.ClientBean;

/**
 * Created by bin on 2017/9/15.
 * 客户详情页面
 */
@ContentView(R.layout.client_dateils_aty)
public class ClientDetailsActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.title_bar_back)
    private LinearLayout back;
    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.title_bar_right_img)
    private ImageView right_img;

    @ViewInject(R.id.client_details_viewpager)
    private ViewPager viewpager;

    @ViewInject(R.id.client_details_info_tv)
    private TextView tv_info;
    @ViewInject(R.id.client_details_pic_tv)
    private TextView tv_pic;

    @ViewInject(R.id.client_details_info_line)
    private View line_info;
    @ViewInject(R.id.client_details_pic_line)
    private View line_pic;

    private List<Fragment> fragments = new ArrayList<Fragment>();

    private TabAdapter tabAdapter;
    private PicInfoFragment picInfoFragment;
    private ClientInfoFragment clientInfoFragment;


    public ClientBean clientBean;
    public int Id;

    private int page = 0;//当前页


    public static ClientDetailsActivity instance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        back.setVisibility(View.VISIBLE);
        right_img.setVisibility(View.VISIBLE);
        right_img.setImageResource(R.mipmap.icon_edit);

        tabAdapter = new TabAdapter(getSupportFragmentManager());

        picInfoFragment = new PicInfoFragment();
        clientInfoFragment = new ClientInfoFragment();
        fragments.add(clientInfoFragment);
        fragments.add(picInfoFragment);

        tabAdapter.setFragmentData(fragments);
        viewpager.setAdapter(tabAdapter);
        viewpager.addOnPageChangeListener(this);
    }

    /**
     * 逻辑
     */
    private void logic() {
        Intent intent = getIntent();
        clientBean = (ClientBean) intent.getSerializableExtra("client");
        tv_title.setText(clientBean.getShanghu());
        Id = clientBean.getId();

    }

    @Event(value = {R.id.title_bar_back, R.id.client_details_info, R.id.client_details_pic, R.id.title_bar_right_img})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                ClientDetailsActivity.this.finish();
                break;
            case R.id.client_details_info:
                changeTabState(0, false);
                break;
            case R.id.client_details_pic:
                changeTabState(1, false);
                break;
            case R.id.title_bar_right_img:
                if (page == 0) {
                    clientInfoFragment.toModifyInfo();
                } else {
                    picInfoFragment.toAddPicture();
                }

                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int index = viewpager.getCurrentItem();
        changeTabState(index, true);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * 改变tab按钮状态
     *
     * @param index
     */
    private void changeTabState(int index, boolean isPageScrolled) {
        page = index;
        hideLine();
        TextView tv = null;
        switch (index) {
            case 0:
                tv = tv_info;
                right_img.setImageResource(R.mipmap.icon_edit);
                line_info.setVisibility(View.VISIBLE);
                line_pic.setVisibility(View.INVISIBLE);
                clientInfoFragment.setGetFocus();
                picInfoFragment.setCloseFocus();
                break;
            case 1:
                tv = tv_pic;
                right_img.setImageResource(R.mipmap.icon_add);
                line_info.setVisibility(View.INVISIBLE);
                line_pic.setVisibility(View.VISIBLE);
                clientInfoFragment.setCloseFocus();
                picInfoFragment.setGetFocus();
                break;
        }
        tv.setTextColor(ContextCompat.getColor(ClientDetailsActivity.this, R.color.red_FC3D3D));
        if (!isPageScrolled) {
            viewpager.setCurrentItem(index);
        }
    }

    /**
     * 初始设置tab TextView颜色
     */
    private void hideLine() {
        tv_info.setTextColor(ContextCompat.getColor(ClientDetailsActivity.this, R.color.black));
        tv_pic.setTextColor(ContextCompat.getColor(ClientDetailsActivity.this, R.color.black));
    }

}
