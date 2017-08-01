package com.ickkey.dzhousekeeper.net;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ickkey.dzhousekeeper.utils.LogUtil;
import com.ickkey.dzhousekeeper.utils.ToastUtils;

/**
 * Created by Administrator on 2017/7/25.
 */

public  class CommonVolleyErrorListener implements Response.ErrorListener {
    private Context context;
    public CommonVolleyErrorListener(Context context){
        this.context=context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        LogUtil.info(getClass(),error.getMessage());
        if(context!=null){
            ToastUtils.showShortToast(context,"网络错误");
        }
    }
}