package com.hit.aircraftwar.login;

import android.util.Log;
import com.hit.aircraftwar.application.MyClient;

import net.sf.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author 柯嘉铭
 * @date 2022/6/10.
 * description：登录客户端
 */
public class LgClient {

    // 标识信息类型
    public static final int CREATE_ACCOUNT = 0;
    public static final int UPDATE_PWD = 1;
    public static final int UPDATE_CREDIT = 2;
    public static final int DELETE_ACCOUNT = 3;
    public static final int ACCOUNT_INFO = 4;

    private static Socket socket;
    public static boolean connection_state = false;
    public static String TAG = "客户端类";

    public static PrintWriter writer;
    public static BufferedReader in;

    public static int type = -1;


//    public LgClient() {
//        while (!connection_state) {
//            connect();
//            try {
//                Thread.sleep(3000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static void connect() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress
                    ("10.249.57.123", 9998), 5000);
            connection_state = true;
            writer = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream(),"utf-8")),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("111111111111111111111111111111");
            connection_state = false;
        }
    }

    public static void send(){
        new Thread(new clientSend(socket, writer)).start();
    }

    public static void listen(){
        new Thread(new clientListen(socket, in)).start();
    }

    public static void reconnect() {
        while (!connection_state) {
            Log.d(TAG, "客户端重新链接");
            connect();
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接受服务器信息
     */
    static class clientListen implements Runnable {
        private Socket socket;
        private BufferedReader in;

        private String content = "";

        clientListen(Socket socket, BufferedReader in) {
            this.socket = socket;
            this.in = in;
        }

        @Override
        public void run() {
            try {
                while ((content = in.readLine()) != null) {
                    System.out.println(content);
                    parserJson(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //json对象解析,同时进行相对应判断
        public void parserJson(String content) {
            String account = null;
            String pwd = null;
            int credits = 0;

            JSONObject jsonObject = JSONObject.fromObject(content);
            if (jsonObject != null) {
                switch (jsonObject.getInt("type")) {
                    case ACCOUNT_INFO:
                        account = jsonObject.getString("account");
                        pwd = jsonObject.getString("password");
                        credits = jsonObject.getInt("credits");
                        LoginActivity.realUser = new User(account, pwd, credits);
                        LoginActivity.isGetRealUser = true;
                        break;
                }
            }
        }
    }

    static class clientSend implements Runnable {
        private Socket socket;
        private PrintWriter writer;


        clientSend(Socket socket, PrintWriter writer) {
            this.socket = socket;
            this.writer = writer;
        }

        @Override
        public void run() {
            try {
                JSONObject object = new JSONObject();
                 if(type != -1) {
                    switch(type){
                        case ACCOUNT_INFO:
                            object = new JSONObject();
                            object.put("type", ACCOUNT_INFO);
                            object.put("account", LoginActivity.gameUser.getAccount());

                            writer.println(object.toString());
                            break;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    System.out.println("1111111112222222222222222222222222");
                    socket.close();
                    MyClient.connection_state = false;
                    MyClient.reconnect();
                } catch (Exception ee) {
                    System.out.println("2222222222222222222222222");
                    ee.printStackTrace();
                }
            }
        }
    }
}

