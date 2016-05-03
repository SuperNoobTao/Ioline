package zucc.edu.cn.ioline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/4/23.
 */
public class UpdatePwd extends BaseActivity{
    private Button backbut;
    private EditText oldpwdEt;
    private EditText newpwdEt;
    private EditText reNewpwdEt;
    private Button fedbackButton;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.updatepwd_activiy);
        backbut = (Button) findViewById(R.id.backbut);
        oldpwdEt = (EditText) findViewById(R.id.oldpwd_et);
        newpwdEt = (EditText) findViewById(R.id.newpwd_et);
        reNewpwdEt = (EditText) findViewById(R.id.re_newpwd_et);
        fedbackButton = (Button) findViewById(R.id.fedback_button);
    }

    @Override
    protected void setListener() {
        backbut.setOnClickListener(this);
        fedbackButton.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbut:
                finish();
                break;
            case R.id.fedback_button:
                break;
        }
    }

}
