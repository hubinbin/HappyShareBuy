package djs.com.happysharebuy.activity.client;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


import java.io.File;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseActivity;
import djs.com.happysharebuy.activity.input.SelectAddressActivity;
import djs.com.happysharebuy.entity.ClientBean;
import djs.com.happysharebuy.entity.CommonBean;
import djs.com.happysharebuy.utils.Common;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.FileUtils;
import djs.com.happysharebuy.utils.ImageOption;
import djs.com.happysharebuy.utils.PermissionManage;
import djs.com.happysharebuy.utils.RequestService;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.utils.XmlUtils;
import djs.com.happysharebuy.view.SelectedPicPop;

/**
 * Created by bin on 2017/7/6.
 * 修改公司信息页面
 */

@ContentView(R.layout.modify_info_activity)
public class ModifyInfoActivity extends BaseActivity implements SelectedPicPop.OnClickListener {

    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.title_bar_back)
    private LinearLayout back;
    @ViewInject(R.id.modify_info_lin)
    private LinearLayout modify_info_lin;
    @ViewInject(R.id.input_fragment_company_name)
    private EditText edt_company_name;
    @ViewInject(R.id.input_fragment_company_phone)
    private EditText edt_company_phone;
    @ViewInject(R.id.input_fragment_company_address)
    private EditText edt_company_address;
    @ViewInject(R.id.input_fragment_company_contacts)
    private EditText edt_company_contacts;
    @ViewInject(R.id.input_fragment_company_business_license_number)
    private EditText edt_busy_number;
    @ViewInject(R.id.input_fragment_trading_area)
    private EditText edt_trading_area;
    @ViewInject(R.id.input_fragment_wechat)
    private EditText edt_wechat;
    @ViewInject(R.id.input_fragment_sub_industry)
    private EditText edt_sub_industry;
    @ViewInject(R.id.input_fragment_mailbox)
    private EditText edt_mailbox;
    @ViewInject(R.id.input_fragment_id_card)
    private EditText edt_id_card;


    @ViewInject(R.id.input_fragment_company_image)
    private ImageView img_company_image;
    @ViewInject(R.id.input_fragment_company_logo)
    private ImageView img_company_logo;
    @ViewInject(R.id.input_fragment_company_business_license)
    private ImageView img_business_license;

    @ViewInject(R.id.input_fragment_company_address_province)
    private TextView tv_province;
    @ViewInject(R.id.input_fragment_company_address_city)
    private TextView tv_city;
    @ViewInject(R.id.input_fragment_company_address_county)
    private TextView tv_county;
    @ViewInject(R.id.input_fragment_company_industry)
    private TextView tv_industry;

    @ViewInject(R.id.input_fragment_contact)
    private RadioButton rob_contact;
    @ViewInject(R.id.input_fragment_intention)
    private RadioButton rob_intention;
    @ViewInject(R.id.input_fragment_radio_group)
    private RadioGroup radio_group;

    @ViewInject(R.id.input_fragment_submit_info)
    private Button btn_submit;


    private ClientBean clientBean;
    private int status;

    private String str_province, str_city, str_county, str_industry;
    private String str_company_name, str_company_phone, str_company_address, str_company_contacts;
    private String str_mailbox = "", str_id_card = "";

    private int province_id, city_id, county_id, industry_id;

    private int id;
    private String str_busy_number;
    private String str_wechat;
    private String str_trading_area;
    private String str_sub_industry;


    private SelectedPicPop selectedPicPop;
    private String logoPath;
    private String busyPath;


    private File logoFile;
    private File busyFile;

    private int img_type;//图片类型，1:公司照片，2.公司logo 3.营业执照

    private static final int CAMERA = 1000;//相机

    private static final int REQUESR_ALBUM = 1002;//相册


    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        back.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.modify_company_info);
        btn_submit.setVisibility(View.VISIBLE);
    }

    @Override
    protected void logic() {
        Intent intent = getIntent();
        clientBean = (ClientBean) intent.getSerializableExtra("client");
        str_industry = intent.getStringExtra("industry");
        industry_id = intent.getIntExtra("industry_id", 0);
        if (clientBean != null) {
            id = clientBean.getId();
            edt_company_name.setText(clientBean.getShanghu());
            edt_company_phone.setText(clientBean.getTel());
            edt_company_address.setText(clientBean.getAddr());
            edt_company_contacts.setText(clientBean.getLinkName());
            edt_busy_number.setText(clientBean.getLicenseNo());
            edt_wechat.setText(clientBean.getWeixin());
            edt_trading_area.setText(clientBean.getBusarer());
            edt_sub_industry.setText(clientBean.getCid2());
            edt_mailbox.setText(clientBean.getEmail());
            edt_id_card.setText(clientBean.getIdcard());

            x.image().bind(img_company_image, clientBean.getImgurl(), ImageOption.nomalImageOptions());
            x.image().bind(img_company_logo, clientBean.getLogo(), ImageOption.nomalImageOptions());
            x.image().bind(img_business_license, clientBean.getLicensePic(), ImageOption.nomalImageOptions());

            str_province = clientBean.getProvince();
            str_city = clientBean.getCity();
            str_county = clientBean.getDist();
            tv_province.setText(str_province);
            tv_city.setText(str_city);
            tv_county.setText(str_county);
            tv_industry.setText(str_industry);

            status = clientBean.getStatus();

            switch (status) {
                case 1:
                    rob_contact.setChecked(true);
                    rob_intention.setChecked(false);
                    break;
                case 2:
                    rob_intention.setChecked(true);
                    rob_contact.setChecked(false);
                    break;
                case 3:
                    rob_contact.setChecked(false);
                    rob_intention.setChecked(false);
                    break;
            }

        }

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.input_fragment_intention) {
                    status = 2;
                } else if (checkedId == R.id.input_fragment_contact) {
                    status = 1;
                }
            }
        });

        selectedPicPop = new SelectedPicPop(getActivity(), SelectedPicPop.SELECTED_PIC);
        selectedPicPop.setOnClickListener(this);

    }

    @Event(value = {R.id.title_bar_back, R.id.input_fragment_company_address_province, R.id.input_fragment_company_address_city,
            R.id.input_fragment_company_address_county, R.id.input_fragment_company_industry, R.id.input_fragment_submit_info,
            R.id.input_fragment_company_logo, R.id.input_fragment_company_business_license})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_back:
                ModifyInfoActivity.this.finish();
                break;
            case R.id.input_fragment_company_address_province:
                toActivity(SelectAddressActivity.SELECT_PROVINCE);
                break;
            case R.id.input_fragment_company_address_city:
                if (!TextUtils.isEmpty(str_province)) {
                    if (province_id != 0) {
                        toActivity(SelectAddressActivity.SELECT_CITY);
                    } else {
                        ToastUtils.showToast(getActivity(), getXmlString(R.string.selected_province_again));
                    }
                } else {
                    ToastUtils.showToast(getActivity(), getXmlString(R.string.selected_province_again));
                }
                break;
            case R.id.input_fragment_company_address_county:
                if (!TextUtils.isEmpty(str_city)) {
                    if (city_id != 0) {
                        toActivity(SelectAddressActivity.SELECT_COUNTY);
                    } else {
                        ToastUtils.showToast(getActivity(), getXmlString(R.string.selected_city_again));
                    }
                } else {
                    ToastUtils.showToast(getActivity(), getXmlString(R.string.selected_city_again));
                }
                break;
            case R.id.input_fragment_company_industry:
                toActivity(SelectAddressActivity.SELECT_INDUSTRY);
                break;
            case R.id.input_fragment_submit_info:
                if (validateEdtParams()) {
                    startProgressBar(this, "正在提交...");
                    requestSubmitInfo();
                }
                break;
            case R.id.input_fragment_company_logo:
                img_type = 2;
                if (selectedPicPop != null) {
                    selectedPicPop.showAtLocation(modify_info_lin, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.input_fragment_company_business_license:
                img_type = 3;
                if (selectedPicPop != null) {
                    selectedPicPop.showAtLocation(modify_info_lin, Gravity.BOTTOM, 0, 0);
                }
                break;
        }
    }

    /**
     * 跳转Activity
     *
     * @param flag
     */
    private void toActivity(int flag) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SelectAddressActivity.class);
        intent.putExtra("flag", flag);
        if (flag == SelectAddressActivity.SELECT_CITY) {
            intent.putExtra("id", province_id);
        } else if (flag == SelectAddressActivity.SELECT_COUNTY) {
            intent.putExtra("id", city_id);
        } else {
            intent.putExtra("id", 0);
        }
        startActivityForResult(intent, flag);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == CAMERA) {
                switch (img_type) {
                    case 2:
                        if (logoFile.exists()) {
                            logoPath = logoFile.getAbsolutePath();
                            showCompanyImage(logoPath, img_company_logo);
                        }
                        break;
                    case 3:
                        if (busyFile.exists()) {
                            busyPath = busyFile.getAbsolutePath();
                            showCompanyImage(busyPath, img_business_license);
                        }
                        break;
                }

            } else if (requestCode == REQUESR_ALBUM) {
                Uri uri = data.getData();
                switch (img_type) {
                    case 2:
                        logoPath = FileUtils.getAbsolutePath(getActivity(), uri);
                        showCompanyImage(logoPath, img_company_logo);
                        break;
                    case 3:
                        busyPath = FileUtils.getAbsolutePath(getActivity(), uri);
                        showCompanyImage(busyPath, img_business_license);
                        break;
                }
            } else {
                String address = data.getStringExtra("address");
                if (!TextUtils.isEmpty(address)) {
                    if (requestCode == SelectAddressActivity.SELECT_PROVINCE) {
                        province_id = data.getIntExtra("id", 0);
                        str_province = address;
                        tv_province.setText(address);
                        str_city = "";
                        tv_city.setText(R.string.select_city);
                        str_county = "";
                        tv_county.setText(R.string.select_county);
                        edt_company_address.setText("");
                    } else if (requestCode == SelectAddressActivity.SELECT_CITY) {
                        city_id = data.getIntExtra("id", 0);
                        str_city = address;
                        tv_city.setText(address);
                        str_county = "";
                        tv_county.setText(R.string.select_county);
                        edt_company_address.setText("");
                    } else if (requestCode == SelectAddressActivity.SELECT_COUNTY) {
                        county_id = data.getIntExtra("id", 0);
                        str_county = address;
                        tv_county.setText(address);
                        edt_company_address.setText("");
                    } else if (requestCode == SelectAddressActivity.SELECT_INDUSTRY) {
                        industry_id = data.getIntExtra("id", 0);
                        str_industry = address;
                        tv_industry.setText(address);
                    }
                }
            }
        }
    }


    /**
     * 获取输入框输入的值
     */
    private boolean validateEdtParams() {
        str_company_name = edt_company_name.getText().toString().trim();
        str_company_phone = edt_company_phone.getText().toString().trim();
        str_company_address = edt_company_address.getText().toString().trim();
        str_company_contacts = edt_company_contacts.getText().toString().trim();

        str_busy_number = edt_busy_number.getText().toString().trim();
        str_wechat = edt_wechat.getText().toString().trim();
        str_trading_area = edt_trading_area.getText().toString().trim();
        str_sub_industry = edt_sub_industry.getText().toString().trim();
        str_mailbox = edt_mailbox.getText().toString().trim();
        str_id_card = edt_id_card.getText().toString().trim();

        if (validateString(str_company_name)) {//公司名称
            if (validateString(str_company_phone)) {//联系号码
                if (validateString(str_company_address)) {//联系地址
                    if (validateString(str_company_contacts)) {//联系人
                        if (validateString(str_province) && validateString(str_city) && validateString(str_county)) {//选择地址
                            if (validateString(str_industry)) {//行业
                                return true;
                            } else {
                                showToast(getXmlString(R.string.selected_industry));
                            }
                        } else {
                            showToast(getXmlString(R.string.selected_cities));
                        }
                    } else {
                        showToast(getXmlString(R.string.please_input_contacts));
                    }
                } else {
                    showToast(getXmlString(R.string.please_input_details_address));
                }
            } else {
                showToast(getXmlString(R.string.please_input_phone));
            }
        } else {
            showToast(getXmlString(R.string.please_input_company_name));
        }
        return false;
    }

    /**
     * 弹出Toast
     *
     * @param hint
     */
    private void showToast(String hint) {
        ToastUtils.showToast(getActivity(), hint);
    }

    /**
     * 验证字符串
     *
     * @param str
     * @return
     */
    private boolean validateString(String str) {
        if (!TextUtils.isEmpty(str)) {
            return true;
        }
        return false;
    }

    /**
     * 提交信息
     */
    private void requestSubmitInfo() {
        RequestParams params = CustomRequestParams.getParams(ModifyInfoActivity.this);
        params.addBodyParameter("act", "modcompany");
        params.addBodyParameter("keyid", id + "");
        params.addBodyParameter("shanghu", str_company_name);
        params.addBodyParameter("linkName", str_company_contacts);
        params.addBodyParameter("tel", str_company_phone);
        params.addBodyParameter("addr", str_company_address);
        params.addBodyParameter("province", str_province);
        params.addBodyParameter("city", str_city);
        params.addBodyParameter("dist", str_county);
        params.addBodyParameter("cid", industry_id + "");
        params.addBodyParameter("status", status + "");
        params.addBodyParameter("weixin", str_wechat);
        params.addBodyParameter("busarer", str_trading_area);
        params.addBodyParameter("licenseNo", str_busy_number);
        params.addBodyParameter("cid2", str_sub_industry);
        params.addBodyParameter("email", str_mailbox);
        params.addBodyParameter("idcard", str_id_card);

        if (!TextUtils.isEmpty(logoPath)) {
            String logoPicPath = FileUtils.savePic(logoPath, getActivity());
            params.addBodyParameter("logo", new File(logoPicPath), "multipart/form-data");
        }
        if (!TextUtils.isEmpty(busyPath)) {
            String busyPicPath = FileUtils.savePic(busyPath, getActivity());
            params.addBodyParameter("licensePic", new File(busyPicPath), "multipart/form-data");
        }


        RequestService.post(getActivity(), params, new RequestService.CustomCallback() {
            @Override
            public void onSuccess(String result) {
                requestSubmitInfoSuccess(result);
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
    private void requestSubmitInfoSuccess(String result) {
        Gson gson = HSBApplication.getGson();
        CommonBean commonBean = gson.fromJson(result, CommonBean.class);
        if (commonBean != null) {
            showToast(commonBean.getMsg());
            int status1 = commonBean.getStatus();
            if (status1 == 1) {
                if (clientBean != null) {

                    Intent intent2 = new Intent();
                    intent2.setAction(Common.ACTION);
                    intent2.putExtra(Common.ACTION_REFRESH, "refresh");
                    intent2.putExtra(Common.ACTION_FLAG, status);
                    getActivity().sendBroadcast(intent2);

//                    clientBean.setShanghu(str_company_name);
//                    clientBean.setTel(str_company_phone);
//                    clientBean.setAddr(str_company_address);
//                    clientBean.setProvince(str_province);
//                    clientBean.setCity(str_city);
//                    clientBean.setDist(str_county);
//                    clientBean.setLinkName(str_company_contacts);
//                    clientBean.setCid1(industry_id);
//                    clientBean.setStatus(status);
//                    Intent intent = new Intent();
//                    intent.putExtra("client", clientBean);
//                    setResult(RESULT_OK, intent);
                    if (ClientDetailsActivity.instance != null) {
                        ClientDetailsActivity.instance.finish();
                    }
                    ModifyInfoActivity.this.finish();
                }
            }
        }
    }

    /**
     * 显示公司图片
     *
     * @param url
     */
    private void showCompanyImage(String url, ImageView iv) {
        x.image().bind(iv, url, ImageOption.nomalImageOptions());
    }

    /**
     * 获取XML的String
     *
     * @param id
     * @return
     */
    private String getXmlString(int id) {
        return XmlUtils.getString(ModifyInfoActivity.this, id);
    }

    @Override
    public void onClick(boolean isCamera) {
        if (!isCamera) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUESR_ALBUM);
        } else {
            if (PermissionManage.getPermission(getActivity(), Manifest.permission.CAMERA)) {//判断是否开启照相机权限
                showCameraAction();
            }
        }
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        File file = null;
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                switch (img_type) {
                    case 2:
                        logoFile = FileUtils.createTmpFile(getActivity());
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(logoFile));
                        break;
                    case 3:
                        busyFile = FileUtils.createTmpFile(getActivity());
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(busyFile));
                        break;
                }
            } else {
                switch (img_type) {
                    case 2:
                        logoFile = FileUtils.createTmpFile(getActivity());
                        Uri uri2 = FileProvider.getUriForFile(getActivity().getApplication(),
                                getActivity().getApplication().getPackageName() + ".FileProvider",
                                logoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
                        break;
                    case 3:
                        busyFile = FileUtils.createTmpFile(getActivity());
                        Uri uri3 = FileProvider.getUriForFile(getActivity().getApplication(),
                                getActivity().getApplication().getPackageName() + ".FileProvider",
                                logoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri3);
                        break;
                }

            }
            startActivityForResult(cameraIntent, CAMERA);

        } else {
            ToastUtils.showToast(getActivity(), XmlUtils.getString(getActivity(), R.string.msg_no_camera));
        }
    }
}
