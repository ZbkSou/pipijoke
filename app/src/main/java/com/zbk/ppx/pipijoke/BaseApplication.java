package com.zbk.ppx.pipijoke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alipay.euler.andfix.patch.PatchManager;
import com.zbk.baselibrary.ExceptionCrashHandler;
import com.zbk.baselibrary.utils.AppUtils;

/**
 * User: bkzhou
 * Date: 2018/9/11
 * Description:
 */
public class BaseApplication extends Application {
    public static PatchManager mPatchManager;


    @Override
    public void onCreate() {
        super.onCreate();
        //设置异常捕捉
        ExceptionCrashHandler.getInstance().init(this);
        //出事化阿里修复
        mPatchManager = new PatchManager(this);
        //1初始化版本，设置当前的应用版本
        mPatchManager.init(AppUtils.getAppVersionName());
        //加载之前的 apatch包
        mPatchManager.loadPatch();

    }
}
