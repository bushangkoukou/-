package com.ickkey.dzhousekeeper.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.request.GetVerifyReq;
import com.ickkey.dzhousekeeper.net.request.RegisterReq;
import com.ickkey.dzhousekeeper.net.response.BaseResponse;
import com.ickkey.dzhousekeeper.net.response.GetVerifyResp;
import com.ickkey.dzhousekeeper.utils.DialogUtils;
import com.ickkey.dzhousekeeper.utils.ValidatorUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册页面
 * Created by wangbin11 on 2017/8/15.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.ll_phone)
    LinearLayout ll_phone;
    @BindView(R.id.ll_pwd)
    LinearLayout ll_pwd;
    @BindView(R.id.ll_verify)
    LinearLayout ll_verify;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_verify)
    EditText et_verify;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_pwd_confirm)
    EditText et_pwd_confirm;

    @BindView(R.id.tv_step1)
    TextView tv_step1;
    @BindView(R.id.tv_step2)
    TextView tv_step2;
    @BindView(R.id.tv_step3)
    TextView tv_step3;


    @BindView(R.id.btn_submit_verify)
    TextView btn_submit_verify;

    @BindView(R.id.btn_register)
    TextView btn_register;
    @BindView(R.id.tv_title_base)
    TextView tv_title_base;
    @BindView(R.id.btn_left_base)
    RippleView btn_left_base;
    String verifyCode = "";

    @BindView(R.id.btn_verify)
    TextView btn_verify;

    @Override
    int getLayoutId() {
        return R.layout.activity_registe_layout;
    }

    @Override
    void init() {

        btn_verify.setClickable(false);
        btn_verify.setBackgroundResource(R.drawable.login_btn_bg_disable);

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    btn_verify.setClickable(true);
                    btn_verify.setBackgroundResource(R.drawable.login_btn_bg);
                } else {
                    btn_verify.setClickable(false);
                    btn_verify.setBackgroundResource(R.drawable.login_btn_bg_disable);
                }
            }
        });

        btn_register.setClickable(false);
        et_verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    btn_submit_verify.setClickable(true);
                    btn_submit_verify.setBackgroundResource(R.drawable.login_btn_bg);
                } else {
                    btn_submit_verify.setClickable(false);
                    btn_submit_verify.setBackgroundResource(R.drawable.login_btn_bg_disable);
                }

            }
        });
        et_pwd.addTextChangedListener(new BetweenWatcher());
        et_pwd_confirm.addTextChangedListener(new BetweenWatcher());

    }

    public class BetweenWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (et_pwd.getText().toString().length() > 0 && et_pwd_confirm.getText().toString().length() > 0) {
                btn_register.setClickable(true);
                btn_register.setBackgroundResource(R.drawable.login_btn_bg);
            } else {
                btn_register.setClickable(false);
                btn_register.setBackgroundResource(R.drawable.login_btn_bg_disable);
            }

        }
    }

    public void step2() {
        ll_phone.setVisibility(View.GONE);
        ll_verify.setVisibility(View.VISIBLE);
        tv_step1.setTextColor(getResources().getColor(R.color.global_tv_color));
        tv_step2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    public void step3() {
        ll_verify.setVisibility(View.GONE);
        ll_pwd.setVisibility(View.VISIBLE);
        tv_step2.setTextColor(getResources().getColor(R.color.global_tv_color));
        tv_step3.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @OnClick({R.id.btn_left_base, R.id.btn_verify, R.id.btn_submit_verify, R.id.btn_register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left_base:
                finish();
                break;
            case R.id.btn_verify:
                if (!ValidatorUtils.isMobile(et_phone.getText().toString())) {
                    showToast("手机号码不合法");
                    return;
                }
                DialogUtils.showProgressDialog(mContext);
                GetVerifyReq req = new GetVerifyReq();
                req.mobile = et_phone.getText().toString().trim();
                req.sendType = 1;
                NetEngine.getInstance().sendGetVerifyRequest(mContext, new CommonResponseListener<GetVerifyResp>() {
                    @Override
                    public void onSucceed(GetVerifyResp getVerifyResp) {
                        super.onSucceed(getVerifyResp);
                        showToast("验证码已发送，请注意查收");
                        verifyCode = getVerifyResp.phoneCode;
                        step2();
                    }
                }, tag, req);

                break;
            case R.id.btn_submit_verify:
                if (!et_verify.getText().toString().equals(verifyCode)) {
                    showToast("验证码不正确");
                    return;
                }
                step3();
                break;
            case R.id.btn_register:
                if (et_pwd.getText().toString().trim().length() < 6 || et_pwd_confirm.getText().toString().trim().length() < 6) {
                    showToast(getString(R.string.pwd_too_short));
                    return;
                }
                if (!et_pwd.getText().toString().trim().equals(et_pwd_confirm.getText().toString().trim())) {
                    showToast(getString(R.string.pwd_confirm_error));
                    return;
                }
                RegisterReq registerReq = new RegisterReq();
                registerReq.code = verifyCode;
                registerReq.regtype = 0;
                registerReq.mobile = et_phone.getText().toString().trim();
                registerReq.password = et_pwd.getText().toString().trim();
                DialogUtils.showProgressDialog(mContext);
                NetEngine.getInstance().sendRegisterRequest(mContext, new CommonResponseListener<BaseResponse>() {
                    @Override
                    public void onSucceed(BaseResponse registerResp) {
                        super.onSucceed(registerResp);
                        showToast("注册成功");
                        finish();
                    }
                }, tag, registerReq);
                break;
        }
    }
}
