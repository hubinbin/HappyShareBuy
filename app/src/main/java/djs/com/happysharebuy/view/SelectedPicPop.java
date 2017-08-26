package djs.com.happysharebuy.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import djs.com.happysharebuy.R;

/**
 * Created by hbb on 2017/6/29.
 * 选择图片
 */

public class SelectedPicPop extends PopupWindow {

    private Context context;
    private View mMenuView;

    private OnClickListener onClickListener;

    public static int SELECTED_PIC = 1;
    public static int SELECTED_MODIFY = 2;


    public SelectedPicPop(Context context, int flag) {
        this.context = context;
        initView(flag);
    }

    /***
     * 初始化设置控件
     */
    private void initView(int flag) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.selected_pic_pop, null);

        Button btn1 = (Button) mMenuView.findViewById(R.id.selected_pic_camera);
        Button btn2 = (Button) mMenuView.findViewById(R.id.selected_pic_album);

        switch (flag) {
            case 1:
                btn1.setText(R.string.camera);
                btn2.setText(R.string.album);
                break;
            case 2:
                btn1.setText(R.string.modify_company_info);
                btn2.setText(R.string.add_record);
                break;
        }


        // 导入布局
        this.setContentView(mMenuView);
        // 设置动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置可触
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x0ffffff);
        this.setBackgroundDrawable(dw);
        // 单击弹出窗以外处 关闭弹出窗
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.selected_pic_lin).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        mMenuView.findViewById(R.id.selected_pic_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(true);
                dismiss();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(false);
                dismiss();
            }
        });


    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public interface OnClickListener {
        void onClick(boolean isCamera);
    }

}
