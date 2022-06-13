package com.hit.aircraftwar.application.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.Activity.Game.CommonGameActivity;
import com.hit.aircraftwar.application.Activity.Game.EasyGameActivity;
import com.hit.aircraftwar.application.Activity.Game.HardGameActivity;
import com.hit.aircraftwar.application.Settings;
import com.hit.aircraftwar.login.LoginActivity;

import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Button loginButton;
    private Button singleButton;
    private Button exitButton;
    private Button vsButton;
    private Button storeButton;
    private CheckBox soundCheckBox;
    private Button item_on_off;

    private Button easyButton;
    private Button commonButton;
    private Button hardButton;
    private Button backToMainButton;


    private Button backButton;
    private Button lookButton;
    private Button purchaseButton1;
    private Button purchaseButton2;
    private Button purchaseButton3;
    private Button purchaseButton4;

    public static int width;
    public static int height;
    public static ScheduledThreadPoolExecutor executorService;

    public static MainActivity baseActivity;//传递给非activity的类使用
    public static Context mContext;//传递给非activity的类使用

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;


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

        // 设置layout
        setFirstLayout();
    }

    // 设置第一个layout的按钮监听
    private void setFirstLayout(){
        // 登录

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(
                view -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
            });

        // 单机模式
        singleButton = (Button) findViewById(R.id.single_button);
        singleButton.setOnClickListener(
                view -> {
                    setContentView(R.layout.single_choice);
                    setSecondLayout();
                });

        // 联机模式
        vsButton = (Button) findViewById(R.id.vs_button);
        vsButton.setOnClickListener(
                view ->{
                    // 判断是否登录账号
                    if(LoginActivity.isLogin == false){
                        Toast.makeText(this, "还没登录，请先登录!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, R.string.vs_toast, Toast.LENGTH_SHORT).show();
                        Settings.getInstance().setGameMode(Settings.VS_MODE);
                        Intent intent = new Intent(MainActivity.this, MatchActivity.class);
                        startActivity(intent);
                    }
                });
        //商店
        storeButton = (Button) findViewById(R.id.store_button);
        storeButton.setOnClickListener(
                view -> {
                    setContentView(R.layout.activity_store);
                    setstoreLayout();
                }
        );
        // 退出游戏
        exitButton = (Button) findViewById(R.id.exit_button);
        exitButton.setOnClickListener(
                view ->{
                    Intent MyIntent = new Intent(Intent.ACTION_MAIN);
                    MyIntent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(MyIntent);
                    finish();
                    System.exit(0);
                });

        // 音效选择
        soundCheckBox = (CheckBox) findViewById(R.id.sound_check_box);
        soundCheckBox.setOnCheckedChangeListener(this);
        // 道具选择使用
        item_on_off = (Button) findViewById(R.id.item_on_off);
        item_on_off.setOnClickListener(
                view ->{
                    if(LoginActivity.isLogin == false){
                        Toast.makeText(this, "还没登录，请先登录!", Toast.LENGTH_SHORT).show();
                    }else {
                        if(!Settings.getInstance().getItemState()){
                            Settings.getInstance().setItemuseOn();
                            Toast.makeText(this, "道具使用 on", Toast.LENGTH_SHORT).show();
                        }else{
                            Settings.getInstance().setItemuseOff();
                            Toast.makeText(this, "道具使用 off", Toast.LENGTH_SHORT).show();
                        }
                }});
    }

    // 设置第二个layout的监听按钮
    private void setSecondLayout(){
        // 简单模式
        easyButton = (Button) findViewById(R.id.easy_button);
        easyButton.setOnClickListener(
                view -> {
                    Toast.makeText(this,R.string.easy_toast,Toast.LENGTH_SHORT).show();
                    Settings.getInstance().setGameMode(Settings.EASY_MODE);
                    Intent intent = new Intent(MainActivity.this, EasyGameActivity.class);
                    startActivity(intent);
                });

        // 普通模式
        commonButton = (Button) findViewById(R.id.common_button);
        commonButton.setOnClickListener(
                view -> {
                    Toast.makeText(this,R.string.common_toast,Toast.LENGTH_SHORT).show();
                    Settings.getInstance().setGameMode(Settings.COMMON_MODE);
                    Intent intent = new Intent(MainActivity.this, CommonGameActivity.class);
                    startActivity(intent);
                });

        // 困难模式
        hardButton = (Button) findViewById(R.id.hard_button);
        hardButton.setOnClickListener(
                view -> {
                    Toast.makeText(this, R.string.hard_toast, Toast.LENGTH_SHORT).show();
                    Settings.getInstance().setGameMode(Settings.HARD_MODE);
                    Intent intent = new Intent(MainActivity.this, HardGameActivity.class);
                    startActivity(intent);
                });

        backToMainButton = (Button) findViewById(R.id.back_to_main_button);
        backToMainButton.setOnClickListener(
                view -> {
                    setContentView(R.layout.activity_main);
                    setFirstLayout();
            });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.isChecked()){
            Settings.getInstance().setSoundOn();
            Toast.makeText(this, "音效开",Toast.LENGTH_SHORT).show();
        }else if(! compoundButton.isChecked()){
            Settings.getInstance().setSoundOff();
            Toast.makeText(this, "音效关",Toast.LENGTH_SHORT).show();
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出游戏",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    private void setstoreLayout(){
        backButton = (Button) findViewById(R.id.button);
        backButton.setOnClickListener(
                view -> {
                    setContentView(R.layout.activity_main);
                    setFirstLayout();
                }
        );
            purchaseButton1 = (Button) findViewById(R.id.button2);
            purchaseButton1.setOnClickListener(view -> {
                if(LoginActivity.isLogin == false){
                    Toast.makeText(this, "还没登录，请先登录!", Toast.LENGTH_SHORT).show();
                }else {
                    if(LoginActivity.gameUser.getCredits()<5){
                        Toast.makeText(this, "积分不足!剩余积分："+LoginActivity.gameUser.getCredits()+"道具量："+LoginActivity.gameUser.getItem1(), Toast.LENGTH_SHORT).show();
                    }else{
                        LoginActivity.gameUser.useCredits();
                        LoginActivity.gameUser.addItem1();
                        Toast.makeText(this, "购买成功!剩余积分："+LoginActivity.gameUser.getCredits()+"道具量："+LoginActivity.gameUser.getItem1(), Toast.LENGTH_SHORT).show();
                    }
                }});
            purchaseButton2 = (Button) findViewById(R.id.button3);
            purchaseButton2.setOnClickListener(view -> {
                if(LoginActivity.isLogin == false){
                Toast.makeText(this, "还没登录，请先登录!", Toast.LENGTH_SHORT).show();
            }else {
                if(LoginActivity.gameUser.getCredits()<5){
                    Toast.makeText(this, "积分不足!剩余积分："+LoginActivity.gameUser.getCredits()+"道具量："+LoginActivity.gameUser.getItem2(), Toast.LENGTH_SHORT).show();
                }else{
                    LoginActivity.gameUser.useCredits();
                    LoginActivity.gameUser.addItem2();
                    Toast.makeText(this, "购买成功!剩余积分："+LoginActivity.gameUser.getCredits()+"道具量："+LoginActivity.gameUser.getItem2(), Toast.LENGTH_SHORT).show();
                }
            }});
           purchaseButton3 = (Button) findViewById(R.id.button4);
            purchaseButton3.setOnClickListener(view -> { if(LoginActivity.isLogin == false){
                Toast.makeText(this, "还没登录，请先登录!", Toast.LENGTH_SHORT).show();
            }else {
                if(LoginActivity.gameUser.getCredits()<5){
                    Toast.makeText(this, "积分不足!剩余积分："+LoginActivity.gameUser.getCredits()+"道具量："+LoginActivity.gameUser.getItem3(), Toast.LENGTH_SHORT).show();
                }else{
                    LoginActivity.gameUser.useCredits();
                    LoginActivity.gameUser.addItem3();
                    Toast.makeText(this, "购买成功!剩余积分："+LoginActivity.gameUser.getCredits()+"道具量："+LoginActivity.gameUser.getItem3(), Toast.LENGTH_SHORT).show();
                }
            }});
            purchaseButton4 = (Button) findViewById(R.id.button5);
            purchaseButton4.setOnClickListener(view -> { if(LoginActivity.isLogin == false){
                Toast.makeText(this, "还没登录，请先登录!", Toast.LENGTH_SHORT).show();
            }else {
                if(LoginActivity.gameUser.getCredits()<5){
                    Toast.makeText(this, "积分不足!剩余积分："+LoginActivity.gameUser.getCredits()+"道具量："+LoginActivity.gameUser.getItem4(), Toast.LENGTH_SHORT).show();
                }else{
                    LoginActivity.gameUser.useCredits();
                    LoginActivity.gameUser.addItem4();
                    Toast.makeText(this, "购买成功!剩余积分："+LoginActivity.gameUser.getCredits()+"道具量："+LoginActivity.gameUser.getItem4(), Toast.LENGTH_SHORT).show();
                }
            }});
        }
}