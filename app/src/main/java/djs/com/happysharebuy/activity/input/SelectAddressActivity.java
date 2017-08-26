package djs.com.happysharebuy.activity.input;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
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
import djs.com.happysharebuy.adapter.CityAdapter;
import djs.com.happysharebuy.adapter.CountyAdapter;
import djs.com.happysharebuy.adapter.IndustryAdapter;
import djs.com.happysharebuy.adapter.ProvinceAdapter;
import djs.com.happysharebuy.entity.CityBean;
import djs.com.happysharebuy.entity.CountyBean;
import djs.com.happysharebuy.entity.IndustryBean;
import djs.com.happysharebuy.entity.ProvinceBean;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.RequestService;

/**
 * Created by bin on 2017/7/3.
 * 选择地址页面
 */
@ContentView(R.layout.select_address_activity)
public class SelectAddressActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @ViewInject(R.id.title_bar_back)
    private LinearLayout back;
    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.address_list)
    private ListView address_list;


    public static final int SELECT_PROVINCE = 1;//选择省份
    public static final int SELECT_CITY = 2;//选择城市
    public static final int SELECT_COUNTY = 3;//选择县区
    public static final int SELECT_INDUSTRY = 4;//选择行业


    private int flag = 0;
    private int id;

    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private CountyAdapter countyAdapter;
    private IndustryAdapter industryAdapter;


    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        back.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);
        id = intent.getIntExtra("id", 0);
        showTitle(flag);

        address_list.setOnItemClickListener(this);

        if (flag == SELECT_PROVINCE) {
            provinceAdapter = new ProvinceAdapter(this);
            address_list.setAdapter(provinceAdapter);
        } else if (flag == SELECT_CITY) {
            cityAdapter = new CityAdapter(this);
            address_list.setAdapter(cityAdapter);
        } else if (flag == SELECT_COUNTY) {
            countyAdapter = new CountyAdapter(this);
            address_list.setAdapter(countyAdapter);
        }else if (flag == SELECT_INDUSTRY){
            industryAdapter = new IndustryAdapter(this);
            address_list.setAdapter(industryAdapter);
        }
    }

    /**
     * 显示标题
     *
     * @param flag
     */
    private void showTitle(int flag) {
        switch (flag) {
            case SELECT_PROVINCE:
                tv_title.setText(R.string.select_province);
                break;
            case SELECT_CITY:
                tv_title.setText(R.string.select_city);
                break;
            case SELECT_COUNTY:
                tv_title.setText(R.string.select_county);
                break;
            case SELECT_INDUSTRY:
                tv_title.setText(R.string.select_industry);
                break;
        }

    }

    @Override
    protected void logic() {
        requestAddress();

    }


    @Event(value = {R.id.title_bar_back})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_back:
                SelectAddressActivity.this.finish();
                break;
        }
    }


    /**
     * 请求地址接口
     */
    private void requestAddress() {
        RequestService.getArray(SelectAddressActivity.this, setParams(), new RequestService.CustomCallback() {
            @Override
            public void onSuccess(String result) {
                requestAddressSuccess(result);
            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 设置参数
     *
     * @return
     */
    private RequestParams setParams() {
        RequestParams params = CustomRequestParams.getParams();
        switch (flag) {
            case SELECT_PROVINCE:
                params.addBodyParameter("act", "getprovince");
                break;
            case SELECT_CITY:
                params.addBodyParameter("act", "getcity");
                params.addBodyParameter("pid", id + "");
                break;
            case SELECT_COUNTY:
                params.addBodyParameter("act", "getdist");
                params.addBodyParameter("cityid", id + "");
                break;
            case SELECT_INDUSTRY:
                params.addBodyParameter("act", "gethangye");
                break;
        }
        return params;
    }


    /***
     * 接口请求成功处理
     * @param result
     */
    private void requestAddressSuccess(String result) {
        Gson gson = HSBApplication.getGson();
        if (flag == SELECT_PROVINCE) {
            List<ProvinceBean> provinces = gson.fromJson(result, new TypeToken<LinkedList<ProvinceBean>>() {
            }.getType());
            if (provinces != null && provinceAdapter != null) {
                provinceAdapter.setData(provinces);
            }
        } else if (flag == SELECT_CITY) {
            List<CityBean> citys = gson.fromJson(result, new TypeToken<LinkedList<CityBean>>() {
            }.getType());
            if (citys != null && cityAdapter != null) {
                cityAdapter.setData(citys);
            }
        } else if (flag == SELECT_COUNTY){
            List<CountyBean> countyBean = gson.fromJson(result, new TypeToken<LinkedList<CountyBean>>() {
            }.getType());
            if (countyBean != null && countyAdapter != null) {
                countyAdapter.setData(countyBean);
            }

        }else if (flag == SELECT_INDUSTRY){
            List<IndustryBean> industryBean = gson.fromJson(result, new TypeToken<LinkedList<IndustryBean>>() {
            }.getType());
            if (industryBean != null && industryAdapter != null) {
                industryAdapter.setData(industryBean);
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int id = 0;
        String address = "";
        if (flag == SELECT_PROVINCE) {
            ProvinceBean provinceBean = (ProvinceBean) address_list.getItemAtPosition(i);
            address = provinceBean.getProvinceName();
            id = provinceBean.getProvinceID();
        } else if (flag == SELECT_CITY) {
            CityBean cityBean = (CityBean) address_list.getItemAtPosition(i);
            address = cityBean.getCityName();
            id = cityBean.getCityID();
        }else if (flag == SELECT_COUNTY){
            CountyBean countyBean = (CountyBean) address_list.getItemAtPosition(i);
            address = countyBean.getDistrictName();
            id = countyBean.getDistrictID();
        }else if (flag == SELECT_INDUSTRY){
            IndustryBean industryBean = (IndustryBean) address_list.getItemAtPosition(i);
            address = industryBean.getItemName();
            id = industryBean.getId();
        }
        Intent intent = new Intent();
        intent.putExtra("address", address);
        intent.putExtra("id", id);
        setResult(RESULT_OK, intent);
        SelectAddressActivity.this.finish();
    }
}
