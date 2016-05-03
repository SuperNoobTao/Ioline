package zucc.edu.cn.ioline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.lzy.ui.AlphaIndicator;

import java.util.ArrayList;
import java.util.List;

import zucc.edu.cn.adapter.MainAdapter;
import zucc.edu.cn.fragment.Tab01Fragment;
import zucc.edu.cn.fragment.Tab02Fragment;
import zucc.edu.cn.fragment.Tab02Fragment_qlv;
import zucc.edu.cn.fragment.Tab04Fragment_root;

/**
 * Created by Administrator on 2016/3/21.
 */
public class Main_Activity extends AppCompatActivity
{
    private List<Fragment> mFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        init();
    }
    private void init() {
        mFragments=new ArrayList<Fragment>();
        Fragment mTab01= new Tab01Fragment();
        Fragment mTab02= new Tab02Fragment();
        Fragment mTab04= new Tab04Fragment_root();
        mFragments.add(mTab01);
        mFragments.add(mTab02);
        mFragments.add(mTab04);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager(),mFragments));
        AlphaIndicator alphaIndicator = (AlphaIndicator) findViewById(R.id.alphaIndicator);
        alphaIndicator.setViewPager(viewPager);
    }
}
