package djs.com.happysharebuy.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

	public CustomViewPager(Context context) {
		super(context);
	}
	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		try {  
			return super .dispatchTouchEvent(ev);  
		} catch (IllegalArgumentException ignored) {
		} catch (ArrayIndexOutOfBoundsException e) {
		}  

		return false ;  

	}  


}
