package Client;

import Global.GlobalSettings;
import Global.UserProtocol;
import ui.FriendListPane;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientMessageThread extends Thread{
    private Socket socket;
    private PrintWriter output;

    BufferedReader in = null;

//    private User user;

    boolean init_state = true;

    private ObservableList<FriendListPane.Friend> listData;

    public void run(){
        try{
            InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
            socket = new Socket(addr, GlobalSettings.loginPort);  // 客户端套接字
            output = new PrintWriter(socket.getOutputStream());  // 输出流
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 不断接收登入登出信息
            while(true){
                System.out.println("循环");
                if(!init_state){
                    System.out.println("开始打印");
                    String str = in.readLine();
                    System.out.println(str);
                    // 如果有新登录
                    if(str.startsWith(UserProtocol.LOGIN_ROUND)){
                        String name = str.substring(2);
                        System.out.println("添加:" + name);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                listData.add(new FriendListPane.Friend(null, name, null, 0));
                            }
                        });
                    }
                    else if(str.startsWith(UserProtocol.LOGOUT_ROUND)){
                        String name = str.substring(2);
                        System.out.println("减少:" + name);
                        for(int i = 0; i < listData.size(); i++){
                            FriendListPane.Friend user = listData.get(i);
                            if(user.getName().equals(name)) {
                                int finalI = i;
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        listData.remove(finalI);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendRegisterMsg(String msg) {
        output.println(msg);
        output.flush();
    }

    public int sendLoginMsg(String msg){
        output.println(msg);
        output.flush();
        String str = "";
        try {
            // 收到服务器发送回来的信息
            str = in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Integer.parseInt(str);
    }

    public String getCurLogin(){
        // 不考虑线程冲突
        output.println(UserProtocol.CHECK_LOGIN_ROUND);
        output.flush();

        String str = "";
        try {
            // 收到服务器发送回来的信息
            str = in.readLine();
        } catch (Exception e){
            e.printStackTrace();
        }

        init_state = false;
        return str;
    }

    public void setListData(ObservableList<FriendListPane.Friend> listData){
        this.listData = listData;
    }
}