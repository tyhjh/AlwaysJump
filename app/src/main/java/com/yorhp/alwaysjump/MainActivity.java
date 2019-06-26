package com.yorhp.alwaysjump;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yorhp.alwaysjump.app.Const;
import com.yorhp.alwaysjump.jump.Jump;
import com.yorhp.alwaysjump.service.MyService;
import com.yorhp.alwaysjump.util.AccessbilityUtil;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import permison.FloatWindowManager;
import permison.PermissonUtil;

public class MainActivity extends AppCompatActivity {

    private TextView btnProc, btn_test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btnProc = (TextView) findViewById(R.id.btn_proc);
        btn_test = (TextView) findViewById(R.id.btn_test);
        ScreenRecordUtil.getInstance().screenShot(MainActivity.this, null);
        Jump.setStart_model(Const.RUN_MODEL_SAVE_PIC);

        PermissonUtil.checkPermission(this, null, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        btnProc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this)) {
                    if (!AccessbilityUtil.isAccessibilitySettingsOn(MainActivity.this, MyService.class)) {
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    } else {
                        moveTaskToBack(true);
                    }
                }
            }
        });
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Jump.testColor();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
