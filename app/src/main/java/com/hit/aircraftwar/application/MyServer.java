package com.hit.aircraftwar.application;

import android.content.Intent;
import android.service.autofill.FieldClassification;
import android.util.Log;

import com.hit.aircraftwar.application.Activity.Game.CommonGameActivity;
import com.hit.aircraftwar.application.Activity.Game.GameActivity;
import com.hit.aircraftwar.application.Activity.MatchActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

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
//                System.out.println(ois.readObject());
                MyServer.clientScore = ois.readInt();
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
//                JSONObject object = new JSONObject();
//                object.put("score",1000);
//                oos.writeObject(object);
                oos.writeInt(GameActivity.score);
                oos.flush();
                // 1秒发送一次
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

//    class Service implements Runnable{
//        private Socket socket;
//        private BufferedReader in = null;
//
//
//        public Service(Socket socket){
//            this.socket = socket;
//            try{
//                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//            }catch (IOException ex){
//                ex.printStackTrace();
//            }
//        }
//
//
//        @Override
//        public void run() {
//            System.out.println("wait client message " );
//            try {
//                while ((content = in.readLine()) != null) {
//                    System.out.println("message from client:"+content);
//                    if(content.equals("bye")){
//                        System.out.println("disconnect from client,close socket");
//                        socket.shutdownInput();
//                        socket.shutdownOutput();
//                        socket.close();
//                    }else {
//                        this.sendMessge(socket);
//                    }
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        public void sendMessge(Socket socket) {
//            PrintWriter pout = null;
//            try{
//                String message = GameActivity.score + "";
//                System.out.println("messge to client:" + message);
//                pout = new PrintWriter(new BufferedWriter(
//                        new OutputStreamWriter(socket.getOutputStream())),true);
//                pout.println(message);
//            }catch (IOException ex){
//                ex.printStackTrace();
//            }
//        }
//    }

