package djs.com.happysharebuy.activity.main;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import djs.com.happysharebuy.activity.LoginActivity;
import djs.com.happysharebuy.activity.setting.ModifyPasswordActivity;
import djs.com.happysharebuy.entity.CommonBean;
import djs.com.happysharebuy.utils.CloseActivityClass;
import djs.com.happysharebuy.utils.CustomRequestParams;
import djs.com.happysharebuy.utils.FileUtils;
import djs.com.happysharebuy.utils.ImageOption;
import djs.com.happysharebuy.utils.PermissionManage;
import djs.com.happysharebuy.utils.PrefUtil;
import djs.com.happysharebuy.utils.RequestService;
import djs.com.happysharebuy.utils.ToastUtils;
import djs.com.happysharebuy.utils.XmlUtils;
import djs.com.happysharebuy.view.CustomWorkDialog;
import djs.com.happysharebuy.view.SelectedPicPop;


/**
 * Created by hbb on 2017/6/28.
 * 首页设置
 */

public class SettingFragment extends BaseFragment implements SelectedPicPop.OnClickListener, CustomWorkDialog.OnClickListener {

    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.setting_user_avatar)
    private ImageView user_avatar;
    @ViewInject(R.id.setting_user_nick_name)
    private TextView tv_nick_name;
    @ViewInject(R.id.setting_fragment_lin)
    private LinearLayout setting_fragment_lin;


    private SelectedPicPop selectedPicPop;

    private static final int CAMERA = 1000;//相机
    private static final int ALBUM = 1001;//相册
    private static final int ZOOM_PHOTO = 1002;//裁剪
    private String cameraPath;
    private File mTmpFile;

    private String imgPath;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        return view;
    }

    @Override
    protected void initView() {
        tv_title.setText(R.string.setting);
        String name = PrefUtil.getStringPref(getActivity(), PrefUtil.USER_NAME);
        tv_nick_name.setText(name);
        String avatarUrl = PrefUtil.getStringPref(getActivity(), PrefUtil.USER_AVATAR);
        setAvatar(avatarUrl);
        selectedPicPop = new SelectedPicPop(getActivity(),SelectedPicPop.SELECTED_PIC);
        selectedPicPop.setOnClickListener(this);
        CustomWorkDialog.setOnClickListener(this);
    }

    @Override
    protected void logic() {

    }

    @Event(value = {R.id.setting_modify_password, R.id.setting_modify_avatar, R.id.setting_logout})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_modify_password:
                Intent intent = new Intent();
                intent.setClass(getActivity(), ModifyPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_modify_avatar:
                if (selectedPicPop != null) {
                    selectedPicPop.showAtLocation(setting_fragment_lin, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.setting_logout:
                CustomWorkDialog.showDialog(getActivity(), "", CustomWorkDialog.LOGOUT);
                break;
        }
    }

    @Override
    public void onClick(boolean isCamera) {
        if (!isCamera) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, ALBUM);
        } else {
            if (PermissionManage.getPermission(getActivity(),Manifest.permission.CAMERA)){//判断是否开启照相机权限
                showCameraAction();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == ALBUM) {
                Uri uri = data.getData();
                String path = getAbsolutePath(getActivity(), uri);
                setAvatar(path);
                requestModifyAvatar(path);
            } else if (requestCode == CAMERA) {
                if (mTmpFile.exists()) {
                    requestModifyAvatar(mTmpFile.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 获取相对地址
     *
     * @param context
     * @param uri
     * @return
     */
    public String getAbsolutePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 显示头像
     * @param url
     */
    private void setAvatar(String url) {
        x.image().bind(user_avatar, url, ImageOption.circularImageOptions());
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = FileUtils.createTmpFile(getActivity());

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            } else {
                Uri uri = FileProvider.getUriForFile(getActivity().getApplication(),
                        getActivity().getApplication().getPackageName() + ".FileProvider",
                        mTmpFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            startActivityForResult(cameraIntent, CAMERA);

        } else {
            ToastUtils.showToast(getActivity(), XmlUtils.getString(getActivity(), R.string.msg_no_camera));
        }
    }

    @Override
    public void onClick(int type) {
        if (type == CustomWorkDialog.LOGOUT) {
            if (MainActivity.instance != null) {
                MainActivity.instance.finish();
            }
            CloseActivityClass.exitClient(getActivity());
            PrefUtil.savePref(getActivity(), PrefUtil.USER_ID, 0);
            Intent intent = new Intent();
            intent.setClass(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    /***
     * 请求修改头像
     * @param imgPath
     */
    private void requestModifyAvatar(String imgPath){
        this.imgPath = imgPath;
        RequestParams params = CustomRequestParams.getParams(getActivity());
        params.setMultipart(true);
        params.addBodyParameter("act", "modtximg");
        String newPicPath = FileUtils.savePic(imgPath, getActivity());
        File newFile = new File(newPicPath);
        params.addBodyParameter("file", newFile, "multipart/form-data");
        RequestService.post(getActivity(), params, new RequestService.CustomCallback() {
            @Override
            public void onSuccess(String result) {
                requestModifyAvatarSuccess(result);
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 修改头像请求成功处理
     * @param result
     */
    private void requestModifyAvatarSuccess(String result){
        Gson gson = HSBApplication.getGson();
        CommonBean commonBean = gson.fromJson(result, CommonBean.class);
        if (commonBean != null){
            ToastUtils.showToast(getActivity(),commonBean.getMsg());
            int status = commonBean.getStatus();
            if (status == 1){
                setAvatar(imgPath);
            }
        }

    }

}
