package com.ickkey.dzhousekeeper.net;

import android.content.Context;

import com.ickkey.dzhousekeeper.net.request.BaseRequest;
import com.ickkey.dzhousekeeper.net.response.BaseResponse;
import com.ickkey.dzhousekeeper.net.response.GetVerifyResp;
import com.ickkey.dzhousekeeper.net.response.LoginResponse;

/**
 * Created by Administrator on 2017/7/25.
 */

public class NetEngine extends BaseNetEngine {
    private static NetEngine instance = new NetEngine();

    public static NetEngine getInstance() {
        if (instance == null) {
            instance = new NetEngine();
        }
        return instance;
    }

    /**
     * 登录
     */
    public void sendLoginRequest(Context context, final OnResponseListener<LoginResponse> onResponseListener, String tag, BaseRequest... req) {
        sendPostRequest(Urls.LOGIN, context, LoginResponse.class, onResponseListener, tag, req);
    }

    /**
     * 获取验证码
     */
    public void sendGetVerifyRequest(Context context, final OnResponseListener<GetVerifyResp> onResponseListener, String tag, BaseRequest... req) {
        sendPostRequest(Urls.GET_VERIFY, context, GetVerifyResp.class, onResponseListener, tag, req);
    }

    /**
     * 注册
     */
    public void sendRegisterRequest(Context context, final OnResponseListener<BaseResponse> onResponseListener, String tag, BaseRequest... req) {
        sendPostRequest(Urls.REGISTER, context, BaseResponse.class, onResponseListener, tag, req);
    }


    /**
     * 修改密码
     */
    public void sendUpdatePwdRequest(Context context, final OnResponseListener<BaseResponse> onResponseListener, String tag, BaseRequest... req) {
        sendPostRequest(Urls.UPPWD, context, BaseResponse.class, onResponseListener, tag, req);
    }

    /**
     * 修改用户名
     */
    public void sendUpdateUserNameRequest(Context context, final OnResponseListener<BaseResponse> onResponseListener, String tag, BaseRequest... req) {
        sendPostRequest(Urls.UPUSERNAME, context, BaseResponse.class, onResponseListener, tag, req);
    }

    /**
     * 找回密码
     */
    public void sendFindPwdRequest(Context context, final OnResponseListener<BaseResponse> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.FIND_PWD,context,BaseResponse.class,onResponseListener,tag,req);
    }
}
