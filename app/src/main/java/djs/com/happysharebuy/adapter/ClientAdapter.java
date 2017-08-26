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
import djs.com.happysharebuy.entity.ClientBean;
import djs.com.happysharebuy.utils.DateUtil;
import djs.com.happysharebuy.utils.ImageOption;
import djs.com.happysharebuy.utils.XmlUtils;

/**
 * Created by bin on 2017/7/4.
 * 客户的adapter
 *
 */

public class ClientAdapter  extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    private List<ClientBean> clients;

    public ClientAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    /**
     * 适配器添加数据
     * @param clients
     */
    public void setData(List<ClientBean> clients){
        this.clients = clients;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (clients != null) {
            return clients.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return clients.get(position);
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
            view = inflater.inflate(R.layout.client_item,null);
            viewHolder.img = (ImageView) view.findViewById(R.id.client_item_company_image);
            viewHolder.tv_company_name = (TextView) view.findViewById(R.id.client_item_company_name);
            viewHolder.tv_status = (TextView) view.findViewById(R.id.client_item_company_status);
            viewHolder.tv_link_name = (TextView) view.findViewById(R.id.client_item_company_link_name);
            viewHolder.tv_phone = (TextView) view.findViewById(R.id.client_item_company_phone);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.client_item_company_time);
            viewHolder.tv_address = (TextView) view.findViewById(R.id.client_item_company_address);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ClientBean clientBean = clients.get(position);

        x.image().bind(viewHolder.img,clientBean.getImgurl(), ImageOption.nomalImageOptions());
        viewHolder.tv_company_name.setText(clientBean.getShanghu());
        viewHolder.tv_link_name.setText(getXmlString(R.string.contacts_colon)+clientBean.getLinkName());
        viewHolder.tv_phone.setText(getXmlString(R.string.contact_number_colon)+clientBean.getTel());
        String time = clientBean.getAddtime();
        viewHolder.tv_time.setText(DateUtil.disposeDate(time));
        String address = clientBean.getProvince()+clientBean.getCity()+clientBean.getDist()+clientBean.getAddr();
        viewHolder.tv_address.setText(getXmlString(R.string.address_colon)+address);
        showStatus(clientBean.getStatus(),viewHolder.tv_status);
        return view;
    }

    private class ViewHolder{
        private ImageView img;
        private TextView tv_status,tv_company_name,tv_link_name,tv_phone,tv_time,tv_address;
    }

    /**
     * 显示状态
     * @param index
     * @param tv
     */
    private void showStatus(int index,TextView tv){
        switch (index){
            case 1:
                tv.setText(R.string.contact);
                tv.setBackgroundResource(R.drawable.fillet_rectangle_blue_solid);
                break;
            case 2:
                tv.setText(R.string.intentional);
                tv.setBackgroundResource(R.drawable.fillet_rectangle_green_solid);
                break;
            case 3:
                tv.setText(R.string.sign);
                tv.setBackgroundResource(R.drawable.fillet_rectangle_red_solid);
                break;
        }
    }

    /**
     * 获取XML里面的对应String值
     * @param id
     * @return
     */
    private String getXmlString(int id){
        return XmlUtils.getString(context,id);
    }

}
