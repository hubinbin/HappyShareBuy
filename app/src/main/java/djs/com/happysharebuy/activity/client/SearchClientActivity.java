package djs.com.happysharebuy.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseActivity;
import djs.com.happysharebuy.adapter.ClientAdapter;
import djs.com.happysharebuy.entity.ClientBean;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.ErrorDispose;
import djs.com.happysharebuy.utils.KeyBoardUtils;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.utils.XmlUtils;

/**
 * Created by bin on 2017/7/5.
 * 搜索页面
 */
@ContentView(R.layout.search_client_activity)
public class SearchClientActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @ViewInject(R.id.search_client_edt)
    private EditText edt_search;
    @ViewInject(R.id.search_client_list)
    private ListView client_list;

    private ClientAdapter clientAdapter;

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        clientAdapter = new ClientAdapter(this);
        client_list.setAdapter(clientAdapter);
        client_list.setOnItemClickListener(this);
    }

    @Override
    protected void logic() {

        edt_search.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        KeyBoardUtils.hideKeyboard(SearchClientActivity.this);
                        String condition = edt_search.getText().toString().trim();
                        requestClientList(condition);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    @Event(value = {R.id.title_bar_back, R.id.title_bar_cancel})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_back:
                SearchClientActivity.this.finish();
                break;
            case R.id.title_bar_cancel:
                edt_search.setText("");
                break;
        }
    }


    /**
     * 请求客户列表
     */
    private void requestClientList(String condition) {
        RequestParams params = CustomRequestParams.getParams(getActivity());
        params.addBodyParameter("act", "companylist");
        params.addBodyParameter("p", "1");
        params.addBodyParameter("status", "0");
        params.addBodyParameter("keystr", condition);

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
            clientAdapter.setData(clients);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ClientBean clientBean = (ClientBean)client_list.getItemAtPosition(i);
        Intent intent = new Intent();
        intent.setClass(getActivity(),ClientDetailsActivity.class);
        intent.putExtra("client",clientBean);
        startActivity(intent);
    }
}
