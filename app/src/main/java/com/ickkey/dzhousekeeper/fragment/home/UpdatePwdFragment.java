package com.ickkey.dzhousekeeper.fragment.home;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.base.BaseFragment;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.request.UpdatePwdReq;
import com.ickkey.dzhousekeeper.net.response.BaseResponse;
import com.ickkey.dzhousekeeper.net.response.LoginResponse;
import com.ickkey.dzhousekeeper.utils.DialogUtils;
import com.ickkey.dzhousekeeper.view.UpdateEtWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hhj on 2017/8/1.
 */

public class UpdatePwdFragment extends BaseFragment {
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_pwd_new)
    EditText et_pwd_new;
    @BindView(R.id.et_pwd_confirm)
    EditText et_pwd_confirm;
    @BindView(R.id.btn_check)
    TextView btn_check;
    @BindView(R.id.btn_confirm)
    TextView btn_confirm;
    @BindView(R.id.ll_step1)
    LinearLayout ll_step1;
    @BindView(R.id.ll_step2)
    LinearLayout ll_step2;
    @Override
    public int getLayoutId() {
        return R.layout.fm_update_pwd;
    }

    @Override
    public void initView() {
        setTitle("修改登录密码");
        btn_confirm.setClickable(false);
        btn_check.setClickable(false);
        et_pwd.addTextChangedListener(new UpdateEtWatcher(btn_check));
        et_pwd_new.addTextChangedListener(new BetweenWatcher());
        et_pwd_confirm.addTextChangedListener(new BetweenWatcher());



    }
    public class BetweenWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(et_pwd_new.getText().toString().length()>0&&et_pwd_confirm.getText().toString().length()>0){
                btn_confirm.setClickable(true);
                btn_confirm.setBackgroundResource(R.drawable.update_btn_bg);
            }else {
                btn_confirm.setClickable(false);
                btn_confirm.setBackgroundResource(R.drawable.update_btn_bg_disable);
            }

        }
    }



    @OnClick({R.id.btn_confirm,R.id.btn_check})
    public void onClick(View v){
            switch (v.getId()){
                case R.id.btn_confirm:
                    if(et_pwd_new.getText().toString().length()<6||et_pwd_confirm.getText().toString().length()<6){
                        showToast(getString(R.string.pwd_too_short));
                        return;
                    }
                    if(!et_pwd_new.getText().toString().equals(et_pwd_confirm.getText().toString())){
                        showToast(getString(R.string.pwd_confirm_error));
                        return;
                    }
                    DialogUtils.showProgressDialog(_mActivity);
                    UpdatePwdReq updatePwdReq=new UpdatePwdReq();
                    updatePwdReq.password=et_pwd_confirm.getText().toString();
                    updatePwdReq.token= App.getInstance().getUserInfo().token;
                    updatePwdReq.userId= App.getInstance().getUserInfo().userId;
                    NetEngine.getInstance().sendUpdatePwdRequest(_mActivity,new CommonResponseListener<BaseResponse>(){
                        @Override
                        public void onSucceed(BaseResponse baseResponse) {
                            super.onSucceed(baseResponse);
                            LoginResponse userInfo= App.getInstance().getUserInfo();
                            userInfo.pwd=et_pwd_confirm.getText().toString();
                            App.getInstance().saveUserInfo(userInfo);
                            showToast("修改成功");
                            pop();
                        }
                    },fragment_tag,updatePwdReq);
                    break;
                case R.id.btn_check:
                    if(et_pwd.getText().toString().equals(App.getInstance().getUserInfo().pwd)){
                        step2();
                    }else {
                        showToast("密码不正确");
                    }
                    break;
            }
    }
    public void  step2(){
        ll_step1.setVisibility(View.GONE);
        ll_step2.setVisibility(View.VISIBLE);


    }
}
