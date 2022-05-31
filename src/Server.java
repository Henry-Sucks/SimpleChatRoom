import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
        return line.substring(UserMapProtocol.PROTOCOL_LEN,line.length()-UserMapProtocol.PROTOCOL_LEN);
    }
}

/* 服务器端读写类线程 */
class  ServerReadAndPrint extends Thread{
    Socket nowSocket = null;
    ServerView serverView = null;
    BufferedReader in = null;

    // 所有连接着的端口
    public static UserMap<String, Socket> users = new UserMap<>();
    // 当前发送者的名称
    String curUser = null;
    // 状态：私发还是单发？
    boolean selectState = false;
    // 私发名单
    ArrayList<String> recvList = new ArrayList<>();



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

                /**
                 * str是客户端与服务器端交流的信息
                 * */
                /** 1.登录信息 **/
                if(str.startsWith(UserMapProtocol.LOGIN_ROUND) && str.endsWith(UserMapProtocol.LOGIN_ROUND)){
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
                        SelectChat.TextChat.toEveryone(loginMsg, nowSocket, users);
                    }
                }
                /** 2.发送者信息,需不需要？需要！写清名字 **/
                if(str.startsWith(UserMapProtocol.CURNAME_ROUND) && str.endsWith(UserMapProtocol.CURNAME_ROUND)){
                    curUser = Server.getRealMsg(str);
                }

                /** 3.接受者信息(群发？私发？) **/
                if(str.startsWith(UserMapProtocol.SELECT_ROUND) && str.endsWith(UserMapProtocol.SELECT_ROUND)){
                    String recvMsg = Server.getRealMsg(str);
                    System.out.println(recvMsg);
                    int recvNum = Integer.parseInt(recvMsg.split(UserMapProtocol.SPLIT_SIGN)[0]);


                    for(int i = 1; i <= recvNum; i++){
                        System.out.println(recvMsg);
                        System.out.println("用户名：" + recvMsg.split(UserMapProtocol.SPLIT_SIGN)[i]);
                        recvList.add(recvMsg.split(UserMapProtocol.SPLIT_SIGN)[i]);
                    }

                    selectState = true;
                }

                /** 4. 发送的消息 **/
                if(str.startsWith(UserMapProtocol.MSG_ROUND) && str.endsWith(UserMapProtocol.MSG_ROUND)){
                    String sendMsg = Server.getRealMsg(str);
                    if(!selectState){
                        serverView.setTextArea("群发中。。。");
                        SelectChat.TextChat.toEveryone(sendMsg, nowSocket, users);
                    }
                    else{
                        serverView.setTextArea("私发中。。。");
                        SelectChat.TextChat.toSelectOne(sendMsg, recvList, users);
                    }

                    // 系统消息
                    StringBuilder serverMsg = null;
                    serverMsg = new StringBuilder(curUser + "-> { ");
                    for (String recv : recvList){
                        serverMsg.append(recv);
                        serverMsg.append(" ");
                    }
                    serverMsg.append("} :").append(sendMsg);
                    serverView.setTextArea(serverMsg.toString());
                }


            }
        } catch (Exception e) {
            ServerReadAndPrint.users.map.remove(curUser);  // 线程关闭，移除相应套接字
        }
    }
}


