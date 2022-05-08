package com.hit.aircraftwar.application.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.Game;

import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Button easyButton;
    private Button commonButton;
    private Button hardButton;
    private CheckBox soundCheckBox;

    public static int width;
    public static int height;
    public static ScheduledThreadPoolExecutor executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//                    setContentView(new Game(this));
                    Intent intent = new Intent(MainActivity.this, Game.class);
                    startActivity(intent);
                });

        commonButton = (Button) findViewById(R.id.common_button);
        commonButton.setOnClickListener(
                view -> Toast.makeText(this,R.string.common_toast,Toast.LENGTH_SHORT).show());

        hardButton = (Button) findViewById(R.id.hard_button);
        hardButton.setOnClickListener(
                view -> Toast.makeText(this, R.string.hard_toast, Toast.LENGTH_SHORT).show());

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
}