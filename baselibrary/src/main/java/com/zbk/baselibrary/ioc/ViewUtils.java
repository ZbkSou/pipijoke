package com.zbk.baselibrary.ioc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ZBK on 2018-09-09.
 *
 * @function
 */

public class ViewUtils {
    //activity
    public static  void inject(Activity activity){

        inject( new ViewFinder(activity),activity );
    }
    //view
    public static void inject(View view){

        inject( new ViewFinder(view),view );
    }

    ///fragment
    public static void inject(View view ,Object object){
        inject( new ViewFinder(view) ,object );

    }

    //兼容上面三个方法  object为反射要执行的类
    private static void inject(ViewFinder finder,Object object){
        injectFiled(finder,object);
        injectEvent(finder,object);
    }

    private static void injectEvent(ViewFinder finder, Object object) {
        //!.获取所有方法
        Class<?> clazz= object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        //2.获取OnClick里面的值
        for(Method method:methods){
            OnClick onClick = method.getAnnotation(OnClick.class);
            if(onClick !=null){
                int[] viewIds=onClick.value();
                for (int viewId:viewIds){
                    //3.findViewById 找到View
                    View view =finder.findViewById(viewId);
                    method.setAccessible(true);

                    //拓展功能 检测网络
                    boolean isCheckNet = method.getAnnotation(CheckNet.class)!=null;
                    if(view != null){
                        //4.view.setOnClickListenter
                        view.setOnClickListener(new DeclaredOnClickListener(method,object,isCheckNet));
                    }
                }
            }
        }



    }

    private static class DeclaredOnClickListener implements View.OnClickListener{
        private Object mObject;
        private Method mMethod;
        private boolean misCheckNet;

        public DeclaredOnClickListener(Method method,Object object,boolean isCheckNet){
            this.mObject = object;
            this.mMethod = method;
            this.misCheckNet = isCheckNet;
        }

        @Override
        public void onClick(View v) {
            //判断网络
            if(misCheckNet){
                if(!isConnect(v.getContext())){
                    return;
                }
            }


            try {
                //5.反射执行方法
                mMethod.invoke(mObject,v);
            } catch (Exception e) {
                e.printStackTrace();

                try {
                    mMethod.invoke(mObject,null);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    /**
     * 注入属性
     * @param finder
     * @param object
     */
    private static void injectFiled(ViewFinder finder, Object object)  {
        //1.获取类里面所有属性
        Class<?>clazz =  object.getClass();
        //获取所有属性公私有属性
        Field[] fields  = clazz.getDeclaredFields();

        //2.获取viewbyid注解里面的值
        for(Field field:fields){
            ViewById viewById = field.getAnnotation(ViewById.class);
            if(viewById != null){
                //获取注解里面的id R.id.xx
                int viewId = viewById.value();
                //3.findViewById找到view
                View view = finder.findViewById(viewId);
                //id不存在的情况
                if(view!=null){
                    //注入所有修饰符 pr pu
                    field.setAccessible(true);
                    //4.动态注入找到的的view
                    try {
                        field.set(object,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isConnect(Context context) {
        boolean _isConnect = false;
        ConnectivityManager conManager = (ConnectivityManager)context
          .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            _isConnect = conManager.getActiveNetworkInfo().isAvailable();
        }
        return _isConnect;
    }
}
