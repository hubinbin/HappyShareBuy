package djs.com.happysharebuy.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import org.xutils.common.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by hbb on 2017/6/29.
 * 文件工具类
 */

public class FileUtils {

    public static String file = "/happysharebuy/";
    public static String IMAGE = "image";
    public static String CACHE = "cache";

    public static String state = Environment.getExternalStorageState();


    /**
     * 创建文件
     * @param context
     * @return
     */
    public static File createTmpFile(Context context) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File filePath2 = getFilePath(context, IMAGE);
            if (filePath2 == null) {
                return null;
            }
            String filePath = filePath2.getAbsolutePath();
            File pic = new File(filePath);
            // 判断文件夹是否存在,如果不存在则创建文件夹
            if (!pic.exists()) {
                pic.mkdir();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(pic, fileName + ".jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".jpg");
            return tmpFile;
        }

    }

    /**
     * 得到文件的路径
     *
     * @param context
     * @param fileName
     * @return
     */
    public static File getFilePath(Context context, String fileName) {
        String filePath = getExternalSdCardPath(context) + file + fileName;
        if (context == null) {
            return null;
        }
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取扩展SD卡存储目录
     * <p>
     * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录
     * 否则：返回内置SD卡目录
     *
     * @return
     */
    public static String getExternalSdCardPath(Context context) {
        String path = "";
        if (isMounted()) {
            File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            path = sdCardFile.getAbsolutePath();
        }
        if (TextUtils.isEmpty(path)) {
            path = context.getCacheDir().getAbsolutePath();
        }
        return path;
    }

    /**
     * 有无SD卡判断
     * @return
     */
    public static boolean isMounted() {
        String str = Environment.getExternalStorageState();
        if (str.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 压缩图片
     * @param path
     * @param
     * @return
     */
    public static String savePic(String path, Context context) {
        String picName = "";
        Bitmap btm;
        File filePath = getFilePath(context, CACHE);
        if (filePath == null) {
            return "";
        }
        String folder = filePath.getAbsolutePath();
        try {
            picName = UUID.randomUUID().toString()+".jpg";
            Bitmap bm = getSmallBitmap(path);
            btm = setRotation(path, bm);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            btm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            FileOutputStream fos = new FileOutputStream(new File(
                    getAlbumDir(folder), picName));
            fos.write(baos.toByteArray());
            fos.close();
            baos.close();
            if(btm!=null && !btm.isRecycled()){
                btm.recycle();
                btm = null;
            }
        }  catch (OutOfMemoryError e) {
            LogUtil.e( "OutOfMemoryError", e);
        }catch (Exception e) {
            LogUtil.e( "error", e);
        }
        return folder + "/" + picName;
    }

    /**
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 720, 960);
        options.inJustDecodeBounds = false;
        //		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inPurgeable = true;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (height < width) {
            int manger = height;
            height = width;
            width = manger;
        }
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            // if(height>4000|| width>3000){
            // inSampleSize+=2;
            // }else{
            // inSampleSize ++;
            // }
        }
        if(inSampleSize > 2 && inSampleSize%2 ==1 ){
            inSampleSize++;
        }
        return inSampleSize;
    }


    /**
     * @param path
     * @param bm
     * @return
     *
     */
    public static Bitmap setRotation(String path, Bitmap bm) {
        Bitmap btm = null;
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            // 锟斤拷锟界�����锟斤拷锟斤拷��ワ拷��锟斤拷濞�锟斤拷锟斤拷濞达拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥��锟斤拷锟斤拷���锟�
            // int ori =
            // exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        if (degree != 0) {
            // 锟斤拷锟界�ｏ拷濞达拷锟斤拷��ワ拷��锟斤拷
            Matrix m = new Matrix();
            m.postRotate(degree);
            btm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                    m, true);
        } else if (degree == 0) {
            // 锟斤拷锟界�ｏ拷濞达拷锟斤拷��ワ拷��锟斤拷
            Matrix m = new Matrix();
            m.postRotate(degree);
            btm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                    m, true);
        }
        return btm;
    }

    /**
     * 判断文件是否存在
     * @return
     */
    public static File getAlbumDir(String filePath) {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }


    /**
     * 获取相对地址
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getAbsolutePath(final Context context, final Uri uri) {
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


}
