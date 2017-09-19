package djs.com.happysharebuy.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.BaseActivity;
import djs.com.happysharebuy.adapter.BigPicturePreview;
import djs.com.happysharebuy.entity.ImageBean;
import djs.com.happysharebuy.utils.CloseActivityClass;
import djs.com.happysharebuy.view.CustomViewPager;


/**
 * 动态的大图预览
 *
 * @author hubb 2015-4-19
 */
public class MviewPager extends Activity implements OnClickListener {

	private CustomViewPager viewPager;
	private BigPicturePreview adapter;

	private List<ImageBean> list;

	private int position;

	public static MviewPager intance;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		// 定义全屏参数
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		// 获得当前窗体对象
		Window window = MviewPager.this.getWindow();
		// 设置当前窗体为全屏显示
		window.setFlags(flag, flag);
		setContentView(R.layout.viewpager);
		intance = this;
		CloseActivityClass.activityList.add(intance);

		Intent intent = getIntent();
		list = (List<ImageBean>)intent.getSerializableExtra("img_urls");
		position = intent.getIntExtra("position", -1);
		initView();
		if (list.size() > 0) {
			adapter = new BigPicturePreview(MviewPager.this, list);
			viewPager.setAdapter(adapter);
		}
		if (position != -1) {
			viewPager.setCurrentItem(position);
		} else {
			viewPager.setCurrentItem(0);
		}

	}

	/**
	 * 初始化设置控件
	 */
//	@Override
	private void initView() {
		viewPager = (CustomViewPager) findViewById(R.id.img_viewPager);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			closeActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			default:
				break;
		}
	}

	/**
	 * 关闭当前的界面
	 */
	public void closeActivity() {
//		removeCollect();
		MviewPager.this.finish();
		//		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	}

}
