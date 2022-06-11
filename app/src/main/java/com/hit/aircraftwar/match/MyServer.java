package com.hit.aircraftwar.match;

import android.util.Log;

import com.hit.aircraftwar.application.Activity.Game.GameActivity;
import com.hit.aircraftwar.login.LoginActivity;

import net.sf.json.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 柯嘉铭
 * @date 2022/6/2.
 * description：服务器类
 */
public class MyServer {
    private String content = "";
    public static String TAG = "服务器启动";
    public static int clientScore;
    public static boolean isClientConnected = false;
    public static String clientAccount = "";

    public MyServer() {
        try {
            // 获取本机ip
            InetAddress addr = InetAddress.getLocalHost();
            Log.d(TAG, addr + "");

            //创建server socket
            ServerSocket serverSocket = new ServerSocket(9999);

            while (true) {
//                System.out.println("waiting client connect");
                Socket socket = serverSocket.accept();
//                System.out.println("accept client connect" + socket);
//                new Thread(new Service(socket)).start();
                if(socket.isConnected()){
                    isClientConnected = true;
                }
                // 接受线程启动
                new Thread(new Server_listen(socket)).start();
                // 发送线程启动
                new Thread(new Server_send(socket)).start();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


class Server_listen implements Runnable{
    private Socket socket;

    Server_listen(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (true){
                JSONObject object = (JSONObject) ois.readObject();
                MyServer.clientScore = object.getInt("score");
                MyServer.clientAccount = object.getString("account");
                }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

class Server_send implements Runnable{
    private Socket socket;

    Server_send(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            while (true){
                JSONObject object = new JSONObject();
                object.put("account", LoginActivity.gameUser.getAccount());
                object.put("score", GameActivity.score);
                oos.writeObject(object);
                oos.flush();
                // 1秒发送一次
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

