package djs.com.happysharebuy.activity.main;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseFragment;
import djs.com.happysharebuy.activity.input.SelectAddressActivity;
import djs.com.happysharebuy.entity.CommonBean;
import djs.com.happysharebuy.utils.Common;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.FileUtils;
import djs.com.happysharebuy.utils.ImageOption;
import djs.com.happysharebuy.utils.LocationUtils;
import djs.com.happysharebuy.utils.PermissionManage;
import djs.com.happysharebuy.utils.RequestService;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.utils.XmlUtils;
import djs.com.happysharebuy.view.SelectedPicPop;

/**
 * Created by hbb on 2017/6/28.
 * 首页录入
 */

public class InputFragment extends BaseFragment implements SelectedPicPop.OnClickListener {

    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.input_fragment_company_address_province)
    private TextView tv_province;
    @ViewInject(R.id.input_fragment_company_address_city)
    private TextView tv_city;
    @ViewInject(R.id.input_fragment_company_address_county)
    private TextView tv_county;
    @ViewInject(R.id.input_fragment_company_industry)
    private TextView tv_industry;
    @ViewInject(R.id.input_fragment_lin)
    private LinearLayout input_fragment_lin;

    @ViewInject(R.id.input_fragment_company_name)
    private EditText edt_company_name;
    @ViewInject(R.id.input_fragment_company_phone)
    private EditText edt_company_phone;
    @ViewInject(R.id.input_fragment_company_address)
    private EditText edt_company_address;
    @ViewInject(R.id.input_fragment_company_contacts)
    private EditText edt_company_contacts;
    @ViewInject(R.id.input_fragment_remarks)
    private EditText edt_remarks;
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
    private ImageView company_image;
    @ViewInject(R.id.input_fragment_company_logo)
    private ImageView company_logo;
    @ViewInject(R.id.input_fragment_company_business_license)
    private ImageView business_license;


    @ViewInject(R.id.input_fragment_radio_group)
    private RadioGroup radio_group;
    @ViewInject(R.id.input_fragment_intention)
    private RadioButton radio_button_intention;
    @ViewInject(R.id.input_fragment_contact)
    private RadioButton radio_button_contact;
    @ViewInject(R.id.location_error_hint)
    private TextView tv_location_error;


    private String str_province, str_city, str_county, str_industry;
    private String str_company_name, str_company_phone, str_company_address, str_company_contacts, str_remarks;
    private String str_busy_number = "", str_trading_area = "", str_wechat = "", str_sub_industry = "";
    private String str_mailbox = "", str_id_card = "";


    private int province_id, city_id, county_id, industry_id;


    private String str_latitude, str_longitude;

    private File mTmpFile;
    private File logoFile;
    private File busyFile;

    private int img_type;//图片类型，1:公司照片，2.公司logo 3.营业执照

    private static final int CAMERA = 1000;//相机

    private static final int REQUESR_CAMERA = 1001;//相机
    private static final int REQUESR_ALBUM = 1002;//相册

    private String imagePath;

    private int status = 1;

//    public String str_longitude = "";
//    public String str_latitude = "";

    public static InputFragment instance;

    private SelectedPicPop selectedPicPop;
    private String logoPath;
    private String busyPath;
    private LocationUtils locationUtils;


    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_fragment, container, false);
        return view;
    }

    @Override
    protected void initView() {
        instance = this;
        tv_title.setText(R.string.input);


        selectedPicPop = new SelectedPicPop(getActivity(), SelectedPicPop.SELECTED_PIC);
        selectedPicPop.setOnClickListener(this);
    }

    @Override
    protected void logic() {

        getLocation();

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

    }

    @Event(value = {R.id.input_fragment_company_address_province, R.id.input_fragment_company_address_city,
            R.id.input_fragment_company_address_county, R.id.input_fragment_company_industry, R.id.input_fragment_company_image,
            R.id.input_fragment_submit, R.id.input_fragment_company_logo, R.id.input_fragment_company_business_license})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_fragment_company_address_province:
                toActivity(SelectAddressActivity.SELECT_PROVINCE);
                break;
            case R.id.input_fragment_company_address_city:
                if (!TextUtils.isEmpty(str_province) && province_id != 0) {
                    toActivity(SelectAddressActivity.SELECT_CITY);
                } else {
                    ToastUtils.showToast(getActivity(), getXmlString(R.string.select_province));
                }
                break;
            case R.id.input_fragment_company_address_county:
                if (!TextUtils.isEmpty(str_city) && city_id != 0) {
                    toActivity(SelectAddressActivity.SELECT_COUNTY);
                } else {
                    ToastUtils.showToast(getActivity(), getXmlString(R.string.select_city));
                }
                break;
            case R.id.input_fragment_company_industry:
                toActivity(SelectAddressActivity.SELECT_INDUSTRY);
                break;
            case R.id.input_fragment_company_image:
                img_type = 1;
                showCameraAction();
                break;
            case R.id.input_fragment_submit:
                if (validateEdtParams()) {
                    startProgressBar(getActivity(), "正在提交...");
                    requestSubmitInfo();
                }
                break;
            case R.id.input_fragment_company_logo:
                img_type = 2;
                if (selectedPicPop != null) {
                    selectedPicPop.showAtLocation(input_fragment_lin, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.input_fragment_company_business_license:
                img_type = 3;
                if (selectedPicPop != null) {
                    selectedPicPop.showAtLocation(input_fragment_lin, Gravity.BOTTOM, 0, 0);
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
                    case 1:
                        if (mTmpFile.exists()) {
                            imagePath = mTmpFile.getAbsolutePath();
                            showCompanyImage(imagePath, company_image);
                        }
                        break;
                    case 2:
                        if (logoFile.exists()) {
                            logoPath = logoFile.getAbsolutePath();
                            showCompanyImage(logoPath, company_logo);
                        }
                        break;
                    case 3:
                        if (busyFile.exists()) {
                            busyPath = busyFile.getAbsolutePath();
                            showCompanyImage(busyPath, business_license);
                        }
                        break;
                }

            } else if (requestCode == REQUESR_ALBUM) {
                Uri uri = data.getData();
                switch (img_type) {
                    case 2:
                        logoPath = FileUtils.getAbsolutePath(getActivity(), uri);
                        showCompanyImage(logoPath, company_logo);
                        break;
                    case 3:
                        busyPath = FileUtils.getAbsolutePath(getActivity(), uri);
                        showCompanyImage(busyPath, business_license);
                        break;
                }
            } else {
                String address = data.getStringExtra("address");
                if (!TextUtils.isEmpty(address)) {
                    if (requestCode == SelectAddressActivity.SELECT_PROVINCE) {
                        province_id = data.getIntExtra("id", 0);
                        str_province = address;
                        tv_province.setText(address);
                        city_id = 0;
                        county_id = 0;
                        tv_city.setText(R.string.select_city);
                        tv_county.setText(R.string.select_county);
                    } else if (requestCode == SelectAddressActivity.SELECT_CITY) {
                        city_id = data.getIntExtra("id", 0);
                        str_city = address;
                        tv_city.setText(address);
                        county_id = 0;
                        tv_county.setText(R.string.select_county);
                    } else if (requestCode == SelectAddressActivity.SELECT_COUNTY) {
                        county_id = data.getIntExtra("id", 0);
                        str_county = address;
                        tv_county.setText(address);
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
                    case 1:
                        mTmpFile = FileUtils.createTmpFile(getActivity());
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                        break;
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
                    case 1:
                        mTmpFile = FileUtils.createTmpFile(getActivity());
                        Uri uri = FileProvider.getUriForFile(getActivity().getApplication(),
                                getActivity().getApplication().getPackageName() + ".FileProvider",
                                mTmpFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        break;
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


    /**
     * 显示公司图片
     *
     * @param url
     */
    private void showCompanyImage(String url, ImageView iv) {
        x.image().bind(iv, url, ImageOption.nomalImageOptions());
    }

    /**
     * 获取输入框输入的值
     */
    private boolean validateEdtParams() {
        str_company_name = edt_company_name.getText().toString().trim();
        str_company_phone = edt_company_phone.getText().toString().trim();
        str_company_address = edt_company_address.getText().toString().trim();
        str_company_contacts = edt_company_contacts.getText().toString().trim();
        str_remarks = edt_remarks.getText().toString().trim();

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
                        if (validateString(imagePath)) {//公司图片
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
                            showToast(getXmlString(R.string.please_selected_company_image));
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
        RequestParams params = CustomRequestParams.getParams(getActivity());
        params.addBodyParameter("act", "addcompany");
        params.addBodyParameter("shanghu", str_company_name);
        params.addBodyParameter("linkName", str_company_contacts);
        params.addBodyParameter("tel", str_company_phone);
        params.addBodyParameter("addr", str_company_address);
        params.addBodyParameter("province", str_province);
        params.addBodyParameter("city", str_city);
        params.addBodyParameter("dist", str_county);
        params.addBodyParameter("cid", industry_id + "");
        params.addBodyParameter("txt1", str_remarks);
        params.addBodyParameter("longitude", str_longitude);
        params.addBodyParameter("latitude", str_latitude);
        params.addBodyParameter("status", status + "");
        params.addBodyParameter("weixin", str_wechat);
        params.addBodyParameter("busarer", str_trading_area);
        params.addBodyParameter("licenseNo", str_busy_number);
        params.addBodyParameter("cid2", str_sub_industry);
        params.addBodyParameter("email", str_mailbox);
        params.addBodyParameter("idcard", str_id_card);
        String newPicPath = FileUtils.savePic(imagePath, getActivity());
        File newFile = new File(newPicPath);
        params.addBodyParameter("img1", newFile, "multipart/form-data");
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
                cleanInfo();
                Intent intent = new Intent();
                intent.setAction(Common.ACTION);
                intent.putExtra(Common.ACTION_REFRESH, "refresh");
                intent.putExtra(Common.ACTION_FLAG, status);
                getActivity().sendBroadcast(intent);
            }
        }
    }


    /**
     * 清空输入信息
     */
    private void cleanInfo() {
        edt_company_name.setText("");
        edt_company_phone.setText("");
        edt_company_address.setText("");
        edt_company_contacts.setText("");
        edt_busy_number.setText("");
        edt_wechat.setText("");
        edt_trading_area.setText("");
        edt_sub_industry.setText("");
        edt_mailbox.setText("");
        edt_id_card.setText("");
        edt_remarks.setText("");

        imagePath = "";
        logoPath = "";
        busyPath = "";
        company_image.setImageResource(R.mipmap.default_picture);
        company_logo.setImageResource(R.mipmap.default_picture);
        business_license.setImageResource(R.mipmap.default_picture);
        tv_province.setText(R.string.select_province);
        tv_city.setText(R.string.select_city);
        tv_county.setText(R.string.select_county);
        tv_industry.setText(R.string.select_industry);
        radio_button_intention.setChecked(false);
        radio_button_contact.setChecked(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationUtils.destroyLocation();
    }

    public void hideLocationHError() {
        if (tv_location_error != null) {
            tv_location_error.setVisibility(View.GONE);
        }
    }


    /**
     * 获取XML的String
     *
     * @param id
     * @return
     */
    private String getXmlString(int id) {
        return XmlUtils.getString(getActivity(), id);
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
     * 获取当前位子
     */
    private void getLocation() {
        locationUtils = new LocationUtils(getActivity());
        locationUtils.startLocation();
        locationUtils.setLocationClient(new LocationUtils.LocationClientOption() {
            @Override
            public void onLocationChanged(String city, String longitude, String latitude) {
                if (!TextUtils.isEmpty(city) && !TextUtils.equals("定位失败", city)) {
                    str_longitude = longitude;
                    str_latitude = latitude;
                }else {
                    tv_location_error.setVisibility(View.VISIBLE);
                    tv_location_error.setText(R.string.location_error);
                }
            }
        });
    }


}
