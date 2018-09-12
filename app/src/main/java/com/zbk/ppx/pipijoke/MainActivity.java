package com.zbk.ppx.pipijoke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ihaveu.iuzuan.framelibrary.BaseSkinActivity;
import com.zbk.baselibrary.ExceptionCrashHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;


public class MainActivity extends BaseSkinActivity {




    @Override
    protected void initData() {

        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
        if(crashFile.exists()){
            //上传服务器
            try {
                InputStreamReader fileReader = new InputStreamReader(new FileInputStream(crashFile));
                char[] buffer = new char[1024];
                int len = 0;
                while ((len = fileReader.read(buffer))!=-1){
                    String str = new String(buffer,0,len);
                    Log.d("TAG",str);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void initTitle() {


    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setContentView() {
        //设置页面
        setContentView(R.layout.activity_main);
        int i = 2/0;


    }


}
