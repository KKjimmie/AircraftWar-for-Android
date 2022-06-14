package com.hit.aircraftwar.application.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.Activity.Game.CommonGameActivity;
import com.hit.aircraftwar.match.MyClient;
import com.hit.aircraftwar.match.MyServer;
import com.hit.aircraftwar.application.Settings;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class MatchActivity extends AppCompatActivity {
    private Socket socket;
    private PrintWriter writer;
    public static Context mContext;
    public static boolean isClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext=this.getBaseContext();
        isClient = false;

        // 设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_match);

        // 开房间按钮
        Button createRoom = findViewById(R.id.server_button);
        createRoom.setOnClickListener(view -> {
            createRoom();
        });

        // 加入房间按钮
        Button joinRoom = findViewById(R.id.client_button);
        joinRoom.setOnClickListener(view ->{
            EditText editText = findViewById(R.id.ip_edittext);
            String ip = editText.getText().toString();
            if("".equals(ip)){
                Toast.makeText(this, "请输入房主ip", Toast.LENGTH_SHORT).show();
            }else{
                joinRoom(ip);
            }
        });
        // 返回按钮
        Button back = findViewById(R.id.match_back_button);
        back.setOnClickListener(view ->{
           finish();
        });
    }

    // 创建房间
    public void createRoom(){
        isClient = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MyServer();
            }
        }).start();
        Toast.makeText(this, "创建成功，等待加入",Toast.LENGTH_LONG).show();
        while(true){
            if (MyServer.isClientConnected) {
                Intent intent = new Intent(this, CommonGameActivity.class);
                Settings.getInstance().setGameMode(Settings.VS_MODE);
                startActivity(intent);
                break;
            }
        }
    }

    /**
     * 加入房间
     * @param ip 房主ip
     */
    public void joinRoom(String ip){
        isClient = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MyClient(ip);
            }
        }).start();
        Toast.makeText(this, "搜索房间中。。。",Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isConnected = false;
                while(! isConnected){
                    if(MyClient.connection_state){
                        Intent intent = new Intent(MatchActivity.this, CommonGameActivity.class);
                        Settings.getInstance().setGameMode(Settings.VS_MODE);
                        startActivity(intent);
                        isConnected = true;
                        System.out.println("连接成功！..................");
                        // 3秒判定一次
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("连接中！..................");
                }
            }
        }).start();
    }

}