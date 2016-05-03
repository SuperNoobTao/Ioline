package zucc.edu.cn.ioline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import zucc.edu.cn.util.MyJazzyViewPager;

/**
 * Created by shentao on 2016/4/20.
 */
public class GuidanceActivity extends Activity implements ViewPager.OnPageChangeListener {
    protected static final String TAG = "GuidanceActivity";
    public static final String FIST_USER = "fist_user";
    private int[] mImgIds;
    private MyJazzyViewPager mViewPager;
    private boolean misScrolled =false;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        mImgIds = new int[] { R.drawable.guidance1, R.drawable.guidance2, R.drawable.guidance3};
        mViewPager = (MyJazzyViewPager) findViewById(R.id.id_viewPager);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(new PagerAdapter()
        {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1)
            {
                return arg0 == arg1;
            }
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object)
            {
                container.removeView((View) object);
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {
                ImageView imageView = new ImageView(GuidanceActivity.this);
                imageView.setImageResource(mImgIds[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                container.addView(imageView);
                mViewPager.setObjectForPosition(imageView, position);
                return imageView;
            }

            @Override
            public int getCount()
            {
                return mImgIds.length;
            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                misScrolled = false;
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                misScrolled = true;
                break;
            case ViewPager.SCROLL_STATE_IDLE:
//                Toast.makeText(this, "mViewPager.getCurrentItem():" + mViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "mViewPager.getAdapter().getCount() - 1:" + (mViewPager.getAdapter().getCount() - 1), Toast.LENGTH_SHORT).show();
                if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1 && !misScrolled) {

                    SharedPreferences sharedPreferences =
                            getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(FIST_USER, "false");
                    editor.commit();

                    startActivity(new Intent(this, Main_Activity.class));
                    GuidanceActivity.this.finish();

                }
                misScrolled = true;
                break;
        }

    }
}