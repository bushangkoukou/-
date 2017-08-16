package com.ickkey.dzhousekeeper.activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ickkey.dzhousekeeper.R;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 房屋界面
 * Created by wangbin11 on 2017/8/16.
 */

public class HouseActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.tv_houseNO)
    TextView tv_houseNO;
    @BindView(R.id.tv_house_address)
    TextView tv_house_address;
    @BindView(R.id.tv_housekeeper)
    TextView tv_housekeeper;
    @BindView(R.id.btn_left_base)
    RippleView btn_left_base;
    @BindView(R.id.tv_title_base)
    TextView tv_title_base;
    @BindView(R.id.btn_getLock)
    Button btn_getLock;

    private ViewPagerAdapter vpAdapter;
    private List<View> views;

    private static final int[] pics = { R.drawable.ic_logo,
            R.drawable.ic_logo};

    // 底部小店图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    int getLayoutId() {
        return R.layout.activity_house_layout;
    }

    @Override
    void init() {
        tv_title_base.setText("房屋信息");

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
//        for (int i = 0; i < pics.length; i++) {
//            ImageView iv = new ImageView(this);
//            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            iv.setLayoutParams(mParams);
//            iv.setImageResource(pics[i]);
//            if (i == pics.length - 1) {
//                iv.setOnClickListener(this);
//            }
//            views.add(iv);
//        }
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(mContext);
        view_pager.setAdapter(vpAdapter);
        // 绑定回调
        view_pager.setOnPageChangeListener(this);

        // 初始化底部小点
        initDots();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    @OnClick({R.id.btn_left_base, R.id.btn_getLock})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left_base:
                finish();
                break;
            case R.id.btn_getLock:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 设置底部小点选中状态
        setCurDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    public class ViewPagerAdapter extends PagerAdapter {

        Context mContext;

        public ViewPagerAdapter(Context context) {

            mContext = context;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = getLayoutInflater().inflate(R.layout.item_viewpager_layout, container, false);
            container.addView(view);
            return view;
        }
    }
}
