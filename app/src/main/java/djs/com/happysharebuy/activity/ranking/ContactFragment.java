package djs.com.happysharebuy.activity.ranking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseFragment;
import djs.com.happysharebuy.adapter.RankingAdapter;
import djs.com.happysharebuy.entity.UserBean;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.RequestService;
import djs.com.happysharebuy.view.refresh.PullListView;
import djs.com.happysharebuy.view.refresh.PullToRefreshLayout;

/**
 * Created by bin on 2017/7/14.
 * 当前联系
 */

public class ContactFragment extends BaseFragment {

    @ViewInject(R.id.contact_fragment_lin)
    private LinearLayout contact_fragment_lin;

    @ViewInject(R.id.contact_fragment_list)
    private ListView contact_list;

    private RankingAdapter rankingAdapter;


    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        return view;
    }

    @Override
    protected void initView() {
        rankingAdapter = new RankingAdapter(getActivity());
        contact_list.setAdapter(rankingAdapter);

    }


    @Override
    protected void logic() {
        requestRanking();
    }


    /**
     * 获取本页面的焦点
     */
    public void setGetFocus() {
        if (contact_fragment_lin != null) {
            contact_fragment_lin.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);//在根之前获取焦点
        }
    }

    /**
     * 关闭本页面的焦点
     */
    public void setCloseFocus() {
        if (contact_fragment_lin != null) {
            contact_fragment_lin.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }
    }


    /**
     * 请求排行榜数据
     */
    private void requestRanking() {
        RequestParams params = CustomRequestParams.getParams();
        params.addBodyParameter("Act", "todaytop");
        RequestService.getArray(getActivity(), params, new RequestService.CustomCallback() {
            @Override
            public void onSuccess(String result) {
                requestRankingSuccess(result);
            }

            @Override
            public void onFinished() {

            }
        });


    }

    /**
     * 接口请求成功处理
     *
     * @param result
     */
    private void requestRankingSuccess(String result) {
        Gson gson = HSBApplication.getGson();
        List<UserBean> users = gson.fromJson(result,new TypeToken<LinkedList<UserBean>>(){}.getType());
        if (users != null){
            rankingAdapter.setData(users);
        }
    }

}
