package com.hit.aircraftwar.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hit.aircraftwar.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                User user = null;
                if (inputAffirm.equals(inputPassword)) {
                    //存储账号密码
                    user = new User(inputAccount, inputPassword, 1000);
                    //传回账号
                    Intent intent = new Intent();
                    intent.putExtra("username", inputAccount);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this,"两次密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}