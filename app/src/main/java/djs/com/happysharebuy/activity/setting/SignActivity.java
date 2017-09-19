package djs.com.happysharebuy.activity.setting;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
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
import djs.com.happysharebuy.entity.CommonBean;
import djs.com.happysharebuy.entity.SignDayBean;
import djs.com.happysharebuy.utils.DateUtil;
import djs.com.happysharebuy.utils.ErrorDispose;
import djs.com.happysharebuy.utils.HttpUrlUtils;
import djs.com.happysharebuy.utils.LocationUtils;
import djs.com.happysharebuy.utils.PrefUtil;
import djs.com.happysharebuy.utils.ToastUtils;

/**
 * Created by bin on 2017/9/14.
 * 打卡签到
 */
@ContentView(R.layout.sign_activity)
public class SignActivity extends BaseActivity {

    @ViewInject(R.id.title_bar_back)
    private LinearLayout back;
    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.sign_activity_date)
    private TextView tv_date;
    @ViewInject(R.id.sign_activity_current_sign)
    private TextView tv_current_sign;
    @ViewInject(R.id.sign_activity_year)
    private EditText edt_year;
    @ViewInject(R.id.sign_activity_month)
    private EditText edt_month;
    @ViewInject(R.id.sign_activity_query_sign)
    private TextView tv_query_sign;


    private String str_year, str_month;
    private int int_year, int_month;

    private int perid;
    private int currentMonthDay;//当月天数
    private int punchDays;//打卡天数


    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        back.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.punch_clock);


    }

    @Override
    protected void logic() {
        currentMonthDay = DateUtil.getCurrentMonthLastDay();
        perid = PrefUtil.getIntPref(SignActivity.this, PrefUtil.USER_ID, 0);
        String date = DateUtil.currentDate();
        tv_date.setText(date);

        if (!TextUtils.isEmpty(date)) {
            String queryDate = date.substring(0, date.length() - 3);
            queryRecord(0, queryDate);
        }

    }

    @Event(value = {R.id.title_bar_back, R.id.sign_activity_query, R.id.sign_activity_punch})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                SignActivity.this.finish();
                break;
            case R.id.sign_activity_query:
                if (validate()) {
                    queryRecord(1, str_year + "-" + str_month);
                }
                break;
            case R.id.sign_activity_punch:
                requestSign();
                break;
        }
    }

    /**
     * 验证输入年月
     *
     * @return
     */
    private boolean validate() {
        str_year = edt_year.getText().toString().trim();
        str_month = edt_month.getText().toString().trim();
        if (!TextUtils.isEmpty(str_year)) {
            int_year = Integer.parseInt(str_year);
            if (!TextUtils.isEmpty(str_month)) {
                int_month = Integer.parseInt(str_month);
                if (int_month > 0 && int_month <= 12){
                    return true;
                }else {
                    ToastUtils.showToast(this, "请输入正确的月份");
                }
            } else {
                ToastUtils.showToast(this, "请输入月份");
            }
        } else {
            ToastUtils.showToast(this, "请输入年份");
        }
        return false;
    }

    /**
     * 打卡签到
     */
    private void requestSign() {
        startProgressBar(SignActivity.this, "正在打卡请稍后...");
        LocationUtils LocationUtils = new LocationUtils(SignActivity.this);
        LocationUtils.startLocation();
        LocationUtils.setLocationClient(new LocationUtils.LocationClientOption() {
            @Override
            public void onLocationChanged(String city, String longitude, String latitude) {
                RequestParams params = new RequestParams(HttpUrlUtils.url);
                params.addBodyParameter("act", "checkin");
                params.addBodyParameter("perid", perid + "");
                params.addBodyParameter("longitude", longitude);
                params.addBodyParameter("latitude", latitude);
                x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        LogUtil.d(jsonObject.toString());
                        requestSignSuccess(jsonObject.toString());
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        ErrorDispose.xHttpError( throwable, b);
                    }

                    @Override
                    public void onCancelled(CancelledException e) {

                    }

                    @Override
                    public void onFinished() {
                        dismissProgressBar();
                    }
                });
            }
        });
    }

    /**
     * 打卡签到成功
     *
     * @param result
     */
    private void requestSignSuccess(String result) {
        if (!TextUtils.isEmpty(result)) {
            Gson gson = HSBApplication.getGson();
            CommonBean commonBean = gson.fromJson(result, CommonBean.class);
            if (commonBean != null) {
                ToastUtils.showToast(SignActivity.this, commonBean.getMsg());
                int status = commonBean.getStatus();
                if (status == 1) {
                    showPunchDays(punchDays + 1);
                }
            }
        } else {
            ToastUtils.showToast(SignActivity.this, "打卡失败，请稍后重试");
        }
    }

    /**
     * 查询打卡记录
     * type 0单月
     *
     * @param date
     */
    private void queryRecord(final int type, String date) {
        RequestParams params = new RequestParams(HttpUrlUtils.url);
        params.addBodyParameter("act", "getcheckinlog");
        params.addBodyParameter("perid", perid + "");
        params.addBodyParameter("month", date);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(result);
                queryRecordSuccess(type, result);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                ErrorDispose.xHttpError( throwable, b);
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
     * 查询签到记录成功处理
     *
     * @param result
     */
    private void queryRecordSuccess(int type, String result) {
        if (!TextUtils.isEmpty(result)) {
            Gson gson = HSBApplication.getGson();
            List<SignDayBean> signDays = gson.fromJson(result, new TypeToken<LinkedList<SignDayBean>>() {
            }.getType());
            if (signDays != null) {
                if (type == 0) {
                    punchDays = signDays.size();
                    showPunchDays(punchDays);
                } else {
                    tv_query_sign.setVisibility(View.VISIBLE);
                    int daysByYearMonth = DateUtil.getDaysByYearMonth(int_year, int_month);
                    tv_query_sign.setText(str_year + "年" + str_month + "月出勤" + signDays.size() + "次(当月" + daysByYearMonth + "天)");
                }
            }
        } else {
            ToastUtils.showToast(SignActivity.this, "查询考勤记录失败");
        }
    }

    /**
     * 显示打卡天数
     *
     * @param punchDays
     */
    private void showPunchDays(int punchDays) {
        tv_current_sign.setText("你已经打卡" + punchDays + "天（当月" + currentMonthDay + "天）");
    }

}
