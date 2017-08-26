package djs.com.happysharebuy.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.utils.Common;
import djs.com.happysharebuy.utils.PrefUtil;
import djs.com.happysharebuy.utils.QRCode;


/**
 * Created by bin on 2017/8/22.
 * 自定义UI展示dialog
 */

public class CustomDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity context;
    private int type;

    public static final int TYPE_QRCODE = 1;//二维码

    /**
     * @param context
     * @param type    弹出框类型
     */
    public CustomDialog(Activity context, int type) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.type = type;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (type == TYPE_QRCODE) {
            setContentView(R.layout.dialog_qrcode);
        }
        this.setCanceledOnTouchOutside(true);
        initView();
    }


    /**
     * 初始化设置控件
     */
    private void initView() {
        findViewById(R.id.dialog_out_side_cancel).setOnClickListener(this);
        if (type == TYPE_QRCODE){
            int perid = PrefUtil.getIntPref(context, PrefUtil.USER_ID, 0);
            ImageView img = (ImageView) findViewById(R.id.dialog_qrcode_img);
            img.setImageBitmap(QRCode.createQRCode(context,Common.QR_CODE_URL+perid));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_out_side_cancel:
                this.dismiss();
                break;
        }
    }
}
