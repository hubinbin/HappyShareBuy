package djs.com.happysharebuy.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseFragment;
import djs.com.happysharebuy.activity.client.AllClientFragment;
import djs.com.happysharebuy.activity.client.ContactClientFragment;
import djs.com.happysharebuy.activity.client.IntentionClientFragment;
import djs.com.happysharebuy.activity.client.SearchClientActivity;
import djs.com.happysharebuy.activity.client.SignClientFragment;
import djs.com.happysharebuy.adapter.TabAdapter;
import djs.com.happysharebuy.utils.Common;

/**
 * Created by hbb on 2017/6/28.
 * 首页客户
 */

public class ClientFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.title_bar_right)
    private LinearLayout btn_search;
    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;

    @ViewInject(R.id.client_fragment_viewpager)
    private ViewPager viewpager;

    @ViewInject(R.id.client_fragment_all_tv)
    private TextView tv_all;
    @ViewInject(R.id.client_fragment_sign_tv)
    private TextView tv_sign;
    @ViewInject(R.id.client_fragment_intention_tv)
    private TextView tv_intention;
    @ViewInject(R.id.client_fragment_contact_tv)
    private TextView tv_contact;


    @ViewInject(R.id.client_fragment_all_line)
    private View line_all;
    @ViewInject(R.id.client_fragment_sign_line)
    private View line_sign;
    @ViewInject(R.id.client_fragment_intention_line)
    private View line_intention;
    @ViewInject(R.id.client_fragment_contact_line)
    private View line_contact;


    private List<Fragment> fragments = new ArrayList<Fragment>();

    private TabAdapter tabAdapter;
    private AllClientFragment allClientFragment;
    private SignClientFragment signClientFragment;
    private IntentionClientFragment intentionClientFragment;
    private ContactClientFragment contactClientFragment;

    private RefreshBroadcastReceiver refreshBroadcastReceiver;

    private boolean isFrist = true;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_fragment, container, false);
        return view;
    }

    @Override
    protected void initView() {
        btn_search.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.client);

        tabAdapter = new TabAdapter(getActivity().getSupportFragmentManager());

        allClientFragment = new AllClientFragment();
        signClientFragment = new SignClientFragment();
        intentionClientFragment = new IntentionClientFragment();
        contactClientFragment = new ContactClientFragment();
        fragments.add(allClientFragment);
        fragments.add(signClientFragment);
        fragments.add(intentionClientFragment);
        fragments.add(contactClientFragment);

        tabAdapter.setFragmentData(fragments);
        viewpager.setAdapter(tabAdapter);
        viewpager.addOnPageChangeListener(this);

        refreshBroadcastReceiver = new RefreshBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Common.ACTION);
        getActivity().registerReceiver(refreshBroadcastReceiver, filter);


    }

    @Override
    protected void logic() {


    }


    @Event(value = {R.id.client_fragment_all, R.id.client_fragment_sign, R.id.client_fragment_intention,
            R.id.client_fragment_contact, R.id.title_bar_right})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.client_fragment_all:
                changeTabState(0, false);
                break;
            case R.id.client_fragment_sign:
                changeTabState(1, false);
                break;
            case R.id.client_fragment_intention:
                changeTabState(2, false);
                break;
            case R.id.client_fragment_contact:
                changeTabState(3, false);
                break;
            case R.id.title_bar_right:
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchClientActivity.class);
                startActivity(intent);
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
        hideLine();
        TextView tv = null;
        switch (index) {
            case 0:
                tv = tv_all;
                line_all.setVisibility(View.VISIBLE);
                line_sign.setVisibility(View.INVISIBLE);
                line_intention.setVisibility(View.INVISIBLE);
                line_contact.setVisibility(View.INVISIBLE);
                allClientFragment.setGetFocus();
                signClientFragment.setCloseFocus();
                intentionClientFragment.setCloseFocus();
                contactClientFragment.setCloseFocus();
                break;
            case 1:
                tv = tv_sign;
                line_all.setVisibility(View.INVISIBLE);
                line_sign.setVisibility(View.VISIBLE);
                line_intention.setVisibility(View.INVISIBLE);
                line_contact.setVisibility(View.INVISIBLE);
                allClientFragment.setCloseFocus();
                signClientFragment.setGetFocus();
                intentionClientFragment.setCloseFocus();
                contactClientFragment.setCloseFocus();
                break;
            case 2:
                tv = tv_intention;
                line_all.setVisibility(View.INVISIBLE);
                line_sign.setVisibility(View.INVISIBLE);
                line_intention.setVisibility(View.VISIBLE);
                line_contact.setVisibility(View.INVISIBLE);
                allClientFragment.setCloseFocus();
                signClientFragment.setCloseFocus();
                intentionClientFragment.setGetFocus();
                contactClientFragment.setCloseFocus();
                break;
            case 3:
                tv = tv_contact;
                line_all.setVisibility(View.INVISIBLE);
                line_sign.setVisibility(View.INVISIBLE);
                line_intention.setVisibility(View.INVISIBLE);
                line_contact.setVisibility(View.VISIBLE);
                allClientFragment.setCloseFocus();
                signClientFragment.setCloseFocus();
                intentionClientFragment.setCloseFocus();
                contactClientFragment.setGetFocus();
                break;
        }
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.red_FC3D3D));
        if (!isPageScrolled) {
            viewpager.setCurrentItem(index);
        }
    }

    /**
     * 初始设置tab TextView颜色
     */
    private void hideLine() {
        tv_all.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        tv_sign.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        tv_intention.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        tv_contact.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
    }


    /**
     * 接受广播 刷新数据
     *
     * @author hbb
     */
    private class RefreshBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(Common.ACTION)) return;
            String type = intent.getStringExtra(Common.ACTION_REFRESH);
            if (TextUtils.equals("refresh", type)) {
                int status = intent.getIntExtra(Common.ACTION_FLAG, 0);
                if (AllClientFragment.instance != null) {
                    AllClientFragment.instance.refreshData();
                }
                if (status == 1) {//联系
                    if (ContactClientFragment.instance != null) {
                        ContactClientFragment.instance.refreshData();
                    }
                } else if (status == 2) {
                    if (IntentionClientFragment.instance != null) {
                        IntentionClientFragment.instance.refreshData();
                    }
                }
            }
//            getActivity().unregisterReceiver(this);// 不需要时注销
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(refreshBroadcastReceiver);// 不需要时注销
    }
}
