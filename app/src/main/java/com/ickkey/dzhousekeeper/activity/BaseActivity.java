package com.ickkey.dzhousekeeper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.utils.StackManager;
import com.ickkey.dzhousekeeper.utils.ToastUtils;

import java.util.UUID;

import butterknife.ButterKnife;

/**
 * App中所有Activity的基类
 * Created by wangbin11 on 2017/8/15.
 */

abstract class BaseActivity extends Activity implements View.OnClickListener {

    protected Context mContext;

    protected Handler mHandler;

    protected final String tag = getClass().getSimpleName() + UUID.randomUUID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mHandler = App.getInstance().getMainThreadHandler();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        StackManager.getInstance().addActivity(this);
        init();
    }

    abstract int getLayoutId();

    abstract void init();

    void onViewClick(View v) {
    }

    @Override
    public void onClick(View v) {
        onViewClick(v);
    }

    protected void moveNext(Class clzz) {
        Intent intent = new Intent(mContext, clzz);
        startActivity(intent);
    }

    protected void showToast(String s) {
        ToastUtils.showShortToast(mContext, s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StackManager.getInstance().removeActivity(this);
    }
}
