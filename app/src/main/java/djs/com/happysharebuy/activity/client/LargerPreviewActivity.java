package djs.com.happysharebuy.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseActivity;
import djs.com.happysharebuy.utils.ImageOption;

/**
 * Created by bin on 2017/7/10.
 * 大图预览
 */
@ContentView(R.layout.larger_preview_activity)
public class LargerPreviewActivity extends BaseActivity {

    @ViewInject(R.id.larger_preview_imag)
    private ImageView img;

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        x.image().bind(img, url, ImageOption.nomalImageOptions());

    }

    @Override
    protected void logic() {


    }

    @Event(value = {R.id.larger_preview_imag})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.larger_preview_imag:
                LargerPreviewActivity.this.finish();
                break;
        }
    }
}
