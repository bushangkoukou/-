package com.ickkey.dzhousekeeper.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.BuildConfig;
import com.ickkey.dzhousekeeper.R;
import com.ickkey.dzhousekeeper.net.CommonResponseListener;
import com.ickkey.dzhousekeeper.net.NetEngine;
import com.ickkey.dzhousekeeper.net.Urls;
import com.ickkey.dzhousekeeper.net.request.GetAppVersionReq;
import com.ickkey.dzhousekeeper.net.request.LoginReq;
import com.ickkey.dzhousekeeper.net.response.GetAppVersionResp;
import com.ickkey.dzhousekeeper.net.response.LoginResponse;
import com.ickkey.dzhousekeeper.service.DownloadApkService;
import com.ickkey.dzhousekeeper.utils.DialogUtils;
import com.ickkey.dzhousekeeper.view.WaveView;

import java.util.List;

import butterknife.BindView;

/**
 * 管家APP启动页
 * Created by bsqq on 2017/8/15.
 */

public class LaunchActivity extends BaseActivity {

    @BindView(R.id.waveView)
    WaveView waveView;

    @Override
    int getLayoutId() {
        return R.layout.activity_launch_layout;
    }

    @Override
    void init() {
        waveView.setWaterAlpha(1);
        waveView.changWater(0.8f);
        NetEngine.getInstance().getHttpResult(new CommonResponseListener<GetAppVersionResp>(){
            @Override
            public void onSucceed(final GetAppVersionResp getAppVersionResp) {
                super.onSucceed(getAppVersionResp);
                if(!getAppVersionResp.msg.androidVersion.equals(BuildConfig.VERSION_NAME)){
                    showUpdateDialog(getAppVersionResp);
                }else {
                    toLogin();
                }


            }
        }, Urls.GET_APP_VERSION,GetAppVersionResp.class,LaunchActivity.this,new GetAppVersionReq());





    }
    public  void toLogin(){
        if (App.getInstance().getUserInfo() == null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("-wb-", "moveNext(LoginActivity.class)");
                    moveNext(LoginActivity.class);
                    finish();
                }
            }, 3000);
        } else {

            if (System.currentTimeMillis() < Long.valueOf(App.getInstance().getUserInfo().tokenTimeOut)) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moveNext(MainActivity.class);
                        Log.d("-wb-", "moveNext(MainActivity.class)");
                        finish();
                    }
                }, 1500);
            } else {
                LoginReq loginReq = new LoginReq();
                loginReq.mobile = App.getInstance().getUserInfo().username;
                loginReq.password = App.getInstance().getUserInfo().pwd;
                NetEngine.getInstance().sendLoginRequest(mContext, new CommonResponseListener<LoginResponse>() {
                    @Override
                    public void onSucceed(LoginResponse loginResponse) {
                        super.onSucceed(loginResponse);
                        Log.d("-wb-", "moveNext(MainActivity.class)onSucceed");
                        loginResponse.tokenTimeOut = String.valueOf(System.currentTimeMillis() + loginResponse.expire * 1000);
                        App.getInstance().saveUserInfo(loginResponse);
                        moveNext(MainActivity.class);
                        finish();
                    }
                }, tag, loginReq);
            }
        }
    }
    public void showUpdateDialog(final GetAppVersionResp getAppVersionResp){
        AlertDialog dialog=  DialogUtils.showDialog(LaunchActivity.this, R.layout.dialog_version, false, new DialogUtils.CustomizeAction() {
            @Override
            public void setCustomizeAction(final AlertDialog dialog, View view) {
                ListView info_list= (ListView) view.findViewById(R.id.info_list);
                info_list.setAdapter(new UpdateAdapter(getAppVersionResp.msg.androidNotes));

                TextView btn_cancel= (TextView) view.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        toLogin();
                    }
                });
                TextView btn_update= (TextView) view.findViewById(R.id.btn_update);
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent=new Intent();
                        intent.setClass(LaunchActivity.this, DownloadApkService.class);
                        intent.putExtra("url",getAppVersionResp.msg.androidUrl);
                        LaunchActivity.this.startService(intent);

                    }
                });


            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    return true;

                return false;
            }
        });
    }
    @Override
    protected void onResume() {
        waveView.startWave();
        super.onResume();
    }

    @Override
    protected void onPause() {

        waveView.postDelayed(new Runnable() {
            @Override
            public void run() {
                waveView.stopWave();
            }
        }, 500);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        waveView.stopWave();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }
    private class UpdateAdapter extends BaseAdapter {
        List<String> infos;
        public UpdateAdapter(List<String> infos){
            this.infos=infos;
        }

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int i) {
            return infos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder  viewHolder;
            if(view==null){
                view=View.inflate(LaunchActivity.this,R.layout.item_update,null);
                viewHolder=new ViewHolder();
                viewHolder.tv= (TextView) view.findViewById(R.id.tv);
                view.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.tv.setText((i+1)+"、"+infos.get(i));
            return view;
        }
        private class ViewHolder{
            public TextView tv;
        }

    }
}
