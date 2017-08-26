package djs.com.happysharebuy.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
import djs.com.happysharebuy.activity.LoginActivity;
import djs.com.happysharebuy.activity.main.MainActivity;
import djs.com.happysharebuy.entity.CommonBean;
import djs.com.happysharebuy.utils.CloseActivityClass;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.PrefUtil;
import djs.com.happysharebuy.utils.RequestService;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.utils.XmlUtils;

/**
 * Created by hbb on 2017/6/28.
 * 修改密码页面
 */
@ContentView(R.layout.modify_password_activity)
public class ModifyPasswordActivity extends BaseActivity {

    @ViewInject(R.id.title_bar_back)
    private LinearLayout back;
    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.modify_password_old_password)
    private EditText edt_old_password;
    @ViewInject(R.id.modify_password_new_password)
    private EditText edt_new_password;
    @ViewInject(R.id.modify_password_new_password_again)
    private EditText edt_new_password_again;
    @ViewInject(R.id.modify_password_submit)
    private Button btn_submit;


    private ModifyPasswordActivity instance = null;

    private String str_old_password, str_new_password, str_new_password_again;

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        instance = this;
        back.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.modify_password);
    }

    @Override
    protected void logic() {
        EditTextListener(edt_old_password);
        EditTextListener(edt_new_password);
        EditTextListener(edt_new_password_again);

    }

    @Event(value = {R.id.title_bar_back, R.id.modify_password_submit})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_back:
                ModifyPasswordActivity.this.finish();
                break;
            case R.id.modify_password_submit:
                if (validatePassword()) {
                    requestModifyPassword();
                }
                break;
        }
    }

    /**
     * 验证密码
     *
     * @return
     */
    private boolean validatePassword() {
        str_old_password = edt_old_password.getText().toString().trim();
        str_new_password = edt_new_password.getText().toString().trim();
        str_new_password_again = edt_new_password_again.getText().toString().trim();
        if (!TextUtils.isEmpty(str_old_password) && !TextUtils.isEmpty(str_new_password)) {
            if (TextUtils.equals(str_new_password, str_new_password_again)) {
                return true;
            } else {
                ToastUtils.showToast(this, XmlUtils.getString(this, R.string.two_password_inconsistent));
            }
        } else {
            ToastUtils.showToast(this, XmlUtils.getString(this, R.string.password_not_null));
        }
        return false;
    }

    /**
     * edittext 输入变化监听
     *
     * @param edt
     */
    private void EditTextListener(EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString().trim();
                if (!TextUtils.isEmpty(str)) {
                    btn_submit.setBackgroundResource(R.drawable.fillet_rectangle_red_solid);
                } else {
                    btn_submit.setBackgroundResource(R.drawable.fillet_rectangle_gray_solid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 请求修改密码
     */
    private void requestModifyPassword() {
        RequestParams params = CustomRequestParams.getParams(this);
        params.addBodyParameter("act", "modpwd");
        params.addBodyParameter("ypwd", str_old_password);
        params.addBodyParameter("npwd", str_new_password);
        RequestService.post(instance, params, new RequestService.CustomCallback() {
            @Override
            public void onSuccess(String result) {
                requestSuccess(result);
            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 密码修改请求成功处理
     *
     * @param result
     */
    private void requestSuccess(String result) {
        Gson gson = HSBApplication.getGson();
        CommonBean commonBean = gson.fromJson(result, CommonBean.class);
        if (commonBean != null) {
            int status = commonBean.getStatus();
            ToastUtils.showToast(instance, commonBean.getMsg());
            if (status == 1) {
                PrefUtil.savePref(getActivity(), PrefUtil.USER_ID, 0);
                PrefUtil.savePref(getActivity(), PrefUtil.USER_PASSWORD, "");
                Intent intent = new Intent();
                intent.setClass(instance, LoginActivity.class);
                startActivity(intent);
                CloseActivityClass.exitClient(getActivity());
                if (MainActivity.instance != null) {
                    MainActivity.instance.finish();
                }
            }
        }
    }

}
