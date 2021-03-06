package zucc.edu.cn.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by QI on 2015/12/14.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext ;
    protected List<T> mDatas ;
    protected LayoutInflater mInflater;
    private  int lv;
    public CommonAdapter(Context context, List<T> datas, int lv)
    {
        this.mContext=context;
        this.mDatas=datas;
        this.lv=lv;
        mInflater=LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, lv, position);
        convert(holder,getItem(position));


        return holder.getmConvertView();

    }

    public abstract void convert(ViewHolder holder,T t);
}
