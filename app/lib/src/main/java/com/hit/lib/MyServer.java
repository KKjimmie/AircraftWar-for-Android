package com.hit.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    private String content = "";
    public static void main(String args[]){
        new MyServer();
    }
    public  MyServer(){
        try{
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);

            //创建server socket
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("listen port 9999");

            while(true){
                System.out.println("waiting client connect");
                Socket socket = serverSocket.accept();
                System.out.println("accept client connect" + socket);
                new Thread(new Service(socket)).start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    class Service implements Runnable{
        private Socket socket;
        private BufferedReader in = null;


        public Service(Socket socket){
            this.socket = socket;
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            }catch (IOException ex){
                ex.printStackTrace();
            }
        }


        @Override
        public void run() {
            System.out.println("wait client message " );
            try {
                while ((content = in.readLine()) != null) {
                    System.out.println("message from client:"+content);
                    if(content.equals("bye")){
                        System.out.println("disconnect from client,close socket");
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();
                    }else {
                        this.sendMessge(socket);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        public void sendMessge(Socket socket) {
            PrintWriter pout = null;
            try{
                String message = "hello,client!";
                System.out.println("messge to client:" + message);
                pout = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),true);
                pout.println(message);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}