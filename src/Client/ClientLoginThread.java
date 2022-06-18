package Client;

import Global.GlobalSettings;
import User.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientLoginThread extends Thread{
    private Socket socket;
    private PrintWriter output;

    BufferedReader in = null;

    private User user;

    public void run(){
        try{
            System.out.println("初始化");
            InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
            socket = new Socket(addr, GlobalSettings.loginPort);  // 客户端套接字
            System.out.println(socket);
            output = new PrintWriter(socket.getOutputStream());  // 输出流
            System.out.println(output);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendRegisterMsg(String msg) {
        output.println(msg);
        output.flush();
        System.out.println("客户端发出消息:" + msg);
    }

    public int sendLoginMsg(String msg){
        output.println(msg);
        output.flush();
        System.out.println("客户端发出消息:" + msg);
        String str = "";
        try {
            // 收到服务器发送回来的信息
            str = in.readLine();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Integer.parseInt(str);
    }
}
