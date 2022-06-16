package Server;

import Client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Global.*;
import SelectChat.*;

public class Server {
    static ServerSocket serverSocket = null;
    static Socket socket = null;

    public static void main(String[] args) {
        ServerView serverView = new ServerView();
        try{
            // 文字进程专用serverSocket
            serverSocket = new ServerSocket(GlobalSettings.textPort);

            // 在服务器端对客户端开启文件传输的线程
            ServerFileThread serverFileThread = new ServerFileThread();
            serverFileThread.start();

            ServerImageThread serverImageThread = new ServerImageThread();
            serverImageThread.start();

            // 等待连接并开启相应线程
            while (true) {
                // 等待连接
                socket = serverSocket.accept();
                // 在服务器端对客户端开启相应的线程
                ServerReadAndPrint readAndPrint = new ServerReadAndPrint(socket, serverView);
                readAndPrint.start();
            }
            } catch (IOException e1){
            e1.printStackTrace();
        }
    }

    static public String getRealMsg(String line) {
        return line.substring(UserProtocol.PROTOCOL_LEN,line.length()- UserProtocol.PROTOCOL_LEN);
    }
}

/* 服务器端读写类线程 */
class  ServerReadAndPrint extends Thread{
    Socket nowSocket = null;
    ServerView serverView = null;
    BufferedReader in = null;
    PrintWriter out = null;

    // 所有连接着的端口
    public static UserMap<String, Socket> users = new UserMap<>();
    // 当前发送者的名称
    String curUser = null;
    // 状态：私发还是单发？
    boolean selectState = false;
    // 私发SocketList
    ArrayList<Socket> recvList ;
    // 私发名单
    ArrayList<String> nameList;

    // 构造函数
    public ServerReadAndPrint(Socket socket, ServerView serverView){
        this.nowSocket = socket;
        this.serverView = serverView;
    }

    // 改写：可选择性的发送
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));  // 输入流
            // 获取客户端信息并把信息发送给所有客户端
            while (true) {
                String str = in.readLine();
                String clientMsg = Server.getRealMsg(str);

                /**
                 * str是客户端与服务器端交流的信息
                 * */
                /** 1.登录信息 **/
                if(str.startsWith(UserProtocol.LOGIN_ROUND) && str.endsWith(UserProtocol.LOGIN_ROUND)){
                    curUser = Server.getRealMsg(str);
                    if(ServerReadAndPrint.users.map.containsKey(curUser)){
                        /** 用户名重复处理
                         *
                         */
                    }
                    else{
                        String loginMsg = "用户 " + curUser + " 登录成功!";
                        serverView.setTextArea(loginMsg);// 登录信息
                        ServerReadAndPrint.users.map.put(curUser, nowSocket);
                        // 群发
                        for(Socket socket : users.valueSet()){
                            out = new PrintWriter(socket.getOutputStream());  // 对每个客户端新建相应的socket套接字
                            if(socket == nowSocket) {  // 发送给当前客户端
                                out.println("(你)" + loginMsg);
                            }
                            else {  // 发送给其它客户端
                                out.println(loginMsg);
                            }
                            out.flush();  // 清空out中的缓存
                        }
                    }
                }
                /** 2.发送者信息,需不需要？ **/


                /** 3.接受者信息(群发？私发？) **/
                if(str.startsWith(UserProtocol.SELECT_ROUND) && str.endsWith(UserProtocol.SELECT_ROUND)){

                    recvList = SelectChat.getSelectList(clientMsg, users);
                    nameList = SelectChat.getNameList(clientMsg);
                    selectState = true;
                }

                /** 4. 发送的消息 **/
                if(str.startsWith(UserProtocol.MSG_ROUND) && str.endsWith(UserProtocol.MSG_ROUND)){
                    if(!selectState){
                        // 群发
                        clientMsg = curUser + " 对每个人说：" + clientMsg;


                        for(Socket socket : users.valueSet()){
                            out = new PrintWriter(socket.getOutputStream());  // 对每个客户端新建相应的socket套接字
                            if(socket == nowSocket) {  // 发送给当前客户端
//                                out.println("(你)" + clientMsg);
                            }
                            else {  // 发送给其它客户端
                                out.println(clientMsg);
                            }
                            out.flush();  // 清空out中的缓存
                        }
                    }
                    else{
                        // 私发
                        clientMsg = curUser + " 悄悄对你说说：" + clientMsg;
                        for(Socket socket : recvList){
                            out = new PrintWriter(socket.getOutputStream());  // 对每个客户端新建相应的socket套接字
                            // 发送给其它客户端
                                out.println(clientMsg);
                            }
                            out.flush();  // 清空out中的缓存
                        }
                    // 自发
                    out = new PrintWriter(nowSocket.getOutputStream());
                    clientMsg = "(你)" + clientMsg;
                    out.println(clientMsg);
                    out.flush();


                    // 系统消息
                    StringBuilder serverMsg = null;
                    if(selectState) {
                        serverMsg = new StringBuilder(curUser + "-> { ");
                        for (String recv : nameList) {
                            serverMsg.append(recv);
                            serverMsg.append(" ");
                        }
                        serverMsg.append("} :").append(clientMsg);
                        serverView.setTextArea(serverMsg.toString());
                    }
                    else{
                        serverMsg = new StringBuilder(curUser + "-> 全部人 ");
                        serverMsg.append("} :").append(clientMsg);
                        serverView.setTextArea(serverMsg.toString());
                    }
                    }
                }



        } catch (Exception e) {
            ServerReadAndPrint.users.map.remove(curUser);  // 线程关闭，移除相应套接字
        }
    }
}



