package com.ickkey.dzhousekeeper.activity;


import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.R;
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

        App.getInstance().getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveNext(LoginActivity.class);
                finish();
            }
        }, 2000);
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
    public void onBackPressed() { }
}
