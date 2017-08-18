package com.ickkey.dzhousekeeper.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.activity.HouseActivity;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.Urls;
import com.ickkey.dzhousekeeper.net.request.SearchLocksReq;
import com.ickkey.dzhousekeeper.net.response.SearchLocksResponse;
import com.ickkey.dzhousekeeper.utils.DialogUtils;
import com.ickkey.dzhousekeeper.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页Fragment
 * Created by wangbin11 on 2017/8/16.
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.tv_house_detail)
    TextView tv_house_detail;
    @BindView(R.id.btn_search)
    Button btn_search;
    @BindView(R.id.et_houseNO)
    EditText et_houseNO;

    private SearchLocksResponse searchLocksResponse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        btn_search.setClickable(false);

        et_houseNO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_houseNO.getText().toString().length() > 0) {
                    btn_search.setClickable(true);
                }
            }
        });

    }

    @OnClick({R.id.tv_house_detail, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                String houseNO = et_houseNO.getText().toString();

                SearchLocksReq searchLocksReq = new SearchLocksReq();
                searchLocksReq.houseNumber = houseNO;
                searchLocksReq.token = App.getInstance().getUserInfo().token;
                searchLocksReq.userId = App.getInstance().getUserInfo().userId;
                DialogUtils.showProgressDialog(getActivity());

                NetEngine.getInstance().getHttpResult(new CommonResponseListener() {
                    @Override
                    public void onSucceed(Object obj) {
                        searchLocksResponse = (SearchLocksResponse) obj;
                        ToastUtils.showLongToast(getActivity(), searchLocksResponse.msg.size() + "");
                    }

                }, Urls.SEARCHLOCKS, SearchLocksResponse.class, getActivity(), searchLocksReq);
                break;
            case R.id.tv_house_detail:
                startActivity(new Intent(getActivity(), HouseActivity.class));
                break;
        }
    }
}
