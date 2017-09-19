package djs.com.happysharebuy.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseActivity;
import djs.com.happysharebuy.adapter.ImageAdapter;
import djs.com.happysharebuy.entity.CommonBean;
import djs.com.happysharebuy.entity.ImageBean;
import djs.com.happysharebuy.selectpic.MultiImageSelectorActivity;
import djs.com.happysharebuy.utils.ErrorDispose;
import djs.com.happysharebuy.utils.FileUtils;
import djs.com.happysharebuy.utils.HttpUrlUtils;
import djs.com.happysharebuy.utils.ToastUtils;

/**
 * Created by bin on 2017/9/14.
 * 图片信息界面
 */
@ContentView(R.layout.pic_info_activity)
public class AddPictureActivity extends BaseActivity {

    @ViewInject(R.id.title_bar_back)
    private LinearLayout back;
    @ViewInject(R.id.title_bar_name)
    private TextView tv_title;
    @ViewInject(R.id.add_picture_list)
    private RecyclerView picture_list;
    @ViewInject(R.id.title_bar_right_tv)
    private TextView submit;


    private List<ImageBean> images = new ArrayList<>();
    private ImageAdapter imageAdapter;

    private static final int REQUEST_SELECT_PICTURE = 1001;

    private int Id;


    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        back.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.add_picture);
        submit.setVisibility(View.VISIBLE);
        submit.setText("上传");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        picture_list.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter();
        picture_list.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(new ImageAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                if (position == 0) {
                    Intent intent2 = new Intent(AddPictureActivity.this, MultiImageSelectorActivity.class);
                    intent2.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                    intent2.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1000 - images.size());
//                    intent2.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, imgs);
                    startActivityForResult(intent2, REQUEST_SELECT_PICTURE);
                } else {
                    if (images.size() > 1) {
                        List<ImageBean> previewImages = new ArrayList<>();
                        for (int i = 0; i < images.size(); i++) {
                            String url = images.get(i).getImgurl();
                            if (TextUtils.equals("add", url)) {
                                continue;
                            }
                            previewImages.add(images.get(i));
                        }
                        Intent intent = new Intent();
                        intent.setClass(AddPictureActivity.this, MviewPager.class);
                        intent.putExtra("img_urls", (Serializable) previewImages);
                        intent.putExtra("position", position - 1);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    protected void logic() {
        ImageBean imageBean = new ImageBean();
        imageBean.setImgurl("add");
        images.add(imageBean);
        imageAdapter.setData(images);
        Intent intent = getIntent();
        Id = intent.getIntExtra("Id", 0);

    }


    @Event(value = {R.id.title_bar_right_tv, R.id.title_bar_back})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_right_tv:
                if (validatePic()) {
                    submitPic();
                }
                break;
            case R.id.title_bar_back:
                AddPictureActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                ArrayList<String> list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (list != null) {
                    List<ImageBean> imgs = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        ImageBean imageBean = new ImageBean();
                        imageBean.setImgurl(list.get(i));
                        imageBean.setDelect(true);
                        imgs.add(imageBean);
                    }
                    images.addAll(imgs);
                    imgs.clear();
                    imgs = null;
                }
                imageAdapter.setData(images);
            }
        }
    }

    /**
     * 验证图片
     */
    private boolean validatePic() {
        if (images.size() > 1) {
            return true;
        } else {
            ToastUtils.showToast(AddPictureActivity.this, "请选择图片");
        }
        return false;
    }

    /**
     * 提交图片
     */
    private void submitPic() {
        startProgressBar(AddPictureActivity.this, "正在上传，请稍后...");
        RequestParams params = new RequestParams(HttpUrlUtils.url);
        params.addBodyParameter("act", "addcompanyphoto");
        params.addBodyParameter("keyid", Id + "");
        for (int i = 0; i < images.size(); i++) {
            String url = images.get(i).getImgurl();
            if (TextUtils.equals("add", url)) {
                continue;
            }
            String logoPicPath = FileUtils.savePic(url, AddPictureActivity.this);
            params.addBodyParameter("logo", new File(logoPicPath), "multipart/form-data");
        }

        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                LogUtil.d(jsonObject.toString());
                submitPicSuccess(jsonObject.toString());
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                ErrorDispose.xHttpError(throwable, b);
                ToastUtils.showToast(AddPictureActivity.this, "图片上传失败,请稍后重试");
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

    /**
     * 图片上传成功处理
     *
     * @param result
     */
    private void submitPicSuccess(String result) {
        if (!TextUtils.isEmpty(result)) {
            Gson gson = HSBApplication.getGson();
            CommonBean commonBean = gson.fromJson(result, CommonBean.class);
            if (commonBean != null) {
                ToastUtils.showToast(AddPictureActivity.this, commonBean.getMsg());
                int status = commonBean.getStatus();
                if (status == 1) {
                    setResult(RESULT_OK);
                    AddPictureActivity.this.finish();
                }
            }
        } else {
            ToastUtils.showToast(AddPictureActivity.this, "图片上传失败,请稍后重试");
        }

    }
}
