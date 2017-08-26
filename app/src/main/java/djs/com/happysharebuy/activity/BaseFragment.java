package djs.com.happysharebuy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import org.xutils.x;

import djs.com.happysharebuy.R;

/**
 * Created by hbb on 2017/6/28.
 */

public abstract class BaseFragment extends Fragment {

    private View mParentView;

    public ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mParentView == null) {
            mParentView = getContentView(inflater, container, savedInstanceState);
            x.view().inject(this, mParentView);
            initView();
        }
//        else {
//            ((ViewGroup) mParentView.getParent()).removeView(mParentView);
//        }
        return mParentView;
    }

    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState);


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logic();
    }


    protected abstract void initView();

    protected abstract void logic();


    /**
     * 在屏幕上添加一个转动的小菊花（传说中的Loading），默认为隐藏状态
     * 注意：务必保证此方法在setContentView()方法后调用，否则小菊花将会处于最底层，被屏幕其他View给覆盖
     *
     * @param context 需要添加菊花的Activity
     * @return {ProgressBar}    菊花对象
     */
    public void startProgressBar(Context context,String msg) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setMessage(msg);
//        dialog.setIcon(R.mipmap.ic_launcher);//
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.show();
    }

    /**
     * dismiss ProgressDialog
     */
    public void dismissProgressBar() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
