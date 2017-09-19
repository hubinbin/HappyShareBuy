package djs.com.happysharebuy.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.activity.client.MviewPager;
import djs.com.happysharebuy.entity.ImageBean;
import djs.com.happysharebuy.utils.ImageOption;
import djs.com.happysharebuy.view.ScaleView;
import djs.com.happysharebuy.view.ScaleViewAttacher;


/**
 * 大图预览的adapter
 */
public class BigPicturePreview extends PagerAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<ImageBean> list;


	/**
	 * 自定义成长预览图片的适配器
	 * @param context
	 * @param list
	 */
	public BigPicturePreview(Context context, List<ImageBean> list) {
		super();
		this.context = context;
		initAdapterData(list);
		inflater = LayoutInflater.from(context);

	}
	private void initAdapterData(List<ImageBean> list) {
		if (this.list==null){
			this.list=new ArrayList<ImageBean>();
		}
		if (list!=null&&list.size()>=0){
			this.list.clear();
			this.list.addAll(list);
		}
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object)   {
		return POSITION_NONE;
		//		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(View container, final int position) {
		//友盟上的错误，可能由于自由定制的ROM导致，暂时做这样处理,保证不crash,后期可能做成一个默认页。
		View view=null;
		try {
			view = inflater.inflate(R.layout.viewpager_item, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(view==null){
			view =new View(context);
			return view;
		}
		final ScaleView mImg = (ScaleView) view.findViewById(R.id.div_img);
		String imgUrl = list.get(position).getImgurl();
		mImg.setVisibility(View.VISIBLE);
		x.image().bind(mImg,imgUrl, ImageOption.nomalImageOptions());

		((ViewPager) container).addView(view, 0);
		mImg.setOnViewTapListener(new ScaleViewAttacher.OnViewTapListener() {
			@Override
			public void onViewTap(View view, float x, float y) {
				if (MviewPager.intance != null){
					MviewPager.intance.finish();
				}
			}
		});

		return view;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(container);
	}

//	public int getPosition () {
//		return index;
//	}

}
