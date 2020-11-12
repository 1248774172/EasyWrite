package com.xiaoer.easywrite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.xiaoer.easywrite.Utils.SpKey;
import com.xiaoer.easywrite.Utils.SpUtil;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private ImageView iv_ad;
    private ImageView iv_splash;
    private Timer mTimer;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            iv_splash.setVisibility(View.GONE);
        }
    };
    private Button bt_stepAD;
    private TimerTask mTimerTask;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initView();


        Glide.with(SplashActivity.this)
                .load(R.drawable.ad)
                .placeholder(R.drawable.ad)
                .into(iv_ad);

        mTimerTask = new TimerTask() {
            int i = 6;
            @Override
            public void run() {
                if (i > 0) {
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            bt_stepAD.setText(i + "  跳过");
                        }
                    });
                    i--;
                } else {
                    nextActivity();
                    this.cancel();
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(3000);
                Log.i(TAG, "run: --------------------图标展示完毕，展示广告");
                mHandler.sendEmptyMessage(0);
                mTimer = new Timer();
                mTimer.schedule(mTimerTask, 0, 1000);
            }
        }.start();

    }

    private void initView() {
        iv_ad = (ImageView) findViewById(R.id.iv_ad);
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
        bt_stepAD = findViewById(R.id.bt_stepAD);

        //给跳过按钮添加监听事件
        bt_stepAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerTask.cancel();
                nextActivity();
            }
        });
    }

    private void nextActivity() {
        Log.i(TAG, "-------------------广告页展示完毕，进入应用");
        boolean is_first_enter = SpUtil.getBoolean(this, SpKey.IS_FIRST_ENTER, true);
        Intent intent;
        if(is_first_enter)
            intent = new Intent(this,GuideActivity.class);
        else
            intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}