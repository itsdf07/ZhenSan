package com.sdf.zhensan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.sdf.aso.common.logutils.LogUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("这个是测试log信息打印");
            }
        });
//        FileUtils.getAssetsResource(getApplicationContext(), "heros.txt");
    }
}
