package djs.com.happysharebuy.activity.ranking;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseFragment;
import djs.com.happysharebuy.adapter.RankingAdapter;
import djs.com.happysharebuy.entity.UserBean;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.DateUtil;
import djs.com.happysharebuy.utils.ErrorDispose;
import djs.com.happysharebuy.utils.RequestService;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.utils.XmlUtils;

/**
 * Created by bin on 2017/7/14.
 * 成功客户
 */

public class CustomerFragment extends BaseFragment {

    @ViewInject(R.id.contact_fragment_lin)
    private LinearLayout contact_fragment_lin;

    @ViewInject(R.id.contact_fragment_list)
    private ListView contact_list;
    @ViewInject(R.id.contact_fragment_no_msg)
    private TextView no_msg;

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
     * 请求排行榜数据
     */
    private void requestRanking() {
        String currentDate = DateUtil.currentDate();
        if (TextUtils.isEmpty(currentDate)) {
            return;
        }
        String[] dates = currentDate.split("-");
        RequestParams params = CustomRequestParams.getParams();
        params.addBodyParameter("Act", "monthtopsuccess");
        params.addBodyParameter("year", dates[0]);
        params.addBodyParameter("month", Integer.parseInt(dates[1]) + "");
        x.http().get(params, new Callback.CommonCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                String str = jsonArray.toString();
                LogUtil.i(str);
                if (!TextUtils.isEmpty(str) && TextUtils.equals("[", str.trim().substring(0, 1))) {
                    requestRankingSuccess(str);
                } else {
                    ToastUtils.showToast(getActivity(), XmlUtils.getString(getActivity(), R.string.net_error));
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                ErrorDispose.xHttpError(throwable, b);
                ToastUtils.showToast(getActivity(), XmlUtils.getString(getActivity(), R.string.net_error));
            }

            @Override
            public void onCancelled(CancelledException e) {

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
        List<UserBean> users = gson.fromJson(result, new TypeToken<LinkedList<UserBean>>() {
        }.getType());
        if (users != null) {
            rankingAdapter.setData(users);
            if (users.size() == 0) {
                no_msg.setVisibility(View.VISIBLE);
                no_msg.setText("无成功客户");
                contact_list.setVisibility(View.GONE);
            } else {
                no_msg.setVisibility(View.GONE);
                contact_list.setVisibility(View.VISIBLE);
            }
        }
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
}
