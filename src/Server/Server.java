package Server;

import Global.UserMap;
import User.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    static ServerSocket serverSocket = null;
    static Socket socket = null;
    static List<Socket> list = new ArrayList<>();

    static UserMap<String, User> userHashMap = new UserMap<>();

    static HashMap<Socket, ArrayList<Socket>> chatPerson = new HashMap<>();


    public static void main(String[] args) {
        ServerView serverView = new ServerView();
        try{
            // 在服务器端对客户端开启文件传输的线程
            serverSocket = new ServerSocket(8090);
            ServerMessageThread serverMessageThread = new ServerMessageThread(userHashMap);
            serverMessageThread.start();

            ServerFileThread serverFileThread = new ServerFileThread();
            serverFileThread.start();

            ServerImageThread serverImageThread = new ServerImageThread();
            serverImageThread.start();


            // 等待连接并开启相应线程
            while (true) {
                // 等待连接
                socket = serverSocket.accept();
                // 添加当前客户端到列表
                list.add(socket);

                chatPerson.put(socket, new ArrayList<>());
                for (Socket socket: list){
                    if(chatPerson.get(socket).size() > 2 || list.size() <= 3 ||
                            chatPerson.get(socket).size() == 0){
                        chatPerson.get(socket).clear();
                        chatPerson.get(socket).addAll(list);
                    }
                }

                // 在服务器端对客户端开启相应的线程
                ServerReadAndPrint readAndPrint = new ServerReadAndPrint(socket, serverView);
                readAndPrint.start();
            }
        } catch (IOException e1){
            e1.printStackTrace();
        }
    }
}

/* 服务器端读写类线程 */
class ServerReadAndPrint extends Thread{
    Socket nowSocket = null;
    ServerView serverView = null;
    BufferedReader in = null;
    PrintWriter out = null;

    // 构造函数
    public ServerReadAndPrint(Socket socket, ServerView serverView){
        this.nowSocket = socket;
        this.serverView = serverView;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));  // 输入流
            // 获取客户端信息并把信息发送给所有客户端
            while (true) {
                String str = in.readLine();

                String regex = "%&%~\\d";//假设房间最多仅有6个人
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(str);
                ArrayList<Socket> outList = Server.chatPerson.get(nowSocket);
                if(matcher.find())
                {
                    int start = matcher.start();
                    int end = matcher.end();
                    String num = str.substring(start+4,end);
                    System.out.println(num);
                    int number = Integer.parseInt(num) - 1;
                    outList.clear();
                    System.out.println("num" + number);
                    if(number == 6){//与所有人通信
                        outList.addAll(Server.list);
                    }else{
                        outList.add(Server.list.get(number));//与指定人通信
                        outList.add(nowSocket);
                    }
                    continue; //跳过输出
                }

                System.out.println(outList.size());
                for(Socket socket: outList) {
                    out = new PrintWriter(socket.getOutputStream());  // 对每个客户端新建相应的socket套接字
                    if(socket != nowSocket) {  // 发送给其他客户端
                        out.println("2" + str); //1和2用来判断自己和别人发的消息
                        out.flush();  // 清空out中的缓存
                    }
                }
                // 发送给当前客户端
                out = new PrintWriter(nowSocket.getOutputStream());
                out.println("1" + str);
                System.out.println("1" + str);
                out.flush();
                // 调用自定义函数输出到图形界面
                serverView.setTextArea(str);
            }
        } catch (Exception e) {
            Server.list.remove(nowSocket);  // 线程关闭，移除相应套接字
        }
    }
}



