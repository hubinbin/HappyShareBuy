package djs.com.happysharebuy.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.x;

import java.util.List;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.entity.ImageBean;
import djs.com.happysharebuy.utils.ImageOption;

/**
 * Created by bin on 2017/9/15.
 * 图片列表的adapter
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<ImageBean> images;
    private ItemClickListener itemClickListener;
    private ItemLongClickListener itemlongClickListener;

    /***
     * 添加数据
     * @param images
     */
    public void setData(List<ImageBean> images) {
        this.images = images;
        notifyDataSetChanged();
    }


    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.image_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        holder.setData(this.images.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (this.images != null) {
            return this.images.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img, add_img, img_del;


        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.image_item_img);
            add_img = (ImageView) itemView.findViewById(R.id.image_item_add_img);
            img_del = (ImageView) itemView.findViewById(R.id.image_item_img_del);
        }

        public void setData(ImageBean imageBean, final int position) {
            String imgurl = imageBean.getImgurl();
            if (TextUtils.equals("add", imgurl)) {
                add_img.setVisibility(View.VISIBLE);
            } else {
                add_img.setVisibility(View.GONE);
                x.image().bind(img, imgurl, ImageOption.nomalImageOptions());
                boolean isDelect = imageBean.isDelect();
                if (isDelect) {
                    img_del.setVisibility(View.VISIBLE);
                } else {
                    img_del.setVisibility(View.GONE);
                }
            }
            add_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClickListener(0);
                }
            });

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClickListener(position);
                }
            });

            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemlongClickListener.onItemLongClickListener(position);
                    return true;
                }
            });

            img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    images.remove(position);
                    notifyDataSetChanged();
                }
            });

        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(ItemLongClickListener itemlongClickListener) {
        this.itemlongClickListener = itemlongClickListener;
    }

    public interface ItemLongClickListener {
        void onItemLongClickListener(int position);
    }

    public interface ItemClickListener {
        void onItemClickListener(int position);
    }
}
