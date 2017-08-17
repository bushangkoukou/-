package com.ickkey.dzhousekeeper.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ickkey.dzhousekeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人中心Fragment
 * Created by wangbin11 on 2017/8/16.
 */

public class UserFragment extends Fragment {

    @BindView(R.id.tv_title_base)
    TextView tv_title_base;
    @BindView(R.id.btn_left_base)
    RippleView btn_left_base;
    @BindView(R.id.btn_logout)
    Button btn_logout;
    @BindView(R.id.rel_changeUserName)
    RelativeLayout rel_changeUserName;
    @BindView(R.id.rel_changePassword)
    RelativeLayout rel_changePassword;
    @BindView(R.id.tv_userName)
    TextView tv_userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_layout, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        btn_left_base.setVisibility(View.INVISIBLE);
        tv_title_base.setText("个人中心");
    }

    @OnClick({R.id.btn_logout, R.id.rel_changeUserName, R.id.rel_changePassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:

                break;
            case R.id.rel_changeUserName:
                showEditDialog();
                break;

            case R.id.rel_changePassword:
                break;

        }
    }

    private void showEditDialog() {
        EditText editText = new EditText(getActivity());
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);

        new AlertDialog.Builder(getActivity()).setTitle("修改昵称")
                .setView(editText).setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
        }).show();
    }
}
