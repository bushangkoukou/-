package com.ickkey.dzhousekeeper.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.Urls;
import com.ickkey.dzhousekeeper.net.request.BaseRequest;
import com.ickkey.dzhousekeeper.net.request.GetLocksPwdsReq;
import com.ickkey.dzhousekeeper.net.response.GetLocksPwdsResponse;
import com.ickkey.dzhousekeeper.net.response.GetLocksResponse;
import com.ickkey.dzhousekeeper.net.response.SearchLocksResponse;
import com.ickkey.dzhousekeeper.utils.DialogUtils;
import com.ickkey.dzhousekeeper.utils.DisplayUtil;
import com.ickkey.dzhousekeeper.utils.ToastUtils;

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
    @BindView(R.id.rel_rooms)
    RelativeLayout rel_rooms;
    @BindView(R.id.ll_dots)
    LinearLayout ll_dots;
    @BindView(R.id.ll_lock_result)
    LinearLayout ll_lock_result;
    @BindView(R.id.ll_lock)
    LinearLayout ll_lock;
    private ViewPagerAdapter vpAdapter;

    // 底部小店图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    private int pageCount;

    private List<GetLocksResponse.LockMsg> lockMsgList;
    private GetLocksResponse getLocksResponse;

    private SearchLocksResponse.LockMsg lockMsg;

    private List<Integer> lockIds;

    private List<GetLocksPwdsResponse.LockPwd> lockPwds;

    @Override
    int getLayoutId() {
        return R.layout.activity_house_layout;
    }

    @Override
    void init() {
        tv_title_base.setText("房屋信息");

        lockMsg = (SearchLocksResponse.LockMsg) getIntent().getSerializableExtra("LockMsg");
        tv_houseNO.setText(lockMsg.houseNo);
        StringBuffer sb = new StringBuffer();
        sb.append(lockMsg.province).append(lockMsg.city).append(lockMsg.district).append(lockMsg.installAddress);
        tv_house_address.setText(sb.toString());
        tv_housekeeper.setText(App.getInstance().getUserInfo().username);

        initData();
    }

    private void initData() {
        BaseRequest request = new BaseRequest();
        request.userId = App.getInstance().getUserInfo().userId;
        request.token = App.getInstance().getUserInfo().token;

        DialogUtils.showProgressDialog(mContext);

        NetEngine.getInstance().getHttpResult(new CommonResponseListener() {
            @Override
            public void onSucceed(Object obj) {
                if (obj != null && obj instanceof GetLocksResponse) {
                    getLocksResponse = (GetLocksResponse) obj;
                    lockMsgList = getLocksResponse.msg;
                    if (lockMsgList != null && !lockMsgList.isEmpty()) {
                        initViewPager(lockMsgList);
                    }
                }
            }

        }, Urls.GETLOCKS, GetLocksResponse.class, mContext, request);
    }

    private void setLockPwdsView(List<GetLocksPwdsResponse.LockPwd> lockPwds) {

        if (lockPwds != null && !lockPwds.isEmpty()) {
            ll_lock.setVisibility(View.VISIBLE);
            ll_lock_result.removeAllViews();

            List<String> lockNOs = new ArrayList<>();

            for (GetLocksPwdsResponse.LockPwd lockPwd : lockPwds) {
                if (lockPwd.isCheckIn == 1) {
                    lockNOs.add(lockPwd.locksNo);
                } else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 10;
                    TextView tv_lockNO = new TextView(mContext);
                    tv_lockNO.setText("门牌编号: " + lockPwd.locksNo);
                    tv_lockNO.setTextColor(getResources().getColor(R.color.white));
                    tv_lockNO.setTextSize(16);
                    tv_lockNO.setLayoutParams(params);
                    ll_lock_result.addView(tv_lockNO);

                    TextView tv_lockPwd = new TextView(mContext);
                    tv_lockPwd.setText("密码: " + lockPwd.pwd);
                    tv_lockPwd.setTextColor(getResources().getColor(R.color.white));
                    tv_lockPwd.setTextSize(16);
                    tv_lockPwd.setLayoutParams(params);
                    ll_lock_result.addView(tv_lockPwd);
                }
            }

            if (!lockNOs.isEmpty()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("门牌编号为");
                for (int i = 0; i < lockNOs.size(); i++) {
                    stringBuffer.append(lockNOs.get(i));
                    if (i != lockNOs.size()-1) {
                        stringBuffer.append(",");
                    }
                }
                stringBuffer.append("的房间已经有租客入住，您没有权限申请钥匙");

                ToastUtils.showShortToast(mContext, stringBuffer.toString());
            }
        }

    }

    private void initViewPager(List<GetLocksResponse.LockMsg> list) {
        rel_rooms.setVisibility(View.VISIBLE);
        btn_getLock.setVisibility(View.VISIBLE);
        view_pager.removeAllViews();

        lockIds = new ArrayList<>();

        int count = list.size();
        pageCount = count % 3 == 0 ? count / 3 : count / 3 + 1;

        if (pageCount > 1) {
            initDots(pageCount);
        }

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

                final GetLocksResponse.LockMsg lockMsg = list.get(i);

                final LinearLayout menu = new LinearLayout(mContext);
                LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
                menu.setOrientation(LinearLayout.VERTICAL);
                menuParams.gravity = Gravity.CENTER_HORIZONTAL;
                TextView tv_status = new TextView(mContext);
                tv_status.setText(lockMsg.isOnlie == 1 ? "在线" : "离线");
                tv_status.setTextColor(lockMsg.isOnlie == 1 ?
                        getResources().getColor(R.color.green_font)
                        : getResources().getColor(R.color.grey_font));
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
                iv_room.setImageResource(lockMsg.isOnlie == 1 ? R.drawable.ic_house_on : R.drawable.ic_house_off);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                iv_room.setLayoutParams(params);

                frameLayout.addView(iv_room);

                ImageView iv_key = new ImageView(mContext);
                iv_key.setId(R.id.iv_key);
                iv_key.setImageResource(R.drawable.ic_room_selected);
                iv_key.setVisibility(View.INVISIBLE);
                FrameLayout.LayoutParams iv_keyParams = new FrameLayout.LayoutParams(DisplayUtil.dp2px(mContext, 26), DisplayUtil.dp2px(mContext, 56));
                iv_keyParams.gravity = Gravity.CENTER;

                frameLayout.addView(iv_key, iv_keyParams);

                menu.addView(frameLayout, flParams);

                TextView tv_name = new TextView(mContext);
                tv_name.setText(lockMsg.roomNo);
                tv_name.setTextColor(getResources().getColor(R.color.green_font));
                tv_name.setTextSize(16);
                tv_name.setSingleLine();
                LinearLayout.LayoutParams tvNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvNameParams.gravity = Gravity.CENTER_HORIZONTAL;
                menu.addView(tv_name, tvNameParams);

                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView iv_key = (ImageView) v.findViewById(R.id.iv_key);
                        boolean isSelected = iv_key.getVisibility() == View.VISIBLE;
                        iv_key.setVisibility(isSelected ? View.GONE : View.VISIBLE);
                        if (!isSelected) {
                            lockIds.add(lockMsg.id);
                        } else {
                            if (lockIds.contains(lockMsg.id))
                                lockIds.remove((Integer) lockMsg.id);
                        }
                        if (lockIds.isEmpty()) {
                            btn_getLock.setBackground(getResources().getDrawable(R.drawable.bg_btn_disable));
                        } else {
                            btn_getLock.setBackground(getResources().getDrawable(R.drawable.bg_btn_enable));
                        }
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

    private void initDots(int pageCount) {
        ll_dots.setVisibility(View.VISIBLE);
        ImageView[] dots = new ImageView[pageCount];

        // 循环取得小点图片
        for (int i = 0; i < pageCount; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            dots[i] = (ImageView) ll_dots.getChildAt(i);
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

                if (!lockIds.isEmpty()) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < lockIds.size(); i++) {
                        sb.append(lockIds.get(i));
                        if (i != lockIds.size()-1) {
                            sb.append(",");
                        }
                    }

                    GetLocksPwdsReq getLocksPwdsReq = new GetLocksPwdsReq();
                    getLocksPwdsReq.locksIds = sb.toString();
                    getLocksPwdsReq.userId = App.getInstance().getUserInfo().userId;
                    getLocksPwdsReq.token = App.getInstance().getUserInfo().token;

                    NetEngine.getInstance().getHttpResult(new CommonResponseListener() {
                        @Override
                        public void onSucceed(Object obj) {
                            super.onSucceed(obj);
                            if (obj != null && obj instanceof GetLocksPwdsResponse) {
                                GetLocksPwdsResponse response = (GetLocksPwdsResponse) obj;
                                lockPwds = response.msg;
                                setLockPwdsView(lockPwds);
                            }
                        }

                        @Override
                        public void onError(String errorMsg) {
                            super.onError(errorMsg);
                        }
                    }, Urls.GETLOCKSPWDS, GetLocksPwdsResponse.class, mContext, getLocksPwdsReq);

                }

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
        if (positon < 0 || positon > pageCount - 1 || currentIndex == positon) {
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
