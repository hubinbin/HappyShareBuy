package djs.com.happysharebuy.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.main.MainActivity;
import djs.com.happysharebuy.entity.UserInfoBean;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.HttpUrlUtils;
import djs.com.happysharebuy.utils.PermissionManage;
import djs.com.happysharebuy.utils.PrefUtil;
import djs.com.happysharebuy.utils.RequestService;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.utils.UserInfoUtils;
import djs.com.happysharebuy.utils.XmlUtils;

import static djs.com.happysharebuy.R.style.dialog;

/**
 * Created by hbb on 2017/6/28.
 * 登陆页面
 */
@ContentView(R.layout.login_activity)
public class LoginActivity extends Activity {

    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.login_submit)
    private Button submit;
    @ViewInject(R.id.login_input_account)
    private EditText edt_account;
    @ViewInject(R.id.login_input_password)
    private EditText edt_password;

    private String str_account, str_password;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        logic();
    }

    /**
     * 初始化设置
     */
    private void initView() {
        tv_title.setText(R.string.login);
    }

    /**
     * 逻辑判断
     */
    private void logic() {
        PermissionManage.getPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int id = PrefUtil.getIntPref(LoginActivity.this, PrefUtil.USER_ID, 0);
        if (id > 0) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
            return;
        }
        str_account = PrefUtil.getStringPref(LoginActivity.this, PrefUtil.USER_ACCOUNT);
        str_password = PrefUtil.getStringPref(LoginActivity.this, PrefUtil.USER_PASSWORD);

        edt_account.setText(str_account);
        edt_password.setText(str_password);

        if (!TextUtils.isEmpty(str_account) || !TextUtils.isEmpty(str_password)) {
            submit.setBackgroundResource(R.drawable.fillet_rectangle_red_solid);
        }

        edt_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (!TextUtils.isEmpty(str)) {
                    submit.setBackgroundResource(R.drawable.fillet_rectangle_red_solid);
                } else {
                    submit.setBackgroundResource(R.drawable.fillet_rectangle_gray_solid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (!TextUtils.isEmpty(str)) {
                    submit.setBackgroundResource(R.drawable.fillet_rectangle_red_solid);
                } else {
                    submit.setBackgroundResource(R.drawable.fillet_rectangle_gray_solid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Event(value = {R.id.login_submit})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_submit:
                if (validateAccountPassword()) {
                    requestLogin();
                }
                break;
        }
    }

    /**
     * 验证账号密码
     *
     * @return
     */
    private boolean validateAccountPassword() {
        str_account = edt_account.getText().toString().trim();
        str_password = edt_password.getText().toString().trim();
        if (!TextUtils.isEmpty(str_account)) {
            if (!TextUtils.isEmpty(str_password)) {
                return true;
            } else {
                ToastUtils.showToast(this, XmlUtils.getString(this, R.string.password_err));
            }
        } else {
            ToastUtils.showToast(this, XmlUtils.getString(this, R.string.account_err));
        }
        return false;
    }

    /**
     * 登入请求
     */
    private void requestLogin() {
        startProgressBar(this,"正在登录，请稍后...");
        RequestParams params = CustomRequestParams.getParams();
        params.addBodyParameter("act", "login");
        params.addBodyParameter("mobile", str_account);
        params.addBodyParameter("pwd", str_password);
        RequestService.post(LoginActivity.this, params, new RequestService.CustomCallback() {
            @Override
            public void onSuccess(String result) {
                requestSuccess(result);
            }

            @Override
            public void onFinished() {
                dismissProgressBar();
            }
        });
    }

    /**
     * 接口请求成功处理
     *
     * @param result
     */
    private void requestSuccess(String result) {
        Gson gson = HSBApplication.getGson();
        UserInfoBean userInfoBean = gson.fromJson(result, UserInfoBean.class);
        if (userInfoBean != null) {
            int status = userInfoBean.getStatus();
            if (status > 0) {
                PrefUtil.savePref(LoginActivity.this, PrefUtil.USER_ACCOUNT, str_account);
                PrefUtil.savePref(LoginActivity.this, PrefUtil.USER_PASSWORD, str_password);
                UserInfoUtils.saveUserInfo(LoginActivity.this, userInfoBean);
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            } else {
                ToastUtils.showToast(LoginActivity.this, userInfoBean.getMsg());
            }
        }

    }

    /**
     * 在屏幕上添加一个转动的小菊花（传说中的Loading），默认为隐藏状态
     * 注意：务必保证此方法在setContentView()方法后调用，否则小菊花将会处于最底层，被屏幕其他View给覆盖
     *
     * @param context 需要添加菊花的Activity
     * @return {ProgressBar}    菊花对象
     */
    public void startProgressBar(Context context, String msg) {
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
