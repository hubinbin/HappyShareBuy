package djs.com.happysharebuy.activity.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseFragment;
import djs.com.happysharebuy.activity.ranking.ContactFragment;
import djs.com.happysharebuy.activity.ranking.CustomerFragment;
import djs.com.happysharebuy.adapter.TabAdapter;

/**
 * Created by bin on 2017/7/14.
 * 排行版
 */

public class RankedFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.ranked_edition_viewpager)
    private ViewPager viewpager;

    @ViewInject(R.id.ranked_edition_contact_tv)
    private TextView tv_contact;
    @ViewInject(R.id.ranked_edition_customer_tv)
    private TextView tv_customer;

    @ViewInject(R.id.ranked_edition_contact_line)
    private View line_contact;
    @ViewInject(R.id.ranked_edition_customer_line)
    private View line_customer;

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private TabAdapter tabAdapter;


    private ContactFragment contactFragment;
    private CustomerFragment customerFragment;


    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ranked_edition_fragment, container, false);
        return view;
    }

    @Override
    protected void initView() {
        tv_title.setText(R.string.ranking_list);

        tabAdapter = new TabAdapter(getActivity().getSupportFragmentManager());
        contactFragment = new ContactFragment();
        customerFragment = new CustomerFragment();
        fragments.add(contactFragment);
        fragments.add(customerFragment);


        tabAdapter.setFragmentData(fragments);
        viewpager.setAdapter(tabAdapter);
        viewpager.addOnPageChangeListener(this);
    }

    @Override
    protected void logic() {

    }


    @Event(value = {R.id.ranked_edition_contact, R.id.ranked_edition_customer})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.ranked_edition_contact:
                changeTabState(0, false);
                break;
            case R.id.ranked_edition_customer:
                changeTabState(1, false);
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
                tv = tv_contact;
                line_contact.setVisibility(View.VISIBLE);
                line_customer.setVisibility(View.INVISIBLE);
                contactFragment.setGetFocus();
                customerFragment.setCloseFocus();
                break;
            case 1:
                tv = tv_customer;
                line_contact.setVisibility(View.INVISIBLE);
                line_customer.setVisibility(View.VISIBLE);
                contactFragment.setCloseFocus();
                customerFragment.setGetFocus();
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
        tv_contact.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        tv_customer.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
    }

}
