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
import djs.com.happysharebuy.entity.UserBean;
import djs.com.happysharebuy.utils.ImageOption;
import djs.com.happysharebuy.utils.XmlUtils;

/**
 * Created by bin on 2017/7/14.
 * 排行榜的adapter
 */

public class RankingAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    private List<UserBean> users;
    public RankingAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    /**
     * 适配器添加数据
     * @param users
     */
    public void setData(List<UserBean> users){
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (users != null) {
            return users.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        return users.get(position);
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
            view = inflater.inflate(R.layout.ranking_item,null);
            viewHolder.avatar = (ImageView) view.findViewById(R.id.ranking_user_avatar);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.ranking_user_name);
            viewHolder.tv_score = (TextView) view.findViewById(R.id.ranking_user_score);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }

        UserBean userBean = users.get(position);

        viewHolder.tv_name.setText(userBean.getRealName());
        viewHolder.tv_score.setText(userBean.getNum()+" "+ XmlUtils.getString(context,R.string.home));

        x.image().bind(viewHolder.avatar,userBean.getTximg(), ImageOption.circularImageOptions());

        return view;
    }

    private class ViewHolder{
        private TextView tv_name,tv_score;
        private ImageView avatar;


    }
}
