package com.hit.aircraftwar.application.Activity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.music.MusicManager;
import com.hit.aircraftwar.music.MySoundPool;

import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Button easyButton;
    private Button commonButton;
    private Button hardButton;
    private CheckBox soundCheckBox;

    public static int width;
    public static int height;
    public static ScheduledThreadPoolExecutor executorService;

    public static MainActivity baseActivity;//传递给非activity的类使用
    public static Context mContext;//传递给非activity的类使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseActivity=this;//传递给非activity的类使用
        mContext=this.getBaseContext();//传递给非activity的类使用

        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        //Scheduled 线程池，用于定时任务调度
        executorService = new ScheduledThreadPoolExecutor(20);


        // 设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        easyButton = (Button) findViewById(R.id.easy_button);
        easyButton.setOnClickListener(
                view -> {
                    Toast.makeText(this,R.string.easy_toast,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, EasyGameActivity.class);
                    startActivity(intent);
                });

        commonButton = (Button) findViewById(R.id.common_button);
        commonButton.setOnClickListener(
                view -> {
                    Toast.makeText(this,R.string.common_toast,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, CommonGameActivity.class);
                    startActivity(intent);
                });

        hardButton = (Button) findViewById(R.id.hard_button);
        hardButton.setOnClickListener(
                view -> {
                    Toast.makeText(this, R.string.hard_toast, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, HardGameActivity.class);
                    startActivity(intent);
                });
        soundCheckBox = (CheckBox) findViewById(R.id.sound_check_box);
        soundCheckBox.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.isChecked()){
            Toast.makeText(this, "音效开",Toast.LENGTH_SHORT).show();
        }else if(! compoundButton.isChecked()){
            Toast.makeText(this, "音效关",Toast.LENGTH_SHORT).show();
        }
    }

    // TODO:返回桌面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AtomicReference<Boolean> back = new AtomicReference<>(false);
        if(keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("退出游戏？")
                    .setMessage("是否退出游戏。")
                    .setIcon(R.drawable.hero)
                    .setPositiveButton("确定", (dialog, which) -> {back.set(true); })
                    .setNegativeButton("取消", (dialog, which) -> {back.set(false);})
                    .create();
            alertDialog.show();
        }
        if (back.get()){
            // TODO:实现结束进程返回桌面
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
        }
        return back.get();
    }
}