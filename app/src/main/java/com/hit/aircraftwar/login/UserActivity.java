package com.hit.aircraftwar.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hit.aircraftwar.R;

import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    @SuppressLint({"SetTextI18n", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // 设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // 获取账户信息
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);

        // 显示账户信息
        TextView setAccount = findViewById(R.id.set_account);
        setAccount.setText(sp.getString("account", null));
        TextView setCredits = findViewById(R.id.set_credits);
        setCredits.setText(sp.getInt("credits", 0) + "");

        // 退出登录按钮
        Button exitLoginButton = findViewById(R.id.exitLogin_button);
        exitLoginButton.setOnClickListener(
                view -> {
                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                    sp.edit()
                            .putBoolean("isLogin", false)
                            .apply();
                    startActivity(intent);
                    finish();
        });

        // 返回按钮
        Button back = findViewById(R.id.user_back_button);
        back.setOnClickListener(
                view -> {
                    finish();
        });
    }
}