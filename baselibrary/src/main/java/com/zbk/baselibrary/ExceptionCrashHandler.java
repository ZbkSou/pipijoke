package com.zbk.baselibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

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
    //緩存文件目錄
    private static final String CRASH_DIR_NAME = "crash";
    //緩存文件名
    private static final String CRASH_FILE_NAME = "CRASH_FILE_NAME";

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
        String crashFileName = saveInfoToSD(throwable);
        Log.e(TAG,"filename-->"+crashFileName);
        //記錄文件名
        cacheCrashFile(crashFileName);


        //交给默认的继续处理
        mDefaultExceptionHandler.uncaughtException(thread,throwable);

    }

    /**
     * 缓存崩溃日志文件
     *
     * @param fileName
     */
    private void cacheCrashFile(String fileName) {
        SharedPreferences sp = context.getSharedPreferences(CRASH_DIR_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(CRASH_FILE_NAME, fileName).commit();
    }


    private String saveInfoToSD(Throwable throwable) {
        String fileName = null;
        //用于组装缓存信息
        StringBuilder sb = new StringBuilder();
        //设备版本信息
        Map<String,String> devInfos = obtainSimpleInfo(context);
        //拼接到字符传
        for(Map.Entry<String,String> entry :devInfos.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        //异常信息
        String exInfo = obtainExceptionInfo(throwable);
        sb.append(exInfo);
        //保存到内部存储，这里不放到sd卡是为了跳过权限，都崩溃了还要啥权限申请
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//内部存储可用
            //创建文件夹
            File dir = new File(context.getFilesDir() + File.separator + CRASH_DIR_NAME + File.separator);
            //先删除之前的异常信息，每一次崩溃只保留一个异常信息
            if(dir.exists()){
                deleteDir(dir);
            }
            //如果不存在重新创建
            if(!dir.exists()){
                dir.mkdir();
            }
            try {
                fileName = dir.getAbsolutePath() + File.separator + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 得到一些设备和版本信息
     * @param context
     * @return
     */
    private Map<String, String> obtainSimpleInfo(Context context) {
        Map<String,String> map = new HashMap<>();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),PackageManager.GET_ACTIVITIES);
            map.put("versionName",packageInfo.versionName);
            map.put("versionCode", String.valueOf(packageInfo.versionCode));
            map.put("model", Build.MODEL);
            map.put("sdk_int", String.valueOf(Build.VERSION.SDK_INT));
            map.put("product",Build.PRODUCT);
            map.put("moble_info",getMobileInfo());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 得到异常信息
     * @param e
     * @return
     */
    private String obtainExceptionInfo(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        //将异常打印到输入流
        e.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * 获取Build的所有属性信息
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name).append("=").append(value).append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 清空文件夹
     * @param dir
     * @return
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()){
            String[] children = dir.list();
            //递归子目录
            for(int i=0; i<children.length; i++){
                boolean success = deleteDir(new File(dir,children[i]));
                if(!success){
                    return false;
                }
            }
        }else {
            dir.delete();
        }
        return true;
    }

    /**
     * 根据当前时间获取文件名
     * @param fromat
     * @return
     */
    private String getAssignTime(String fromat) {
        DateFormat df = new SimpleDateFormat(fromat);
        long currentTime = System.currentTimeMillis();
        return df.format(currentTime);
    }

    /**
     * 获取崩溃文件名称
     *
     * @return
     */
    public File getCrashFile() {
        String crashFileName = context.getSharedPreferences(CRASH_DIR_NAME,
            Context.MODE_PRIVATE).getString("CRASH_FILE_NAME", "");
        return new File(crashFileName);
    }


}
