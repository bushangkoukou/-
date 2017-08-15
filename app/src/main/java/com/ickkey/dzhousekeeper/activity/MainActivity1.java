package com.ickkey.dzhousekeeper.activity;

import android.os.Bundle;

import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.fragment.login.LaunchFragment;
import com.ickkey.dzhousekeeper.fragment.login.LoginFragment;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity1 extends SupportActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadRootFragment(R.id.fl_container, LoginFragment.newInstance(LaunchFragment.class));

    }


    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }
}
