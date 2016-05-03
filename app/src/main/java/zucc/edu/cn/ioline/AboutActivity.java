package zucc.edu.cn.ioline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2016/4/23.
 */
public class AboutActivity extends BaseActivity{
    private Button orBack;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.about_activity);
        orBack = (Button) findViewById(R.id.or_back);
    }
    @Override
    protected void setListener() {
        orBack.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.or_back:
                finish();
                break;
        }
    }
}
