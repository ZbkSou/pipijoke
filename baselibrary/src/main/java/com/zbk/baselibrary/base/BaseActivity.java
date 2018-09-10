package com.zbk.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zbk.baselibrary.ioc.ViewUtils;

/**
 * User: bkzhou
 * Date: 2018/9/10
 * Description:  整合执行顺序
 *  1.设置布局
 *  2.初始化页面
 * 3.初始化头部
 *  4.初始化数据
 */
public abstract class BaseActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.设置布局，绑定 ioc
        this.setContentView();
        //绑定 ioc
        ViewUtils.inject(this);
        //2.初始化页面
        this.initView();
        //3.初始化头部
        this.initTitle();
        // 4.初始化数据
        this.initData();


    }

    protected abstract void initData();

    protected abstract void initTitle();

    protected abstract void initView();


    protected abstract void setContentView();

    /**
     * 页面跳转方法
     * @param clazz
     */
    protected  void startActivity(Class<?> clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }
}
