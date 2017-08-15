package com.ickkey.dzhousekeeper.activity;

import android.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.fragment.HomeFragment;
import com.ickkey.dzhousekeeper.fragment.UserFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主界面
 * Created by wangbin11 on 2017/8/16.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;
    @BindView(R.id.tv_home)
    TextView tv_home;
    @BindView(R.id.tv_user)
    TextView tv_user;

    HomeFragment fragment_home;

    UserFragment fragment_user;

    @Override
    int getLayoutId() {
        return R.layout.activity_main_layout;
    }

    @Override
    void init() {
        onClick(tv_home);
    }

    @OnClick({R.id.tv_home, R.id.tv_user})
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (v.getId()) {
            case R.id.tv_home:
                tv_home.setSelected(true);
                tv_user.setSelected(false);
                if(fragment_home==null){
                    fragment_home = new HomeFragment();
                    transaction.add(R.id.fragment_container, fragment_home);
                }else{
                    transaction.show(fragment_home);
                }
                break;
            case R.id.tv_user:
                tv_user.setSelected(true);
                tv_home.setSelected(false);
                if(fragment_user==null){
                    fragment_user = new UserFragment();
                    transaction.add(R.id.fragment_container,fragment_user);
                }else{
                    transaction.show(fragment_user);
                }
                break;
            default:
                break;
        }

        transaction.commit();
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(fragment_home!=null){
            transaction.hide(fragment_home);
        }
        if(fragment_user!=null){
            transaction.hide(fragment_user);
        }
    }

}
