package djs.com.happysharebuy.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.LinkedList;
import java.util.List;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseActivity;
import djs.com.happysharebuy.adapter.RecordAdapter;
import djs.com.happysharebuy.entity.BusinessBean;
import djs.com.happysharebuy.entity.CommonBean;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.RequestService;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.view.CustomWorkDialog;

/**
 * Created by bin on 2017/8/26.
 * 业务记录页面
 */
@ContentView(R.layout.record_activity)
public class RecordActivity extends BaseActivity implements AdapterView.OnItemLongClickListener, CustomWorkDialog.OnClickListener {

    @ViewInject(R.id.title_bar_name)
    private TextView title_name;
    @ViewInject(R.id.title_bar_right_img)
    private ImageView right_img;
    @ViewInject(R.id.record_list)
    private ListView record_list;
    @ViewInject(R.id.title_bar_back)
    private LinearLayout back;
    @ViewInject(R.id.record_no_msg)
    private TextView record_no_msg;


    private List<BusinessBean> businesses;


    private RecordAdapter recordAdapter;

    private static final int ADD_BUSINESS_RECORD = 1;

    private int businessId;
    private int position;

    private int Id;

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        title_name.setText(R.string.record);
        back.setVisibility(View.VISIBLE);
        right_img.setVisibility(View.VISIBLE);
        right_img.setImageResource(R.mipmap.icon_plus);


        recordAdapter = new RecordAdapter(this);
        record_list.setAdapter(recordAdapter);
        record_list.setOnItemLongClickListener(this);

        CustomWorkDialog.setOnClickListener(this);

    }

    @Override
    protected void logic() {
        Intent intent = getIntent();
        Id = intent.getIntExtra("id", 0);
        startProgressBar(this,"正在加载...");
        requestRecoed();
    }


    @Event(value = {R.id.title_bar_back,R.id.title_bar_right_img})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                RecordActivity.this.finish();
                break;
            case R.id.title_bar_right_img:
                Intent intent = new Intent();
                intent.setClass(RecordActivity.this, AddRecordActivity.class);
                intent.putExtra("id", Id);
                startActivityForResult(intent, ADD_BUSINESS_RECORD);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_BUSINESS_RECORD) {
                requestRecoed();
            }
        }
    }

    /**
     * 请求业务记录列表
     */
    private void requestRecoed() {
        RequestParams params = CustomRequestParams.getParams();
        params.addBodyParameter("Act", "getlxlog");
        params.addBodyParameter("compid", Id + "");

        RequestService.getArray(RecordActivity.this, params, new RequestService.CustomCallback() {
            @Override
            public void onSuccess(String result) {
                requestRecoedSuccess(result);
            }

            @Override
            public void onFinished() {
            dismissProgressBar();
            }
        });
    }

    /**
     * 业务列表请求成功处理
     *
     * @param result
     */
    private void requestRecoedSuccess(String result) {
        Gson gson = HSBApplication.getGson();
        businesses = gson.fromJson(result, new TypeToken<LinkedList<BusinessBean>>() {
        }.getType());
        if (businesses != null && businesses.size() > 0) {
            record_list.setVisibility(View.VISIBLE);
            record_no_msg.setVisibility(View.GONE);
            recordAdapter.setData(businesses);
        } else {
            record_list.setVisibility(View.GONE);
            record_no_msg.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        BusinessBean businessBean = businesses.get(i);
        businessId = businessBean.getId();
        position = i;
        CustomWorkDialog.showDialog(getActivity(), "", CustomWorkDialog.DELETE_RECORDS);
        return true;
    }


    /**
     * 请求删除业务记录列表
     */
    private void requestDelRecoed(int recoedsId) {
        RequestParams params = CustomRequestParams.getParams(RecordActivity.this);
        params.addBodyParameter("act", "dellxlog");
        params.addBodyParameter("keyid", recoedsId + "");

        RequestService.post(RecordActivity.this, params, new RequestService.CustomCallback() {
            @Override
            public void onSuccess(String result) {
                requestDelRecoedSuccess(result);
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 删除业务记录成功处理
     */
    private void requestDelRecoedSuccess(String result) {
        Gson gson = HSBApplication.getGson();
        CommonBean commonBean = gson.fromJson(result, CommonBean.class);
        if (commonBean != null) {
            ToastUtils.showToast(RecordActivity.this, commonBean.getMsg());
            if (commonBean.getStatus() == 1) {
                if (position >= 0 && businesses.size() > position) {
                    businesses.remove(position);
                    recordAdapter.notifyDataSetChanged();
                }
                if (businesses.size() == 0) {
                    record_list.setVisibility(View.GONE);
                    record_no_msg.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onClick(int type) {
        requestDelRecoed(businessId);
    }


}
