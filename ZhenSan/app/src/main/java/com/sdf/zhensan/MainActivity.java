package com.sdf.zhensan;

import android.app.Activity;
import android.os.Bundle;

import com.sdf.aso.common.file.FileUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileUtils.getRawResource(getApplicationContext(),"heros.txt");
    }
}
