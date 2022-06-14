package com.hit.aircraftwar.match;

import android.util.Log;

import com.hit.aircraftwar.application.Activity.Game.GameActivity;
import com.hit.aircraftwar.login.LoginActivity;

import net.sf.json.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public static String serverAccount = "";
    public static boolean serverState = false;
    private static String ip;

    public MyClient(String ip){
        this.ip = ip;
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
            // 10.250.133.41
            socket.connect(new InetSocketAddress
                    (ip,9999),5000);
            connection_state = true;
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            new Thread(new Client_listen(socket,ois)).start();
            new Thread(new Client_send(socket,oos)).start();
        }catch (Exception e){
            e.printStackTrace();
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
                JSONObject object = (JSONObject) ois.readObject();
                MyClient.serverScore = object.getInt("score");
                MyClient.serverAccount = object.getString("account");
                MyClient.serverState = object.getBoolean("over");

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
                JSONObject object = new JSONObject();
                object.put("account", LoginActivity.gameUser.getAccount());
                object.put("score", GameActivity.score);
                object.put("over", GameActivity.gameOverFlag);
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
                ee.printStackTrace();
            }
        }
    }
}
