package com.ickkey.dzhousekeeper.net.response;

import java.io.Serializable;
import java.util.List;

/**
 * 获取锁
 * Created by Administrator on 2017/7/25.
 */

public class GetLocksResponse implements Serializable {

    public List<LockMsg> msg;
    public int code;

    public class LockMsg implements Serializable {

        public int id;
        public int installid;
        public String gatewayNo;
        public String pwd;
        public String gatewayValidNo;
        public String locksNo;
        public String locksValidNo;
        public String roomNo;
        public String province;
        public String city;
        public String district;
        public String installAddress;
        public String houseNumber;
        public String houseNo;
        public int isOnlie;
        public int quantity;
        public int status;
        public String createTime;

    }

}
