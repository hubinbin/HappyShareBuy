package djs.com.happysharebuy.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseActivity;
import djs.com.happysharebuy.entity.CommonBean;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.RequestService;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.utils.XmlUtils;

/**
 * Created by bin on 2017/7/5.
 * 增加公司业务
 */
@ContentView(R.layout.add_record_activity)
public class AddRecordActivity extends BaseActivity {

    @ViewInject(R.id.title_bar_back)
    private LinearLayout back;
    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.input_fragment_remarks)
    private EditText edt_remarks;


    private int id;

    private String str_remarks;

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        back.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.add_record);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);


    }

    @Override
    protected void logic() {

    }

    @Event(value = {R.id.title_bar_back, R.id.input_fragment_submit})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_back:
                AddRecordActivity.this.finish();
                break;
            case R.id.input_fragment_submit:
                if (validateInfo()) {
                    requestAddRemarks();
                }
                break;
        }
    }

    /**
     * 验证输入信息
     */
    private boolean validateInfo() {
        str_remarks = edt_remarks.getText().toString().trim();
        if (!TextUtils.isEmpty(str_remarks)) {
            return true;
        } else {
            ToastUtils.showToast(this, XmlUtils.getString(this, R.string.input_service_record));
        }
        return false;
    }

    /**
     * 请求增加业务记录
     */
    private void requestAddRemarks() {
        RequestParams params = CustomRequestParams.getParams(this);
        params.addBodyParameter("act", "addlxlog");
        params.addBodyParameter("compid", id + "");
        params.addBodyParameter("txt", str_remarks);

        RequestService.post(AddRecordActivity.this, params, new RequestService.CustomCallback() {
            @Override
            public void onSuccess(String result) {
                requestAddRemarks(result);
            }

            @Override
            public void onFinished() {

            }
        });

    }


    /**
     * 业务记录添加成功处理
     *
     * @param result
     */
    private void requestAddRemarks(String result) {
        Gson gson = HSBApplication.getGson();
        CommonBean commonBean = gson.fromJson(result, CommonBean.class);
        if (commonBean != null) {
            ToastUtils.showToast(AddRecordActivity.this, commonBean.getMsg());
            if (commonBean.getStatus() == 1) {
                setResult(RESULT_OK);
                AddRecordActivity.this.finish();
            }
        }

    }

}
