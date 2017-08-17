package com.ickkey.dzhousekeeper.net;

import android.text.TextUtils;

import com.ickkey.dzhousekeeper.App;
import com.ickkey.dzhousekeeper.utils.DialogUtils;
import com.ickkey.dzhousekeeper.utils.ToastUtils;


/**
 * Created by Administrator on 2017/7/25.
 */

public class CommonResponseListener<T> implements OnResponseListener<T> {
    @Override
    public void onSucceed(T t) {
        DialogUtils.closeProgressDialog();
    }

    @Override
    public void onError(String errorMsg) {
        DialogUtils.closeProgressDialog();
        if(!TextUtils.isEmpty(errorMsg)){
            ToastUtils.showShortToast(App.getInstance(),errorMsg);
        }

    }

}
