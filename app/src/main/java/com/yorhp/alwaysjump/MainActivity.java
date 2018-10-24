package com.yorhp.alwaysjump;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yorhp.alwaysjump.service.MyService;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import permison.FloatWindowManager;

import static com.yorhp.alwaysjump.jump.Jump.testColor;

public class MainActivity extends AppCompatActivity {

    private TextView btnProc, btn_test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //testColor();

        final Intent intent = new Intent(this, MyService.class);
        btnProc = (TextView) findViewById(R.id.btn_proc);
        ScreenRecordUtil.getInstance().screenShot(MainActivity.this, null);
        btnProc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this)) {
                    startService(intent);
                    moveTaskToBack(true);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
