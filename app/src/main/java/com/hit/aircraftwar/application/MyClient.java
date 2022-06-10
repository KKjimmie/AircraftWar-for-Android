package com.hit.aircraftwar.application;

import android.util.Log;
import android.widget.Toast;

import com.hit.aircraftwar.application.Activity.Game.GameActivity;
import com.hit.aircraftwar.application.Activity.MainActivity;

import org.json.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author 柯嘉铭
 * @date 2022/6/6.
 * description：联机客户端类
 */
public class MyClient {
    private static Socket socket;
    public static boolean connection_state = false;
    public static String TAG = "客户端类";
    public static int serverScore = 0;

    public MyClient(){
        while (!connection_state) {
            connect();
            try {
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void connect(){
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress
                    ("10.250.133.41",9999),5000);
            connection_state = true;
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            new Thread(new Client_listen(socket,ois)).start();
            new Thread(new Client_send(socket,oos)).start();
//            new Thread(new Client_heart(socket,oos)).start();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("111111111111111111111111111111");
            connection_state = false;
        }
    }

    public static void reconnect(){
        while (!connection_state){
            Log.d(TAG, "客户端重新链接");
            connect();
            try {
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

class Client_listen implements Runnable{
    private Socket socket;
    private ObjectInputStream ois;

    Client_listen(Socket socket,ObjectInputStream ois){
        this.socket = socket;
        this.ois = ois;
    }

    @Override
    public void run() {
        try {
            while (true){
//                Toast.makeText(MainActivity.baseActivity, ois.readObject().toString(), Toast.LENGTH_SHORT).show();
//                System.out.println(ois.readObject());
                MyClient.serverScore = ois.readInt();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Client_send implements Runnable{
    private Socket socket;
    private ObjectOutputStream oos;

    Client_send(Socket socket, ObjectOutputStream oos){
        this.socket = socket;
        this.oos = oos;
    }

    @Override
    public void run() {
        try {
            while (true){
//                JSONObject object = new JSONObject();
//                object.put("score", 1000);
//                oos.writeObject(object);
                oos.writeInt(GameActivity.score);
                oos.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                System.out.println("1111111112222222222222222222222222");
                socket.close();
                MyClient.connection_state = false;
                MyClient.reconnect();
            }catch (Exception ee){
                System.out.println("2222222222222222222222222");
                ee.printStackTrace();
            }
        }
    }
}
// 心跳包保证连接
class Client_heart implements Runnable{
    private Socket socket;
    private ObjectOutputStream oos;

    Client_heart(Socket socket, ObjectOutputStream oos){
        this.socket = socket;
        this.oos = oos;
    }

    @Override
    public void run() {
        try {
            System.out.println("心跳包线程已启动...");
            while (true){
                Thread.sleep(5000);
                JSONObject object = new JSONObject();
                object.put("type","heart");
                object.put("msg","心跳包");
                oos.writeObject(object);
                oos.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                socket.close();
                MyClient.connection_state = false;
                MyClient.reconnect();
            }catch (Exception ee){
                System.out.println("33333333333333333333");
                ee.printStackTrace();
            }
        }
    }
}

