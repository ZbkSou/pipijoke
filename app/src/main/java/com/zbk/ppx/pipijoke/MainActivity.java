package com.zbk.ppx.pipijoke;

import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import com.ihaveu.iuzuan.framelibrary.BaseSkinActivity;
import com.zbk.baselibrary.ioc.OnClick;
import com.zbk.baselibrary.ioc.ViewById;

import java.io.File;
import java.io.IOException;


public class MainActivity extends BaseSkinActivity {


    /****test****/
    @ViewById(R.id.test_but)
    private Button mTestBut;

    @Override
    protected void initData() {

//        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
//        if(crashFile.exists()){
//            //上传服务器
//            try {
//                InputStreamReader fileReader = new InputStreamReader(new FileInputStream(crashFile));
//                char[] buffer = new char[1024];
//                int len = 0;
//                while ((len = fileReader.read(buffer))!=-1){
//                    String str = new String(buffer,0,len);
//                    Log.d("TAG",str);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        File fixFile = new File(Environment.getExternalStorageDirectory(),"fix.aptach");
        if(fixFile.exists()){
            //修复 bug
            try {
                BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this,"修复成功",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"修复失败",Toast.LENGTH_LONG).show();
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

    }


    @OnClick(R.id.test_but)
    private void testButClick(Button testBut) {
        Toast.makeText(this,2/0+"",Toast.LENGTH_LONG).show();
    }
}
