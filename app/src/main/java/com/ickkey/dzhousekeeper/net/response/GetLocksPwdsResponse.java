package com.ickkey.dzhousekeeper.net.response;

import java.io.Serializable;
import java.util.List;

/**
 * 查询锁
 * Created by Administrator on 2017/7/25.
 */

public class GetLocksPwdsResponse implements Serializable {

    public List<LockPwd> msg;
    public int code;

    public class LockPwd implements Serializable {

        public int locksId;
        public int pwdId;
        public String pwd;
        public int isCheckIn;
        public String locksNo;

    }

}
