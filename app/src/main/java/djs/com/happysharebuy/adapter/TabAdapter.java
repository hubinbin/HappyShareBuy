package djs.com.happysharebuy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbb on 2017/6/28.
 * tab 的Adapter
 */

public class TabAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments = new ArrayList<Fragment>();

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * 添加fragment数据
     *
     * @param fragments
     */
    public void setFragmentData(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int index) {
        Fragment frag = fragments.get(index);
        return frag;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
