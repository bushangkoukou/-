package com.ickkey.dzhousekeeper.net.response;

/**
 * Created by Administrator on 2017/7/25.
 */

public class LoginResponse extends BaseResponse{
    public int expire;
    public String token;
    public String headUrl;
    public String username;
    public String userId;
    //本地存储
    public String pwd;
    public String tokenTimeOut;
    public String mobile;
}
