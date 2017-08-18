package com.ickkey.dzhousekeeper.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页Fragment
 * Created by wangbin11 on 2017/8/16.
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.btn_search)
    Button btn_search;
    @BindView(R.id.et_houseNO)
    EditText et_houseNO;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private SearchLocksResponse searchLocksResponse;

    private List<SearchLocksResponse.LockMsg> list;
    private HouseAdapter adapter;

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
                    btn_search.setBackground(getResources().getDrawable(R.drawable.bg_btn_enable));
                } else {
                    btn_search.setClickable(false);
                    btn_search.setBackground(getResources().getDrawable(R.drawable.bg_btn_disable));
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new HouseAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (searchLocksResponse != null) {
//                    if (searchLocksResponse.isfull) {
//                        ToastUtils.showLongToast(getActivity(), "房间已住满，您没权限进入");
//                    } else {
//                        Intent intent = new Intent(getActivity(), HouseActivity.class);
//                        intent.putExtra("LockMsg", list.get(position));
//                        startActivity(intent);
//                    }

                    Intent intent = new Intent(getActivity(), HouseActivity.class);
                    intent.putExtra("LockMsg", list.get(position));
                    startActivity(intent);
                }
            }
        });



    }

    @OnClick({R.id.btn_search})
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
                        super.onSucceed(obj);
                        if (obj != null && obj instanceof SearchLocksResponse) {
                            searchLocksResponse = (SearchLocksResponse) obj;
                            list = searchLocksResponse.msg;
                            adapter.setNewData(list);
                        }
                    }

                    @Override
                    public void onError(String errorMsg) {
                        super.onError(errorMsg);
                        adapter.setNewData(null);
                    }
                }, Urls.SEARCHLOCKS, SearchLocksResponse.class, getActivity(), searchLocksReq);
                break;
            case R.id.tv_house_detail:
                startActivity(new Intent(getActivity(), HouseActivity.class));
                break;
        }
    }


    private class HouseAdapter extends BaseQuickAdapter<SearchLocksResponse.LockMsg, BaseViewHolder> {

        private HouseAdapter(List<SearchLocksResponse.LockMsg> list) {
            super(R.layout.item_lock_layout, list);
        }

        @Override
        protected void convert(BaseViewHolder holder, SearchLocksResponse.LockMsg lockMsg) {

            if (lockMsg != null) {
                StringBuffer sb = new StringBuffer();
                sb.append(lockMsg.province).append(lockMsg.city).append(lockMsg.district).append(lockMsg.installAddress);

                TextView textView = holder.getView(R.id.tv_house_detail);
                textView.setText(sb.toString());
            }

        }
    }
}
