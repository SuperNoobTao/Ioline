package zucc.edu.cn.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by QI on 2015/12/5.
 */
public class ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position)
    {
        this.mPosition=position;
        this.mViews=new SparseArray<View>();

        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context,View converView,ViewGroup parent,int layoutId,int position)
    {
        if(converView==null)
        {
            return new ViewHolder(context,parent,layoutId,position);
        }
        else
        {
            ViewHolder holder = (ViewHolder) converView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }
    /**
     * ͬ    ��viewId ��ȡ�ؼ�
     * */
    public <T extends View> T getView(int viewId)
    {
        View view=mViews.get(viewId);
        if(view==null)
        {
            view=mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T)view;

    }
    public View getmConvertView()
    {
        return mConvertView;
    }



    /***
     * ����textView ��ֵ
     * */
    public ViewHolder setText(int viewId,String text)
    {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置颜色 字
     * @author Administrator
     * @time 2016/3/29 21:55
     *
     */
    
    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }


    public ViewHolder setTextViewTextColor(int viewId,int color)
    {
        TextView tv = getView(viewId);
        tv.setTextColor(color);
        return this;
    }
    /**
     * ���� ImageView
     * */
    public ViewHolder setImageResource(int viewId,int resId)
    {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * �ȵ�
     * */
    public ViewHolder setImageBitamp(int viewId, Bitmap bitmap)
    {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setImageURI(int viewId, String uri)
    {
//        ImageView view = getView(viewId);
//        Imageloader.get
//        view.setImageURI(uri);
        return this;
    }

    public ViewHolder setPos(int viewId)
    {
        TextView tv = getView(viewId);
        tv.setText(mPosition);
        return this;
    }

//
//    public ViewHolder setFocusableRadioButton(int viewId, boolean arg)
//    {
//        RadioButton view = getView(viewId);
//        view.setFocusable(arg);
//        return this;
//    }
//
//    public ViewHolder setIdRadioButton(int viewId)
//    {
//        RadioButton view = getView(viewId);
//        view.setId(mPosition);
//        return this;
//    }
//    public ViewHolder setCheckedRadioButton(int viewId,int checkedIndex)
//    {
//        RadioButton view = getView(viewId);
//        view.setChecked(mPosition == checkedIndex);
//        return this;
//    }
//    public ViewHolder setOnCheckedChangeListener(int viewId, final int checkedIndex, final ListView listView)
//    {
//        RadioButton view = getView(viewId);
//
//        return this;
//    }






}
