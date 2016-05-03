package zucc.edu.cn.ioline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2016/3/29.
 */
public class IonlineStartActivity extends AppCompatActivity{
    private Handler handler=new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);

        String isFirstUse = null;
        SharedPreferences sharedPreferences =
                getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        isFirstUse = sharedPreferences.getString(GuidanceActivity.FIST_USER, "null");
        if(!isFirstUse.equals("null")&&!isFirstUse.equals(null)){
            handler.postDelayed(new Runnable(){
                public void run() {
                    startActivity(new Intent(IonlineStartActivity.this,Main_Activity.class));
                    finish();
                }

            },1800);
        }
        else{
            startActivity(new Intent(IonlineStartActivity.this,GuidanceActivity.class));
            finish();
        }

    }

}
