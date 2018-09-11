package com.zbk.ppx.pipijoke;

import android.app.Application;

import com.zbk.baselibrary.ExceptionCrashHandler;

/**
 * User: bkzhou
 * Date: 2018/9/11
 * Description:
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //设置异常捕捉
        ExceptionCrashHandler.getInstance().init(this);
    }
}
