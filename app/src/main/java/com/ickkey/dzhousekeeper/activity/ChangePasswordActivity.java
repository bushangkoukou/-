package com.ickkey.dzhousekeeper.activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.request.UpdatePwdReq;
import com.ickkey.dzhousekeeper.net.response.BaseResponse;
import com.ickkey.dzhousekeeper.net.response.LoginResponse;
import com.ickkey.dzhousekeeper.utils.DialogUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码界面
 * Created by wangbin11 on 2017/8/18.
 */

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.ll_check)
    LinearLayout ll_check;
    @BindView(R.id.et_old_pw)
    EditText et_old_pw;
    @BindView(R.id.et_pw_once)
    EditText et_pw_once;
    @BindView(R.id.et_pw_twice)
    EditText et_pw_twice;
    @BindView(R.id.ll_change)
    LinearLayout ll_change;
    @BindView(R.id.btn_check)
    Button btn_check;
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
                InputMethodManager inputManager = (InputMethodManager) et_old_pw.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_old_pw, 0);
            }
        }, 600);

        et_old_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String oldPw = et_old_pw.getText().toString().trim();

                if (oldPw.isEmpty()) {
                    btn_check.setClickable(false);
                    btn_check.setBackground(getResources().getDrawable(R.drawable.bg_btn_disable));
                } else {
                    btn_check.setClickable(true);
                    btn_check.setBackground(getResources().getDrawable(R.drawable.bg_btn_enable));
                }
            }
        });

    }

    @OnClick({R.id.btn_check, R.id.btn_ok, R.id.btn_left_base})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check:
                if (et_old_pw.getText().toString().trim().equals(App.getInstance().getUserInfo().pwd)) {
                    step2();
                } else {
                    showToast("密码不正确");
                }
                break;

            case R.id.btn_ok:
                if (et_pw_once.getText().toString().trim().length() < 6 || et_pw_twice.getText().toString().trim().length() < 6) {
                    showToast(getString(R.string.pwd_too_short));
                    return;
                }
                if (!et_pw_once.getText().toString().trim().equals(et_pw_twice.getText().toString().trim())) {
                    showToast(getString(R.string.pwd_confirm_error));
                    return;
                }
                DialogUtils.showProgressDialog(mContext);
                UpdatePwdReq updatePwdReq = new UpdatePwdReq();
                updatePwdReq.password = et_pw_twice.getText().toString().trim();
                updatePwdReq.token = App.getInstance().getUserInfo().token;
                updatePwdReq.userId = App.getInstance().getUserInfo().userId;
                NetEngine.getInstance().sendUpdatePwdRequest(mContext, new CommonResponseListener<BaseResponse>() {
                    @Override
                    public void onSucceed(BaseResponse baseResponse) {
                        super.onSucceed(baseResponse);
                        LoginResponse userInfo = App.getInstance().getUserInfo();
                        userInfo.pwd = et_pw_twice.getText().toString().trim();
                        App.getInstance().saveUserInfo(userInfo);
                        showToast("修改成功");
                        finish();
                    }
                }, tag, updatePwdReq);
                break;

            case R.id.btn_left_base:
                finish();
                break;
        }
    }

    private void step2() {
        ll_check.setVisibility(View.GONE);
        ll_change.setVisibility(View.VISIBLE);
        et_pw_once.setFocusable(true);
        et_pw_once.setFocusableInTouchMode(true);
        et_pw_once.requestFocus();

    }
}
