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

public class ServerLoginThread extends Thread{
    ServerSocket serverSocket = null;
    Socket socket = null;

    BufferedReader in = null;

    PrintWriter out = null;

    // 调用它的server主类
    UserMap<String, User> userHashMap;

    public ServerLoginThread(UserMap<String, User> userHashMap){
        this.userHashMap = userHashMap;
    }

    public void run(){
        try{
            System.out.println("服务端初始化");
            serverSocket = new ServerSocket(GlobalSettings.loginPort);
            while (true){
                socket = serverSocket.accept();
                System.out.println("服务端收到套接字：" + socket);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                // 读取信息
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
                        System.out.println("服务端回复消息");
                    }
                    else{
                        if (!userPwd.equals(user.getPassword())){
                            // 错误码-2
                            out.println(-2);
                            out.flush();
                            System.out.println("服务端回复消息");
                        }
                        else{
                            out.println(0);
                            out.flush();
                            System.out.println("服务端回复消息");
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
