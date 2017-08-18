package com.ickkey.dzhousekeeper;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.ickkey.dzhousekeeper.net.response.LoginResponse;
import com.ickkey.dzhousekeeper.utils.cache.ACache;


/**
 * Created by Administrator on 2017/7/25.
 */

public class App extends MultiDexApplication {
    public Handler getMainThreadHandler() {
        return handler;
    }


    private Handler handler = new Handler(Looper.getMainLooper());

    public static App getInstance() {
        return instance;
    }

    private static App instance;

    public ACache getCache() {
        return aCache;
    }

    private ACache aCache;
    private LoginResponse userInfo;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        aCache = ACache.get(getInstance());
        CrashHandler.getInstance().init(getApplicationContext());
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public LoginResponse getUserInfo() {
        if (userInfo == null) {
            userInfo = (LoginResponse) aCache.getAsObject(ConstantValue.CACHE_KEY_TOKEN_USER_INFO);
        }
        return userInfo;

    }

    public void setPwd(String pwd){
        sharedPreferences.edit().putString("pwd",pwd).commit();
    }
    public String getPwd(){
        return sharedPreferences.getString("pwd",null);
    }

    public void saveUserInfo(LoginResponse userInfo) {
        this.userInfo = userInfo;
        aCache.put(ConstantValue.CACHE_KEY_TOKEN_USER_INFO, userInfo);

    }

    public void logOut() {
        aCache.clear();
        sharedPreferences.edit().clear().commit();

    }
}
