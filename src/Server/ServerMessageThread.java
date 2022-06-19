package Server;

import Global.GlobalSettings;
import Global.UserMap;
import Global.UserProtocol;
import User.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMessageThread extends Thread{
    ServerSocket serverSocket = null;
    Socket socket = null;

    BufferedReader in = null;

    PrintWriter out = null;

    // 调用它的server主类
    UserMap<String, User> userHashMap;

    // 保存socketList
    UserMap<String, Socket> userSocketMap = new UserMap<>();
    ArrayList<String> userNameList = new ArrayList<>();

    public ServerMessageThread(UserMap<String, User> userHashMap){
        this.userHashMap = userHashMap;
    }

    public void run(){
        try{
            serverSocket = new ServerSocket(GlobalSettings.loginPort);
            while (true){
                socket = serverSocket.accept();
                ServerMessageHandler serverMessageHandler = new ServerMessageHandler(socket);
                serverMessageHandler.start();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /** 内部类 **/
    class ServerMessageHandler extends Thread{
        private Socket nowSocket = null;
        private BufferedReader in = null;
        private PrintWriter out = null;

        private String userName;

        public ServerMessageHandler(Socket socket){
            nowSocket = socket;
        }

        public void run(){
            try{
                in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));
                out = new PrintWriter(nowSocket.getOutputStream());
                // 读取信息
                while(true){
                    String str = in.readLine();
                    System.out.println("服务端收到消息：" + str);
                    // 如果是注册
                    if(str.startsWith(UserProtocol.REGISTER_ROUND)){
                        // get到user的信息:用户名，密码，邮箱
                        int length = str.length();
                        str = str.substring(2, length);
                        String userName = str.split(UserProtocol.SPLIT_SIGN)[0];
                        String userPwd = str.split(UserProtocol.SPLIT_SIGN)[1];
                        String userEmail = str.split(UserProtocol.SPLIT_SIGN)[2];
                        if(userEmail == ""){
                            userEmail = null;
                        }
                        // 添加user
                        User user = new User(userName, userPwd, userEmail);
                        userHashMap.put(userName, user);
                        userSocketMap.put(userName, socket);
                        userNameList.add(userName);
                        this.userName = userName;

                        // 向所有人发出信息
                        String msg = UserProtocol.LOGIN_ROUND + userName;
                        sendMsgExceptSelf(msg, userName);
                    }
                    // 如果是登录
                    else if (str.startsWith(UserProtocol.LOGIN_ROUND)){
                        int length = str.length();
                        str = str.substring(2, length);
                        String userName = str.split(UserProtocol.SPLIT_SIGN)[0];
                        String userPwd = str.split(UserProtocol.SPLIT_SIGN)[1];
                        // 检查是否存在用户?
                        User user = userHashMap.getValueByKey(userName);
                        if(user == null){
                            // 错误码-1
                            out.println(-1);
                            out.flush();
                        }
                        else{
                            if (!userPwd.equals(user.getPassword())){
                                // 错误码-2
                                out.println(-2);
                                out.flush();
                            }
                            else{
                                out.println(0);
                                out.flush();

                                this.userName = userName;
                                userSocketMap.put(userName, socket);
                                userNameList.add(userName);
                                // 向所有人发出信息
                                String msg = UserProtocol.LOGIN_ROUND + userName;
                                sendMsgExceptSelf(msg, userName);
                            }
                        }
                    }
                    // 如果是查看当前登录情况
                    else if(str.startsWith(UserProtocol.CHECK_LOGIN_ROUND)){
                        out.println(getCurLogin(this.userName));
                        out.flush();
                    }

                }
            }catch (Exception e){
                // 登出
                userSocketMap.remove(userName);
                userListRemove(userName);
                // 向所有人发送登出消息
                String msg = UserProtocol.LOGOUT_ROUND + userName;
                try {
                    sendMsgExceptSelf(msg, userName);
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getCurLogin(String curName){
        StringBuilder msg = new StringBuilder();
        ArrayList<Socket> temp = userSocketMap.toArrayList();

        // 当前登录名单不包括自己
        // 是不是线程安全的呢？
        if(temp.size() >= 1) {
            msg.append(temp.size());
            for (Socket socket1 : temp) {
                String name = userSocketMap.getKeyByValue(socket1);
                msg.append(UserProtocol.SPLIT_SIGN).append(name);
            }
        }
        else{
            msg.append(0);
        }
        System.out.println("当前登录 "+msg);
        return msg.toString();
    }

    public void sendMsgExceptSelf(String msg, String userName) throws Exception{
        System.out.println(userName + "发送：" + msg);
        for(String user: userNameList){
            if(!user.equals(userName)){
                Socket socket1 = userSocketMap.getValueByKey(user);
                PrintWriter output = new PrintWriter(socket1.getOutputStream());
                output.println(msg);
                output.flush();
            }
        }
    }

    public void userListRemove(String userName){
        for(int i = 0; i < userNameList.size(); i++){
            if(userNameList.get(i).equals(userName))
                userNameList.remove(i);
        }
    }
}
