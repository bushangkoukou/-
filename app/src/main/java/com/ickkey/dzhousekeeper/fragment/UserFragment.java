package com.ickkey.dzhousekeeper.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.activity.ChangePasswordActivity;
import com.ickkey.dzhousekeeper.activity.LoginActivity;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.request.UpdateUserNameReq;
import com.ickkey.dzhousekeeper.net.response.BaseResponse;
import com.ickkey.dzhousekeeper.net.response.LoginResponse;
import com.ickkey.dzhousekeeper.utils.ToastUtils;

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

        LoginResponse userInfo = App.getInstance().getUserInfo();
        if (userInfo != null) {
            tv_userName.setText(userInfo.username);
        }

    }

    @OnClick({R.id.btn_logout, R.id.rel_changeUserName, R.id.rel_changePassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                App.getInstance().logOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.rel_changeUserName:
                showEditDialog();
                break;

            case R.id.rel_changePassword:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;

        }
    }

    private void showEditDialog() {
        final EditText editText = new EditText(getActivity());
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 15;
        params.rightMargin = 15;
        editText.setLayoutParams(params);

        new AlertDialog.Builder(getActivity()).setTitle("修改昵称")
                .setView(editText).setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String toBeUserName = editText.getText().toString().trim();
                        if (!toBeUserName.isEmpty()) {

                            UpdateUserNameReq updateUserNameReq = new UpdateUserNameReq();
                            updateUserNameReq.token = App.getInstance().getUserInfo().token;
                            updateUserNameReq.userId = App.getInstance().getUserInfo().userId;
                            updateUserNameReq.username = toBeUserName;
                            NetEngine.getInstance().sendUpdateUserNameRequest(getActivity(), new CommonResponseListener<BaseResponse>() {
                                @Override
                                public void onSucceed(BaseResponse baseResponse) {
                                    super.onSucceed(baseResponse);
                                    App.getInstance().getUserInfo().username = toBeUserName;
                                    App.getInstance().saveUserInfo(App.getInstance().getUserInfo());
                                    tv_userName.setText(toBeUserName);
                                    ToastUtils.showShortToast(getActivity(), "修改成功");
                                }
                            }, "1", updateUserNameReq);
                        } else {
                            ToastUtils.showShortToast(getActivity(), "请输入要修改的昵称");
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }).show();
    }
}
