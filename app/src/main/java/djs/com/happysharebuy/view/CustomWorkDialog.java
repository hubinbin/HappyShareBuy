package djs.com.happysharebuy.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.utils.XmlUtils;

/**
 * Created by hbb on 2017/6/6.
 * 自定义业务逻辑样式的dialog
 */

public class CustomWorkDialog {

    private static Dialog dialog;
    private static Context context;
    private static OnClickListener setOnClickListener;

    public static int LOGOUT = 1;//退出登入
    public static int DELETE_RECORDS = 2;//删除业务记录

    /**
     * 显示dialog
     * @param context
     * @param storeName
     * @param type
     */
    public static void showDialog(Context context, String storeName, final int type) {
        CustomWorkDialog.context = context;
        final View diaView = View.inflate(context, R.layout.custom_dialog, null);
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(diaView);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        initDialogView(diaView, storeName, type);
        diaView.findViewById(R.id.custom_dialog_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        dialog = null;
                    }
                });
        diaView.findViewById(R.id.custom_dialog_confirm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        CustomWorkDialog.setOnClickListener.onClick(type);
                        dialog.dismiss();
                        dialog = null;
                    }
                });

//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode,
//                                 KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK
//                        && event.getRepeatCount() == 0) {
//
//                    dialog.dismiss();
//                    dialog = null;
//                }
//                return true;
//            }
//        });
    }

    /**
     * dialog初始化控件
     *
     * @param diaView
     */
    private static void initDialogView(View diaView, String storeName, int type) {

        String str_hint = "";
        String str_lift = "";
        String str_right = "";

        TextView cancel = (TextView) diaView.findViewById(R.id.custom_dialog_cancel);
        TextView confirm = (TextView) diaView.findViewById(R.id.custom_dialog_confirm);
        TextView hint = (TextView) diaView.findViewById(R.id.custom_dialog_hint_content);
        hint.setVisibility(View.VISIBLE);

        if (type == LOGOUT) {
            str_hint = XmlUtils.getString(CustomWorkDialog.context, R.string.logout_hint);
            str_lift = XmlUtils.getString(CustomWorkDialog.context, R.string.cancel);
            str_right = XmlUtils.getString(CustomWorkDialog.context, R.string.quit);
        }else if (type == DELETE_RECORDS){
            str_hint = XmlUtils.getString(CustomWorkDialog.context, R.string.confirm_delete_record);
            str_lift = XmlUtils.getString(CustomWorkDialog.context, R.string.cancel);
            str_right = XmlUtils.getString(CustomWorkDialog.context, R.string.confirm);
        }

        cancel.setText(str_lift);
        confirm.setText(str_right);
        hint.setText(str_hint);
        dialog.setCanceledOnTouchOutside(true);
//        if (isForced) {
//            dialog.setCanceledOnTouchOutside(false);
//            cancel.setVisibility(View.GONE);
//            line.setVisibility(View.GONE);
//        }else {
//            dialog.setCanceledOnTouchOutside(true);
//        }
    }

    /**
     * 销毁dialog
     */
    public static void derDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public interface OnClickListener {
        void onClick(int type);
    }

    public static void setOnClickListener(OnClickListener setOnClickListener) {
        CustomWorkDialog.setOnClickListener = setOnClickListener;
    }

}
