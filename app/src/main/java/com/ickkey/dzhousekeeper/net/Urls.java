package com.ickkey.dzhousekeeper.net;

/**
 * Created by hhj on 2017/7/27.
 */

public interface Urls {
    String BASE_URL = "http://ziru.ickkey.com";
    String LOGIN = BASE_URL + "/api/login";
    String GET_VERIFY = BASE_URL + "/api/verify";
    String REGISTER = BASE_URL + "/api/register";
    String UPPWD = BASE_URL + "/api/uppwd";
    String UPUSERNAME = BASE_URL + "/api/upusername";
    String SEARCHLOCKS = BASE_URL + "/api/searchlocks";
    String GETLOCKS = BASE_URL + "/api/getlocks";

    String FIND_PWD=BASE_URL+"/api/findpwd";
    String GETLOCKSPWDS = BASE_URL + "/api/getlockspwds";
    String GET_APP_VERSION=BASE_URL+"/api/version";
}
