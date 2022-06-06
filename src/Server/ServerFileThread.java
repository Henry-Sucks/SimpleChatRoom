package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Global.*;
import SelectChat.*;

public class ServerFileThread extends Thread{
    ServerSocket server = null;
    Socket socket = null;
    public static UserMap<String, Socket> users = new UserMap<>();
    static ServerToClient messager = null;
    static String userName = null;

    public void run() {
        try {
            // 文件进程和文字进程并不一样!
            server = new ServerSocket(GlobalSettings.filePort);

            while(true) {
                socket = server.accept();
                messager = new ServerToClient(socket);
                /** 接受登录信息 **/
                String serverMsg = messager.getMsg();
                userName =serverMsg;
                System.out.println("文件登录：" + serverMsg);
                users.put(Server.getRealMsg(serverMsg), socket);

                // 开启文件传输线程
                FileReadAndWrite fileReadAndWrite = new FileReadAndWrite(socket, userName);
                fileReadAndWrite.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FileReadAndWrite extends Thread {
    private Socket nowSocket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    private ArrayList<Socket> recvList; // 私发SocketList
    private ArrayList<String> nameList; // 私发名单
    static boolean selectState = false; // 是否选择私发状态
    private String userName = null;

    public FileReadAndWrite(Socket socket, String userName) {
        this.nowSocket = socket;
        this.userName = userName;
    }
    public void run() {
        try {
            input = new DataInputStream(nowSocket.getInputStream());  // 输入流
            while (true) {
                /** 发送文件前固定收到一条信息，设置私发模式 **/
                ServerToClient messager = new ServerToClient(nowSocket);
                String clientMsg = messager.getMsg();
                clientMsg = Server.getRealMsg(clientMsg);

                System.out.println("开始发文件");
                System.out.println(clientMsg);
                recvList = SelectChat.getSelectList(clientMsg, ServerFileThread.users);
                nameList = SelectChat.getNameList(clientMsg);
                selectState = true;

                // 获取文件名字和文件长度
                String textName = input.readUTF();
                long textLength = input.readLong();
                // 发送文件名字和文件长度给所有客户端
                ArrayList<Socket> tempList;
                if(!selectState)
                    tempList = ServerFileThread.users.toArrayList();
                else
                    tempList = recvList;

//                clientMsg = ServerFileThread.userName + " 向您发了一个文件";
//                messager.sendMsg(clientMsg, tempList);


                for(Socket socket: tempList) {
                    output = new DataOutputStream(socket.getOutputStream());  // 输出流
                    if(socket != nowSocket) {  // 发送给其它客户端
                        output.writeUTF(textName);
                        output.flush();
                        output.writeLong(textLength);
                        output.flush();
                    }
                }



                // 发送文件内容
                int length = -1;
                long curLength = 0;
                byte[] buff = new byte[1024];
                while ((length = input.read(buff)) > 0) {
                    curLength += length;
                    for(Socket socket: tempList) {
                        output = new DataOutputStream(socket.getOutputStream());  // 输出流
                        if(socket != nowSocket) {  // 发送给其它客户端
                            output.write(buff, 0, length);
                            output.flush();
                        }
                    }
                    if(curLength == textLength) {  // 强制退出
                        System.out.println("发送完毕");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            ServerFileThread.users.map.remove(nowSocket);  // 线程关闭，移除相应套接字
        }
    }

}
