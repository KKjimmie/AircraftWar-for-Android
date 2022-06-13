package com.hit.aircraftwar.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.Activity.Game.EasyGameActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    public static LoginActivity loginActivity;
    // 服务器回传是否登录成功信息
    public static Boolean result;
    // 本地玩家信息
    public static User gameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = this;

        // 设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // 注册按钮
        TextView register = findViewById(R.id.lg_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        // 连接服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                LgClient.connect();
            }
        }).start();

        //登录
        Button loginButton = findViewById(R.id.lg_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! LgClient.connection_state){
                    Toast.makeText(LoginActivity.this, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                }else {
                    EditText username = findViewById(R.id.lg_username);
                    EditText password = findViewById(R.id.lg_password);
                    // 传递账号给服务器
                    gameUser = new User(username.getText().toString(), password.getText().toString(), 1000);
                    LgClient.responseWithType(LgClient.ACCOUNT_INFO);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 在服务器获得账号信息后，响应
    public void response(){
        //账号密码匹配
        if (result) {
            // 记录以保持登入状态
            SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
            sp.edit()
                    .putString("account", gameUser.getAccount())
                    .putString("password", gameUser.getPassword())
                    .putInt("credits", gameUser.getCredits())
                    .putBoolean("isLogin", true)
                    .apply();
            Looper.prepare();
            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Looper.prepare();
            Toast.makeText(LoginActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
        }
        Looper.loop();
    }

    //接受注册界面传回来的账号
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    final EditText loginUsername = findViewById(R.id.lg_username);
                    String returnUsername = data.getStringExtra("username");
                    loginUsername.setText(returnUsername);
                    loginUsername.setSelection(returnUsername.length());
                }
                break;
            default:
        }
    }
}