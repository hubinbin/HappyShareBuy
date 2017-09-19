package djs.com.happysharebuy.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseFragment;
import djs.com.happysharebuy.adapter.ClientAdapter;
import djs.com.happysharebuy.entity.ClientBean;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.ErrorDispose;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.utils.XmlUtils;
import djs.com.happysharebuy.view.refresh.PullListView;
import djs.com.happysharebuy.view.refresh.PullToRefreshLayout;

/**
 * Created by hbb on 2017/6/28.
 * 已签约客户
 */

public class SignClientFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.all_client_fragment_lin)
    private LinearLayout fragment_lin;
    @ViewInject(R.id.client_fragment_list)
    private PullListView client_list;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout pullToRefreshLayout;
    @ViewInject(R.id.client_fragment_no_client)
    private TextView tv_no_client;

    private int pageNo = 1;

    private ClientAdapter clientAdapter;

    private List<ClientBean> clientAll = new ArrayList<>();

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_client_fragment, container, false);
        return view;
    }

    @Override
    protected void initView() {
        clientAdapter = new ClientAdapter(getActivity());
        client_list.setAdapter(clientAdapter);
        pullToRefreshLayout.setOnRefreshListener(this);
        client_list.setOnItemClickListener(this);

        requestClientList();

    }

    @Override
    protected void logic() {

    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        pageNo = 1;
        requestClientList();
    }


    /**
     * 上啦加载数据
     */
    private void loadData() {
        pageNo++;
        requestClientList();
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refreshData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        loadData();
    }

    /**
     * 请求客户列表
     */
    private void requestClientList() {
        RequestParams params = CustomRequestParams.getParams(getActivity());
        params.addBodyParameter("act", "companylist");
        params.addBodyParameter("p", pageNo + "");
        params.addBodyParameter("status", "3");
        params.addBodyParameter("keystr", "");
        x.http().get(params, new Callback.CommonCallback<JSONArray>() {

            @Override
            public void onSuccess(JSONArray jsonArray) {
                String str = jsonArray.toString();
                LogUtil.i(str);
                if (!TextUtils.isEmpty(str)) {
                    requestClientListSuccess(str);
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
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

            }
        });


    }

    /**
     * 接口请求成功处理
     *
     * @param result
     */
    private void requestClientListSuccess(String result) {
        Gson gson = HSBApplication.getGson();
        List<ClientBean> clients = gson.fromJson(result, new TypeToken<LinkedList<ClientBean>>() {
        }.getType());
        if (clients != null) {
            if (pageNo == 1) {
                clientAll.clear();
                clientAll.addAll(clients);
            } else {
                if (clients.size() > 0) {
                    clientAll.addAll(clients);
                } else {
                    ToastUtils.showToast(getActivity(), XmlUtils.getString(getActivity(),R.string.no_data_more));
                }
            }
            clientAdapter.setData(clientAll);
            if (clientAll.size() == 0 && pageNo == 1) {
                tv_no_client.setVisibility(View.VISIBLE);
            } else {
                tv_no_client.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 获取本页面的焦点
     */
    public void setGetFocus() {
        if (fragment_lin != null) {
            fragment_lin.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);//在根之前获取焦点
        }
    }

    /**
     * 关闭本页面的焦点
     */
    public void setCloseFocus() {
        if (fragment_lin != null) {
            fragment_lin.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ClientBean clientBean = (ClientBean) client_list.getItemAtPosition(i);
        Intent intent = new Intent();
        intent.setClass(getActivity(), ClientDetailsActivity.class);
        intent.putExtra("client", clientBean);
        startActivity(intent);
    }
}
