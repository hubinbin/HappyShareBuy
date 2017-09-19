package djs.com.happysharebuy.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import djs.com.happysharebuy.HSBApplication;
import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseFragment;
import djs.com.happysharebuy.adapter.ImageAdapter;
import djs.com.happysharebuy.entity.ImageBean;
import djs.com.happysharebuy.utils.ErrorDispose;
import djs.com.happysharebuy.utils.HttpUrlUtils;
import djs.com.happysharebuy.utils.ToastUtils;

/**
 * Created by bin on 2017/9/15.
 * 图片信息的fragment
 */

public class PicInfoFragment extends BaseFragment {

    @ViewInject(R.id.pic_info_fragment)
    private LinearLayout fragment_lin;
    @ViewInject(R.id.pic_info_list)
    private RecyclerView picture_list;
    @ViewInject(R.id.pic_info_no)
    private TextView tv_no_pic;

    private ImageAdapter imageAdapter;

    private int Id;
    private static final int ADD_PICTURE = 1001;
    private List<ImageBean> images;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pic_info_fragment, container, false);
        return view;
    }

    @Override
    protected void initView() {
        Id = ClientDetailsActivity.instance.Id;
        tv_no_pic.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        picture_list.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter();
        picture_list.setAdapter(imageAdapter);

        requestPicture();

        imageAdapter.setOnItemClickListener(new ImageAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                if (images.size() > 0) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MviewPager.class);
                    intent.putExtra("img_urls", (Serializable) images);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void logic() {

    }

    /**
     * 跳转添加图片
     */
    public void toAddPicture() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AddPictureActivity.class);
        intent.putExtra("Id", Id);
        startActivityForResult(intent, ADD_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == ADD_PICTURE) {
                requestPicture();
            }
        }

    }

    /**
     * 获取本页面的焦点
     */
    public void setGetFocus() {
        if (fragment_lin != null) {
            fragment_lin.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);//在根之前获取焦点
        }
    }

    /**
     * 关闭本页面的焦点
     */
    public void setCloseFocus() {
        if (fragment_lin != null) {
            fragment_lin.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }
    }

    /**
     * 请求图片列表
     */
    private void requestPicture() {
        RequestParams params = new RequestParams(HttpUrlUtils.url);
        params.addBodyParameter("act", "getcompanyphoto");
        params.addBodyParameter("keyid", Id + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                LogUtil.d(s);
                requestPictureSuccess(s);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                ErrorDispose.xHttpError(throwable, b);
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
     * 图片列表请求成功处理
     *
     * @param result
     */
    private void requestPictureSuccess(String result) {
        if (!TextUtils.isEmpty(result)) {
            Gson gson = HSBApplication.getGson();
            images = gson.fromJson(result, new TypeToken<LinkedList<ImageBean>>() {
            }.getType());
            if (images != null) {
                imageAdapter.setData(images);
                if (images.size() == 0) {
                    tv_no_pic.setVisibility(View.VISIBLE);
                } else {
                    tv_no_pic.setVisibility(View.GONE);
                }
            }
        } else {
            ToastUtils.showToast(getActivity(), "网络异常，请稍后重试");
        }
    }
}
