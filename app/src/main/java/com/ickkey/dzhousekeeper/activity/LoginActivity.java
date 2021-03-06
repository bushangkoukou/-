package com.ickkey.dzhousekeeper.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.ConstantValue;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.request.LoginReq;
import com.ickkey.dzhousekeeper.net.response.LoginResponse;
import com.ickkey.dzhousekeeper.utils.DialogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 管家登录界面
 * Created by bsqq on 2017/8/15.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_pwd)
    EditText et_pwd;

    @Override
    int getLayoutId() {
        return R.layout.activity_login_layout;
    }

    @Override
    void init() {
    }

    @OnClick({R.id.tv_register, R.id.tv_forget_pwd, R.id.btn_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                moveNext(RegisterActivity.class);
                break;
            case R.id.btn_login:
                if (TextUtils.isEmpty(et_username.getText().toString())) {
                    showToast("请输入用户名");
                    return;

                }
                if (TextUtils.isEmpty(et_pwd.getText().toString())) {
                    showToast("请输入密码");
                    return;

                }
                if (et_pwd.getText().toString().trim().length() < 6) {
                    showToast(getString(R.string.pwd_too_short));
                    return;

                }
                LoginReq loginReq = new LoginReq();
                loginReq.mobile = et_username.getText().toString();
                loginReq.password = et_pwd.getText().toString();
                DialogUtils.showProgressDialog(mContext);
                NetEngine.getInstance().sendLoginRequest(mContext, new CommonResponseListener<LoginResponse>() {
                    @Override
                    public void onSucceed(LoginResponse loginResponse) {
                        super.onSucceed(loginResponse);
                        loginResponse.pwd = et_pwd.getText().toString();
                        loginResponse.tokenTimeOut = String.valueOf(System.currentTimeMillis() + loginResponse.expire * 1000);
                        App.getInstance().setPwd(et_pwd.getText().toString());
                        App.getInstance().saveUserInfo(loginResponse);
                        showToast("登录成功");
                        moveNext(MainActivity.class);
                        finish();
                    }
                }, tag, loginReq);


                break;
            case R.id.tv_forget_pwd:
                moveNext(ResetPasswordActivity.class);
                break;
        }


    }
}
