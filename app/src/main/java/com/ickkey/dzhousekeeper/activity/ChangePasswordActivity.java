package com.ickkey.dzhousekeeper.activity;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ickkey.dzhousekeeper.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码界面
 * Created by wangbin11 on 2017/8/18.
 */

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.et_old_pw)
    EditText et_old_pw;
    @BindView(R.id.et_pw_once)
    EditText et_pw_once;
    @BindView(R.id.et_pw_twice)
    EditText et_pw_twice;
    @BindView(R.id.ll_check)
    LinearLayout ll_check;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    @BindView(R.id.tv_title_base)
    TextView tv_title_base;
    @BindView(R.id.btn_left_base)
    RippleView btn_left_base;

    @Override
    int getLayoutId() {
        return R.layout.activity_changepassword_layout;
    }

    @Override
    void init() {
        tv_title_base.setText("修改登录密码");
        et_old_pw.setFocusable(true);
        et_old_pw.setFocusableInTouchMode(true);
        et_old_pw.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)et_old_pw.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_old_pw, 0);
            }
        },600);

    }

    @OnClick({R.id.btn_ok, R.id.btn_left_base})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                break;

            case R.id.btn_left_base:
                finish();
                break;
        }
    }
}
