package com.ickkey.dzhousekeeper.activity;


import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.request.LoginReq;
import com.ickkey.dzhousekeeper.net.response.LoginResponse;
import com.ickkey.dzhousekeeper.view.WaveView;

import butterknife.BindView;

/**
 * 管家APP启动页
 * Created by bsqq on 2017/8/15.
 */

public class LaunchActivity extends BaseActivity {

    @BindView(R.id.waveView)
    WaveView waveView;

    @Override
    int getLayoutId() {
        return R.layout.activity_launch_layout;
    }

    @Override
    void init() {

        waveView.setWaterAlpha(1);
        waveView.changWater(0.8f);

        if (userInfo == null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveNext(LoginActivity.class);
                    finish();
                }
            }, 3000);
        } else {

            if (System.currentTimeMillis() < Long.valueOf(userInfo.tokenTimeOut)) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moveNext(MainActivity.class);
                        finish();
                    }
                }, 1500);
            } else {
                LoginReq loginReq = new LoginReq();
                loginReq.mobile = userInfo.username;
                loginReq.password = userInfo.pwd;
                NetEngine.getInstance().sendLoginRequest(mContext, new CommonResponseListener<LoginResponse>() {
                    @Override
                    public void onSucceed(LoginResponse loginResponse) {
                        super.onSucceed(loginResponse);
                        loginResponse.tokenTimeOut = String.valueOf(System.currentTimeMillis() + loginResponse.expire * 1000);
                        App.getInstance().saveUserInfo(loginResponse);
                        moveNext(MainActivity.class);
                        finish();
                    }
                }, tag, loginReq);
            }
        }

    }

    @Override
    protected void onResume() {
        waveView.startWave();
        super.onResume();
    }

    @Override
    protected void onPause() {

        waveView.postDelayed(new Runnable() {
            @Override
            public void run() {
                waveView.stopWave();
            }
        }, 500);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        waveView.stopWave();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }
}
