package com.hit.aircraftwar.login;

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
import android.widget.Toast;

import com.hit.aircraftwar.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    public static User registerUser;
    public Intent intent;

    public static RegisterActivity registerActivity;

    // 注册结果
    public static boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerActivity = this;

        // 设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();


        Button re_register = findViewById(R.id.re_register);
        re_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.re_username);
                EditText password = findViewById(R.id.re_password);
                EditText passwordAffirm = findViewById(R.id.re_affirm);
                String inputAccount = username.getText().toString();
                String inputPassword = password.getText().toString();
                String inputAffirm = passwordAffirm.getText().toString();
                registerUser = null;
                if (inputAffirm.equals(inputPassword)) {
                    //存储账号密码
                    registerUser = new User(inputAccount, inputPassword, 1000);
                    // 通知注册
                    LgClient.responseWithType(LgClient.CREATE_ACCOUNT);
                    //传回账号
                    intent = new Intent();
                    intent.putExtra("username", inputAccount);
                    setResult(RESULT_OK, intent);
                } else {
                    Toast.makeText(RegisterActivity.this,"两次密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void response() {
        //账号密码匹配
        if (result) {
            finish();
            Looper.prepare();
            Toast.makeText(RegisterActivity.this, "注册成功，返回登录界面", Toast.LENGTH_SHORT).show();
        } else {
            Looper.prepare();
            Toast.makeText(RegisterActivity.this, "账号已存在！", Toast.LENGTH_SHORT).show();
        }
        Looper.loop();
    }
}