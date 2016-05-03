package zucc.edu.cn.ioline;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.pedant.SweetAlert.SweetAlertDialog;
import zucc.edu.cn.util.ToastUtil;

/**
 * Created by Administrator on 2016/3/19.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected String TAG;
    private SweetAlertDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    /**
     *  初始化布局以及View控件
     * @author Administrator
     * @time 2016/3/19 21:58
     *
     */

    protected abstract   void initView(Bundle savedInstanceState);
    /**
     *给View控件添加事件监听器
     * @author Administrator
     * @time 2016/3/19 21:58
     *
     */

    protected abstract void setListener();

    /**处理业务逻辑，状态恢复等操作
     *
     * @author Administrator
     * @time 2016/3/19 21:58
     *
     */
    protected abstract void processLogic(Bundle savedInstanceState);




    /**
     * 需要处理点击事件时，重写该方法
     * @author Administrator
     * @time 2016/3/19 21:56
     *
     */
    public void onClick(View v) {

    }
    protected void showToast(String text) {
        ToastUtil.show(text);
    }
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText("数据加载中...");
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public boolean isLegal(String str) {
        return str != null && str.length() > 0;
    }

}
