package com.ickkey.dzhousekeeper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ickkey.dzhousekeeper.R;

/**
 * 个人中心Fragment
 * Created by wangbin11 on 2017/8/16.
 */

public class UserFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_layout, container, false);
        return view;
    }
}
