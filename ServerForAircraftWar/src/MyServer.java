import net.sf.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MyServer {

    // 标识信息类型
    public static final int CREATE_ACCOUNT = 0;
    public static final int UPDATE_PWD = 1;
    public static final int UPDATE_CREDIT = 2;
    public static final int DELETE_ACCOUNT = 3;
    public static final int ACCOUNT_INFO = 4;
    public static final int MATCH_REQ = 5;
    public static final int DIS_MATCH_REQ = 6;
    public static final int MATCH_SCORE = 7; // 联机时发送分数和账号
    public static final OperateSQLite operateSQLite = new OperateSQLite();

    public static User tempUser = null;
    public static boolean result = false;

    public static int type = -1;

    // 构造服务器与联机准备与否的映射关系
    Map<Socket, Boolean> socketBooleanMap = new HashMap<>();
    // 构建两个客户端的对应关系
    Map<Socket, Socket> socketSocketMap = new HashMap<>();

    public static ScoreMsg scoreMsg = null;

    public static void main(String args[]){
        new MyServer();
    }
    public  MyServer(){
        try{
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);

            //创建server socket
            ServerSocket serverSocket = new ServerSocket(9998);
            System.out.println("listen port 9998");

            while(true){
                System.out.println("waiting client connect");
                Socket socket = serverSocket.accept();
                socketBooleanMap.put(socket, false);
                System.out.println("accept client connect" + socket);
                // 接受线程启动
                new Thread(new serverListen(socket)).start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    /**
     * 接受客户端发来的信息
     */
    class serverListen implements Runnable{
        private Socket socket;
        private String content = "";

        serverListen(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("接受线程已启动");
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while((content = in.readLine()) != null){
                    System.out.println("listen:" +content);
                    parserJson(content);
                    notifySend();
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

        //json对象解析,同时进行相对应判断
        public void parserJson(String content){
            String account = null;
            String pwd = null;
            String newPwd = null;
            int credits = 0;

            JSONObject jsonObject = JSONObject.fromObject(content);
            if (jsonObject != null){
                switch (jsonObject.getInt("type")){
                    case CREATE_ACCOUNT:
                        account = jsonObject.getString("account");
                        pwd = jsonObject.getString("password");
                        credits = 1000;
                        result = operateSQLite.insert(account, pwd, credits);
                        type = CREATE_ACCOUNT;
                        break;
                    case UPDATE_PWD:
                        account = jsonObject.getString("account");
                        pwd = jsonObject.getString("password");
                        newPwd = jsonObject.getString("newPassword");
                        operateSQLite.updatePwd(account, pwd, newPwd);
                        type = UPDATE_PWD;
                        break;
                    case UPDATE_CREDIT:
                        account = jsonObject.getString("account");
                        credits = jsonObject.getInt("credits");
                        operateSQLite.updateCredits(account, credits);
                        type = UPDATE_CREDIT;
                        break;
                    case DELETE_ACCOUNT:
                        account = jsonObject.getString("account");
                        operateSQLite.deleteAccount(account);
                        type = DELETE_ACCOUNT;
                        break;
                    case ACCOUNT_INFO:
                        account = jsonObject.getString("account");
                        pwd = jsonObject.getString("password");
                        tempUser = operateSQLite.select(account);
                        if(tempUser == null){
                            result = false;
                        }else {
                            result = tempUser.getPassword().equals(pwd);
                        }
                        type = ACCOUNT_INFO;
                        break;

//                    case MATCH_REQ:
//                        socketBooleanMap.put(socket, true);
//                        type = MATCH_REQ;
//                        break;
//
//                    case DIS_MATCH_REQ:
//                        socketBooleanMap.put(socket, false);
//                        type = DIS_MATCH_REQ;
//                        break;
//
//                    case MATCH_SCORE:
//                        scoreMsg = new ScoreMsg((String) jsonObject.get("account"),
//                                (Integer) jsonObject.get("score"));
//                        scoreMsg.setTargetSocket(socketSocketMap.get(socket));
//                        type = MATCH_SCORE;
//                        break;

                }
            }
        }

        // 通知发送线程启动
        public void notifySend(){
            // 发送线程启动
            new Thread(new serverSend(socket)).start();
        }
    }

    /**
     * 发送信息给客户端
     */
    class serverSend implements Runnable{
        private Socket socket;


        serverSend(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                System.out.println("发送线程已开启");
                PrintWriter writer = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),"utf-8")),true);
                JSONObject object = null;
                if(type != -1){
                    switch (type) {
                        case CREATE_ACCOUNT:
                            object = new JSONObject();
                            object.put("type", CREATE_ACCOUNT);
                            object.put("result", result);
                            writer.println(object.toString());
                            type = -1;
                            break;
                        case UPDATE_CREDIT:
                            object = new JSONObject();
                            object.put("type", UPDATE_CREDIT);
                            object.put("result", true);
                            writer.println(object.toString());
                            type = -1;
                            break;
                        case ACCOUNT_INFO:
                            object = new JSONObject();
                            object.put("type", ACCOUNT_INFO);
                            if(result){
                                object.put("account", tempUser.getAccount());
                                object.put("credits", tempUser.getCredits());
                            }
                            object.put("result", result);
                            writer.println(object.toString());
                            type = -1;
                            break;
//                        case MATCH_REQ:
//                            object = new JSONObject();
//                            int i = 0;
//                            for (Socket s :socketBooleanMap.keySet()){
//                                if(socketBooleanMap.get(s) && s != socket){
//                                    socketSocketMap.put(socket, s);
//                                    socketSocketMap.put(s, socket);
//                                    result = true;
//                                }
//                            }
//                            object.put("result", result);
//                            writer.println(object.toString());
//                            if(result) {
//                                type = MATCH_SCORE;
//                            }
//                            break;
//
//                        case DIS_MATCH_REQ:
//                            object = new JSONObject();
//                            object.put("result", false);
//                            writer.println(object.toString());
//                            type = -1;
//                            break;
//
//                        case MATCH_SCORE:
//                            object = new JSONObject();
//                            object.put("account", scoreMsg.getAccount());
//                            object.put("score", scoreMsg.getScore());
//                            PrintWriter writer2 = new PrintWriter(new BufferedWriter(
//                                    new OutputStreamWriter(
//                                            socketSocketMap.get(socket).getOutputStream(),"utf-8")),true);
//                            writer2.println(object);
//                            // 这里不改变type
//                            break;

                    }
                    System.out.println("send:" + object);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

