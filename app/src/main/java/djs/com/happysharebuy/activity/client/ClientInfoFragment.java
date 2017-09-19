package djs.com.happysharebuy.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import djs.com.happysharebuy.activity.BaseFragment;
import djs.com.happysharebuy.entity.ClientBean;
import djs.com.happysharebuy.entity.IndustryBean;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.DateUtil;
import djs.com.happysharebuy.utils.ErrorDispose;
import djs.com.happysharebuy.utils.ImageOption;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.utils.XmlUtils;
import djs.com.happysharebuy.view.CustomDialog;

/**
 * Created by bin on 2017/7/5.
 * 客户详情页面
 */
public class ClientInfoFragment extends BaseFragment {


//    @ViewInject(R.id.title_bar_back)
//    private LinearLayout back;
//    @ViewInject(R.id.title_bar_name)
//    private TextView tv_title;
    @ViewInject(R.id.client_details_lin)
    private LinearLayout client_details_lin;
//    @ViewInject(R.id.title_bar_right_img)
//    private ImageView right_img;

    @ViewInject(R.id.show_company_name)
    private TextView tv_company_name;
    @ViewInject(R.id.show_company_phone)
    private TextView tv_company_phone;
    @ViewInject(R.id.show_company_address)
    private TextView tv_company_address;
    @ViewInject(R.id.show_company_contacts)
    private TextView tv_company_contacts;
    @ViewInject(R.id.show_company_time)
    private TextView tv_time;
    @ViewInject(R.id.show_company_image)
    private ImageView img_company_avatar;
    @ViewInject(R.id.show_company_logo)
    private ImageView img_company_logo;
    @ViewInject(R.id.show_company_busy)
    private ImageView img_busy;
    @ViewInject(R.id.show_busy_license_number)
    private TextView tv_busy_no;
    @ViewInject(R.id.show_trading_area)
    private TextView tv_trading_area;
    @ViewInject(R.id.show_wechat)
    private TextView tv_wechat;
    @ViewInject(R.id.show_remarks)
    private TextView tv_remarks;
    @ViewInject(R.id.show_mailbox)
    private TextView tv_mailbox;
    @ViewInject(R.id.show_id_card)
    private TextView tv_id_card;
    @ViewInject(R.id.show_intention)
    private TextView tv_intention;
    @ViewInject(R.id.show_price)
    private TextView tv_price;

    private int Id;
    private int cid;
    private String str_industry;
    private int industry_id;

    private ClientBean clientBean;


    private static final int MODIFY_COMPANY_INFO = 2;


    private List<IndustryBean> industryBean;

    public static ClientInfoFragment instance;
    private int status;


    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.client_info_fragment, container, false);
        return view;
    }

    @Override
    protected void initView() {
//        back.setVisibility(View.VISIBLE);
//        right_img.setVisibility(View.VISIBLE);
//        right_img.setImageResource(R.mipmap.icon_edit);
//        Intent intent = getIntent();
//        clientBean = (ClientBean) intent.getSerializableExtra("client");
//        tv_title.setText(clientBean.getShanghu());
        clientBean  = ClientDetailsActivity.instance.clientBean;
        Id = clientBean.getId();
        industry_id = clientBean.getCid1();
    }

    @Override
    protected void logic() {
        showCompanyInfo(clientBean);
        requestIndustry();
    }

    /**
     * 显示公司信息
     */
    private void showCompanyInfo(ClientBean clientBean) {
        if (clientBean != null) {
            tv_company_name.setText(clientBean.getShanghu());
            tv_company_phone.setText(clientBean.getTel());
            tv_company_address.setText(clientBean.getProvince() + clientBean.getCity() + clientBean.getDist() + clientBean.getAddr());
            tv_company_contacts.setText(clientBean.getLinkName());
            tv_remarks.setText(clientBean.getTxt1());
            String addtime = clientBean.getAddtime();
            tv_time.setText(DateUtil.disposeDate(addtime));
//            x.image().bind(img_company_avatar, clientBean.getImgurl(), ImageOption.nomalImageOptions());
            cid = clientBean.getCid1();


            x.image().bind(img_company_logo, clientBean.getImgurl(), ImageOption.nomalImageOptions());

//            x.image().bind(img_busy, clientBean.getLicensePic(), ImageOption.nomalImageOptions());

            tv_busy_no.setText(clientBean.getLicenseNo());

            String weixin = clientBean.getWeixin();
//            String busarer = clientBean.getBusarer();
//            String cid2 = clientBean.getCid2();
            String email = clientBean.getEmail();
            String idcard = clientBean.getIdcard();

            tv_wechat.setText(weixin);
//            tv_trading_area.setText(busarer);
//            tv_sub_industry.setText(cid2);
            tv_mailbox.setText(email);
            tv_id_card.setText(idcard);
            tv_price.setText(clientBean.getRmb()+"元");


            status = clientBean.getStatus();
            switch (status) {
                case 1:
                    tv_intention.setText("联系");
                    break;
                case 2:
                    tv_intention.setText("有意向");
                    break;
                case 3:
                    tv_intention.setText("签约");
                    break;
            }


        }
    }

    @Event(value = {R.id.title_bar_back, R.id.title_bar_right_img, R.id.show_qrcode, R.id.show_service_record
            , R.id.show_company_image, R.id.show_company_logo, R.id.show_company_busy,R.id.company_picture_info})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_company_image:
                if (clientBean != null) {
                    String imgurl = clientBean.getImgurl();
                    if (!TextUtils.isEmpty(imgurl)) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), LargerPreviewActivity.class);
                        intent.putExtra("url", imgurl);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(getActivity(), "无预览图片");
                    }
                }
                break;
            case R.id.show_company_logo:
                if (clientBean != null) {
                    String imgurl = clientBean.getLogo();
                    if (!TextUtils.isEmpty(imgurl)) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), LargerPreviewActivity.class);
                        intent.putExtra("url", imgurl);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(getActivity(), "无预览图片");
                    }
                }
                break;
            case R.id.show_company_busy:
                if (clientBean != null) {
                    String imgurl = clientBean.getLicensePic();
                    if (!TextUtils.isEmpty(imgurl)) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), LargerPreviewActivity.class);
                        intent.putExtra("url", imgurl);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(getActivity(), "无预览图片");
                    }
                }
                break;

            case R.id.show_qrcode:
                CustomDialog customDialog = new CustomDialog(getActivity(), CustomDialog.TYPE_QRCODE);
                customDialog.show();
                break;
            case R.id.show_service_record:
                Intent intent = new Intent();
                intent.setClass(getActivity(), RecordActivity.class);
                intent.putExtra("id", Id);
                startActivity(intent);
                break;
        }
    }

    /**跳转修改信息*/
    public void toModifyInfo(){
        if (clientBean != null && status != 3) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), ModifyInfoActivity.class);
            intent.putExtra("client", clientBean);
            intent.putExtra("industry", str_industry);
            intent.putExtra("industry_id", industry_id);
            startActivityForResult(intent, MODIFY_COMPANY_INFO);
        } else {
            ToastUtils.showToast(getActivity(), "签约用户无法修改信息");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == MODIFY_COMPANY_INFO) {
                ClientBean clientBean = (ClientBean) data.getSerializableExtra("client");
                showCompanyInfo(clientBean);
                cid = clientBean.getCid1();
                getIndustry();
            }
        }
    }


    /**
     * 请求行业列表
     */
    private void requestIndustry() {
        RequestParams params = CustomRequestParams.getParams();
        params.addBodyParameter("act", "gethangye");

        x.http().get(params, new Callback.CommonCallback<JSONArray>() {

            @Override
            public void onSuccess(JSONArray jsonArray) {
                String str = jsonArray.toString();
                LogUtil.i(str);
                if (!TextUtils.isEmpty(str)) {
                    requestIndustrySuccess(jsonArray.toString());
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
     * 行业列表请求成功后处理
     *
     * @param result
     */
    private void requestIndustrySuccess(String result) {
        Gson gson = HSBApplication.getGson();
        industryBean = gson.fromJson(result, new TypeToken<LinkedList<IndustryBean>>() {
        }.getType());
        getIndustry();
    }

    /**
     * 获取行业名字
     */
    private void getIndustry() {
        if (industryBean != null) {
            for (int i = 0; i < industryBean.size(); i++) {
                int id = industryBean.get(i).getId();
                if (id == cid) {
                    str_industry = industryBean.get(i).getItemName();
                    String busarer = clientBean.getBusarer();
                    String cid2 = clientBean.getCid2();

                    String info = "";

                    if (!TextUtils.isEmpty(busarer)) {
                        info = info + busarer + "   ";
                    }
                    if (!TextUtils.isEmpty(str_industry)) {
                        info = info + str_industry + "|";
                    }

                    if (!TextUtils.isEmpty(cid2)) {
                        info = info + cid2;
                    }
                    tv_trading_area.setText(info);
                    return;
                }
            }
        }
    }



    /**
     * 获取本页面的焦点
     */
    public void setGetFocus() {
        if (client_details_lin != null) {
            client_details_lin.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);//在根之前获取焦点
        }
    }

    /**
     * 关闭本页面的焦点
     */
    public void setCloseFocus() {
        if (client_details_lin != null) {
            client_details_lin.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }
    }

}
