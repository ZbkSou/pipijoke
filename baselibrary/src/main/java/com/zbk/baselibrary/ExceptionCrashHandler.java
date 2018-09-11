package com.zbk.baselibrary;

import android.content.Context;
import android.util.Log;

/**
 * User: bkzhou
 * Date: 2018/9/11
 * Description: 单例异常捕捉 ,可以用双重检测或者静态内部类
 * 1.设置捕获异常类为本类
 * 1.拿到系统默认异常处理类，将异常交给他继续处理（保证系统正常的功能）
 * 1.捕获异常(异常信息，app 信息包名版本，手机信息)
 * 2.保存崩溃到内存卡
 * 3.上线后，重新打开上传内存卡中的崩溃信息到服务器
 */
public class ExceptionCrashHandler  implements Thread.UncaughtExceptionHandler{

//    private static  ExceptionCrashHandler mInstance;
    private static final String TAG = "ExceptionCrashHandler";
    //用来获取应用信息
    private Context context;
    //默认异常处理类
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;


    //静态内部类的实现
    private static class ExceptionCrashHandlerInstance {
        private static  final ExceptionCrashHandler mInstance = new ExceptionCrashHandler();
    }

    public static ExceptionCrashHandler getInstance(){
        //双重检测的实现
//        if(mInstance==null) {
//            synchronized (ExceptionCrashHandler.class){
//                if(mInstance==null) {
//                    mInstance = new ExceptionCrashHandler();
//                }
//            }
//        }
        return ExceptionCrashHandlerInstance.mInstance;
    }

    public void init(Context context){
        this.context = context;
        //设置全局的异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mDefaultExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e(TAG,"捕获异常");
        //


        //交给默认的继续处理
        mDefaultExceptionHandler.uncaughtException(thread,throwable);

    }
}
