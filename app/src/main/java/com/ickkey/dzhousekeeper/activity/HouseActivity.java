package com.ickkey.dzhousekeeper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.utils.DisplayUtil;

import java.util.ArrayList;
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

    private int pageCount;

    @Override
    int getLayoutId() {
        return R.layout.activity_house_layout;
    }

    @Override
    void init() {
        tv_title_base.setText("房屋信息");

        initViewPager();
        // 初始化底部小点
        initDots();
    }

    private void initViewPager() {
        view_pager.removeAllViews();

        int count = 5;

        pageCount = count % 3 == 0 ? count / 3 : count / 3 + 1;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        List<View> groups = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            LinearLayout group = new LinearLayout(mContext);
            LinearLayout.LayoutParams groupParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            group.setOrientation(LinearLayout.HORIZONTAL);
            group.setLayoutParams(groupParams);
            group.setPadding(0, 20, 0, 35);
            for (int j = i * 3; j < (i + 1) * 3 && j < count; j++) {
                final LinearLayout menu = new LinearLayout(mContext);
                LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
                menu.setOrientation(LinearLayout.VERTICAL);
                menuParams.gravity = Gravity.CENTER_HORIZONTAL;
                TextView tv_status = new TextView(mContext);
                tv_status.setText("离线");
                tv_status.setTextColor(getResources().getColor(R.color.green_font));
                tv_status.setTextSize(16);
                tv_status.setSingleLine();
                LinearLayout.LayoutParams tvStatusParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvStatusParams.gravity = Gravity.CENTER_HORIZONTAL;
                menu.addView(tv_status, tvStatusParams);


                FrameLayout frameLayout = new FrameLayout(mContext);
                LinearLayout.LayoutParams flParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                flParams.gravity = Gravity.CENTER_HORIZONTAL;
                flParams.setMargins(0, 15, 0, 15);

                ImageView iv_room = new ImageView(mContext);
                iv_room.setImageResource(R.drawable.ic_house_on);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                iv_room.setLayoutParams(params);

                frameLayout.addView(iv_room);

                ImageView iv_key = new ImageView(mContext);
                iv_key.setId(R.id.iv_key);
                iv_key.setImageResource(R.drawable.ic_room_selected);
                FrameLayout.LayoutParams iv_keyParams = new FrameLayout.LayoutParams(DisplayUtil.dp2px(mContext, 26), DisplayUtil.dp2px(mContext, 56));
                iv_keyParams.gravity = Gravity.CENTER;

                frameLayout.addView(iv_key, iv_keyParams);

                menu.addView(frameLayout, flParams);

                TextView tv_name = new TextView(mContext);
                tv_name.setText("room1");
                tv_name.setTextColor(getResources().getColor(R.color.green_font));
                tv_name.setTextSize(16);
                tv_name.setSingleLine();
                LinearLayout.LayoutParams tvNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvNameParams.gravity = Gravity.CENTER_HORIZONTAL;
                menu.addView(tv_name, tvNameParams);

//                menu.setBackgroundResource(R.drawable.item_selector);

                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView iv_key = (ImageView) v.findViewById(R.id.iv_key);
                        iv_key.setVisibility(iv_key.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                });
                group.addView(menu, menuParams);
            }
            groups.add(group);
        }

        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(groups);
        view_pager.setAdapter(vpAdapter);
        // 绑定回调
        view_pager.setOnPageChangeListener(this);

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

        List<View> menuGrops;

        public ViewPagerAdapter(List<View> menuGrops) {
            this.menuGrops = menuGrops;
        }

        @Override
        public int getCount() {
            if (menuGrops != null) {
                return menuGrops.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(menuGrops.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(menuGrops.get(position));
            return menuGrops.get(position);
        }
    }
}
