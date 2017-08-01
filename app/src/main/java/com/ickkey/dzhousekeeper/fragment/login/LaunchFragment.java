package com.ickkey.dzhousekeeper.fragment.login;

import android.os.Bundle;

import com.ickkey.dzhousekeeper.ConstantValue;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.base.BaseFragment;
import com.ickkey.dzhousekeeper.fragment.gesture.CreateGestureFragment;
import com.ickkey.dzhousekeeper.fragment.gesture.GestureLoginFragment;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.request.LoginReq;
import com.ickkey.dzhousekeeper.net.response.LoginResponse;

import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by hhj on 2017/8/1.
 */

public class LaunchFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fm_launch;
    }

    @Override
    public void initView() {
        setFragmentAnimator(new FragmentAnimator(R.anim.anim_fade_in,R.anim.anim_fade_out,R.anim.anim_fade_in,R.anim.anim_fade_out));
        setTitleGone();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(App.getInstance().getUserInfo()==null){
                    startWithPop(LoginFragment.newInstance(LoginFragment.class));
                }else {
                    if(System.currentTimeMillis()<Long.valueOf(App.getInstance().getUserInfo().tokenTimeOut)){
                        goOn();
                    }else {
                        LoginReq loginReq=new LoginReq();
                        loginReq.mobile= App.getInstance().getUserInfo().username;
                        loginReq.password= App.getInstance().getUserInfo().pwd;
                        NetEngine.getInstance().sendLoginRequest(_mActivity,new CommonResponseListener<LoginResponse>(){
                            @Override
                            public void onSucceed(LoginResponse loginResponse) {
                                super.onSucceed(loginResponse);
                                loginResponse.tokenTimeOut=String.valueOf(System.currentTimeMillis()+loginResponse.expire*1000);
                                App.getInstance().saveUserInfo(loginResponse);
                                goOn();

                            }
                        },fragment_tag,loginReq);
                    }



                }
            }
        },200);

    }
    public void goOn(){
        Bundle bundle=new Bundle();
        bundle.putInt(ConstantValue.GESTURE_PAGER_TYPE,ConstantValue.GESTURE_HANDLE_LOGIN_IN);
        if(App.getInstance().getCache().getAsBinary(ConstantValue.GESTURE_PASSWORD)!=null){
            startWithPop(GestureLoginFragment.newInstance(GestureLoginFragment.class,bundle));
        }else {
            startWithPop(CreateGestureFragment.newInstance(CreateGestureFragment.class,bundle));
        }
    }
}
