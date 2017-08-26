package djs.com.happysharebuy.utils;


import android.widget.ImageView;

import org.xutils.image.ImageOptions;

import djs.com.happysharebuy.R;


/**
 * 得到x.image 的配置参数
 * @author hbb
 */
public class ImageOption {

    private static ImageOptions imageOptions;

    private static ImageOptions circularImageOptions;

    /**
     * 得到正常的参数配置
     */
    public static ImageOptions nomalImageOptions() {
        imageOptions = new ImageOptions.Builder()
                //.setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                //.setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                //.setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                //.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.default_picture)
                .setFailureDrawableId(R.mipmap.default_picture)
                .setUseMemCache(true)
                .build();

        return imageOptions;
    }

    /**
     * 得到圆形的配置参数
     */
    public static ImageOptions circularImageOptions() {
        if (imageOptions != null){
            imageOptions = null;
        }
        circularImageOptions = new ImageOptions.Builder()
                //.setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                //.setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                //.setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                //.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.circular_gray)
                .setFailureDrawableId(R.drawable.circular_gray)
                .setCircular(true)
                .build();

        return circularImageOptions;
    }

}
