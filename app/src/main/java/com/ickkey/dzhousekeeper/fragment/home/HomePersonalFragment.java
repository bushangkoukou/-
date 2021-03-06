package com.ickkey.dzhousekeeper.fragment.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ickkey.dzhousekeeper.ConstantValue;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.base.BaseFragment;
import com.ickkey.dzhousekeeper.fragment.gesture.GestureLoginFragment;
import com.ickkey.dzhousekeeper.fragment.login.LoginFragment;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.request.UpdateUserNameReq;
import com.ickkey.dzhousekeeper.net.response.BaseResponse;
import com.ickkey.dzhousekeeper.utils.DialogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hhj on 2017/7/27.
 */

public class HomePersonalFragment extends BaseFragment {
    @BindView(R.id.tv_username)
    TextView tv_username;

    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    HomeFragment homeFragment;
    @Override
    public int getLayoutId() {
        return R.layout.fm_home_personal;
    }

    @Override
    public void initView() {
        btn_left_base.setVisibility(View.INVISIBLE);
        setTitle("个人中心");

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        tv_username.setText(App.getInstance().getUserInfo().username);
    }

    @OnClick({R.id.rl_update_username,R.id.rl_update_pwd,R.id.rl_update_gesture_pwd,R.id.btn_exit})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_update_username:
                DialogUtils.showDialog(_mActivity, R.layout.dialog_update_username, true, new DialogUtils.CustomizeAction() {
                    @Override
                    public void setCustomizeAction(final AlertDialog dialog, View view) {
                        final EditText et_username= (EditText) view.findViewById(R.id.et_username);
                        et_username.setHint(App.getInstance().getUserInfo().username);
                        TextView btn_cancel= (TextView) view.findViewById(R.id.btn_cancel);
                        TextView btn_confirm= (TextView) view.findViewById(R.id.btn_confirm);
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        btn_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(TextUtils.isEmpty(et_username.getText().toString())){
                                    showToast("请输入用户名");
                                    return;
                                }
                                DialogUtils.showProgressDialog(_mActivity);
                                UpdateUserNameReq updateUserNameReq=new UpdateUserNameReq();
                                updateUserNameReq.token= App.getInstance().getUserInfo().token;
                                updateUserNameReq.userId= App.getInstance().getUserInfo().userId;
                                updateUserNameReq.username=et_username.getText().toString();
                                NetEngine.getInstance().sendUpdateUserNameRequest(_mActivity,new CommonResponseListener<BaseResponse>(){
                                    @Override
                                    public void onSucceed(BaseResponse baseResponse) {
                                        super.onSucceed(baseResponse);
                                        App.getInstance().getUserInfo().username=et_username.getText().toString();
                                        App.getInstance().saveUserInfo( App.getInstance().getUserInfo());
                                        tv_username.setText(App.getInstance().getUserInfo().username);
                                        dialog.dismiss();
                                    }
                                },fragment_tag,updateUserNameReq);


                            }
                        });

                    }
                });
                break;
            case R.id.rl_update_pwd:
                homeFragment.start(UpdatePwdFragment.newInstance(UpdatePwdFragment.class));
                break;
            case R.id.rl_update_gesture_pwd:
                Bundle bundle=new Bundle();
                bundle.putInt(ConstantValue.GESTURE_PAGER_TYPE,ConstantValue.GESTURE_HANDLE_UPDATE);
                homeFragment.start(GestureLoginFragment.newInstance(GestureLoginFragment.class,bundle));
                break;
            case R.id.btn_exit:
                DialogUtils.showInfoDialog(_mActivity, "确认退出登录吗", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        showToast("确认");
                        App.getInstance().logOut();
                        homeFragment.startWithPop(LoginFragment.newInstance(LoginFragment.class));

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                break;
        }


    }
}
