package com.zbk.ppx.pipijoke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zbk.baselibrary.ioc.OnClick;
import com.zbk.baselibrary.ioc.ViewById;
import com.zbk.baselibrary.ioc.ViewUtils;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.test_tv)
    private TextView mTestTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);

        mTestTv.setText("IOC");
    }

    @OnClick(R.id.test_tv)
    protected void onClick(View view){
        Log.d("jw","sss");
    }
}
