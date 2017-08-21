package com.ickkey.dzhousekeeper.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

/**
 * Created by zhengyunguang on 2017/5/27.
 */


public class StackManager {
    private static final String TAG = "StackManager";
    /**
     * Activity栈
     */
    private Stack<WeakReference<Activity>> mActivityStack;

    private static StackManager mInstance = new StackManager();


    private StackManager() {
    }

    /***
     * 获得AppManager的实例
     *
     * @return AppManager实例
     */
    public static StackManager getInstance() {
        if (mInstance == null) {
            mInstance = new StackManager();
        }
        return mInstance;
    }


    /***
     * 栈中Activity的数
     *
     * @return Activity的数
     */
    public int stackSize() {
        if (mActivityStack == null)
            return 0;
        return mActivityStack.size();
    }

    /***
     * 获得Activity栈
     *
     * @return Activity栈
     */
    public Stack<WeakReference<Activity>> getStack() {
        return mActivityStack;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        WeakReference<Activity> wr = new WeakReference<>(activity);
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(wr);
    }


    /**
     * 将栈内Activity放到顶部
     *
     * @param activity
     */
    public void resumeActivity(Activity activity) {
        try {
            WeakReference<Activity> wr = new WeakReference<>(activity);
            if (mActivityStack == null) {
                mActivityStack = new Stack<>();
                mActivityStack.add(wr);
            } else {
                WeakReference<Activity> target = null;
                for (WeakReference<Activity> wf : mActivityStack) {
                    Activity tempActivity = wf.get();
                    if (tempActivity != null) {
                        if (tempActivity.getClass().equals(activity.getClass())) {
                            target = wf;
                            break;
                        }
                    }
                }
                if (target != null) {
                    int index = mActivityStack.indexOf(target);
                    if (index != mActivityStack.size() - 1) {
                        mActivityStack.remove(target);
                        mActivityStack.add(wr);
                    }
                } else {
                    mActivityStack.add(wr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除ac
     *
     * @param activity 弱引用的ac
     */
    public void removeActivity(Activity activity) {
        try {
            WeakReference<Activity> target = null;
            if (mActivityStack != null) {
                for (WeakReference<Activity> wf : mActivityStack) {
                    if(wf.get() != null){
                        if (wf.get().getClass().equals(activity.getClass())) {
                            target = wf;
                            break;
                        }
                    }
                }
            }
            if (target != null) {
                mActivityStack.remove(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 获取栈顶Activity（堆栈中最后一个压入的）
     *
     * @return Activity
     */
    public Activity getTopActivity() {
        Activity activity = null;
        try {
            if (mActivityStack != null && mActivityStack.lastElement() != null) {
                activity = mActivityStack.lastElement().get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activity;
    }

    /***
     * 通过class 获取栈顶Activity
     *
     * @param cls
     * @return Activity
     */
    public Activity getActivityByClass(Class<?> cls) {
        Activity return_activity = null;
        try {
            if (mActivityStack != null){
                for (WeakReference<Activity> activity : mActivityStack) {
                    if(activity.get() != null){
                        if (activity.get().getClass().equals(cls)) {
                            return_activity = activity.get();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return return_activity;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */

    public void killTopActivity() {
        try {
            WeakReference<Activity> activity = mActivityStack.lastElement();
            killActivity(activity);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /***
     * 结束指定的Activity
     *
     * @param activity
     */
    public void killActivity(WeakReference<Activity> activity) {
        try {
            Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                WeakReference<Activity> stackActivity = iterator.next();
                if (stackActivity.get() == null) {
                    iterator.remove();
                    continue;
                }
                if (stackActivity.get().getClass().getName().equals(activity.get().getClass().getName())) {
                    iterator.remove();
                    stackActivity.get().finish();
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    /***
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void killActivity(Class<?> cls) {
        try {
            ListIterator<WeakReference<Activity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity == null) {
                    listIterator.remove();
                    continue;
                }
                if (activity.getClass() == cls) {
                    listIterator.remove();
                    if (activity != null) {
                        activity.finish();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 结束所有Activity
     */
    public void killAllActivity() {
        try {
            if (mActivityStack != null) {
                ListIterator<WeakReference<Activity>> listIterator = mActivityStack.listIterator();
                while (listIterator.hasNext()) {
                    Activity activity = listIterator.next().get();
                    if (activity != null) {
                        activity.finish();
                    }
                    listIterator.remove();
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    /**
     * 移除除了某个activity的其他所有activity
     *
     * @param cls 界面
     */
    public void killAllActivityExceptOne(Class cls) {
        try {
            Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                WeakReference<Activity> stackActivity = iterator.next();
                Activity activity = stackActivity.get();
                if (activity == null) {
                    iterator.remove();
                    continue;
                }
                if (activity.getClass().equals(cls)) {
                    continue;
                }
                activity.finish();
                iterator.remove();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        killAllActivity();
        Process.killProcess(Process.myPid());
    }

}