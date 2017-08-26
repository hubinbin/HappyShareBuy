package djs.com.happysharebuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import djs.com.happysharebuy.R;
import djs.com.happysharebuy.entity.ProvinceBean;

/**
 * Created by bin on 2017/7/3.
 * 省列表的adapter
 */

public class ProvinceAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<ProvinceBean> provinces;

    public ProvinceAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    /**
     * 添加数据
     * @param provinces
     */
    public void setData(List<ProvinceBean> provinces){
        this.provinces = provinces;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (provinces != null) {
            return provinces.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return provinces.get(position);
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
            view = inflater.inflate(R.layout.city_item,null);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.city_item_tv);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ProvinceBean provinceBean = provinces.get(position);
        viewHolder.tv_name.setText(provinceBean.getProvinceName());

        return view;
    }

    private class ViewHolder{
        private TextView tv_name;
    }

}
