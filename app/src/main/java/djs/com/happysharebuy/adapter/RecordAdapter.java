package djs.com.happysharebuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.List;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.entity.BusinessBean;
import djs.com.happysharebuy.utils.DateUtil;
import djs.com.happysharebuy.utils.ImageOption;

/**
 * Created by bin on 2017/7/5.
 * 业务列表的Adapter
 */

public class RecordAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    private List<BusinessBean> businesses;

    public RecordAdapter (Context context){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    public void setData(List<BusinessBean> businesses){
        this.businesses = businesses;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (businesses != null) {
            return businesses.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return businesses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.record_item,null);
            viewHolder.img_avatar = (ImageView) view.findViewById(R.id.recoed_item_avatar);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.recoed_item_name);
            viewHolder.tv_record = (TextView) view.findViewById(R.id.recoed_item_business);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.recoed_item_time);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BusinessBean businessBean = businesses.get(position);

        viewHolder.tv_name.setText(businessBean.getRealName());
        viewHolder.tv_record.setText(businessBean.getTxt1());
       String addtime = businessBean.getAddtime();
        viewHolder.tv_time.setText( DateUtil.disposeDate(addtime));
        x.image().bind(viewHolder.img_avatar,businessBean.getTximg(), ImageOption.circularImageOptions());

        return view;
    }

    private class ViewHolder{
        private ImageView img_avatar;
        private TextView tv_name,tv_record,tv_time;


    }
}
