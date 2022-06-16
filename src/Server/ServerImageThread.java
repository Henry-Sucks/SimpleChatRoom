package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerImageThread extends Thread{
    ServerSocket server = null;
    Socket socket = null;
    static List<Socket> list = new ArrayList<>();  // 存储客户端
    public void run() {
        try {
            server = new ServerSocket(9000);
            while(true) {
                socket = server.accept();
                list.add(socket);
                // 开启文件传输线程
                ImageReadAndWrite imageReadAndWrite = new ImageReadAndWrite(socket);
                imageReadAndWrite.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ImageReadAndWrite extends Thread {
    private Socket nowSocket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    public ImageReadAndWrite(Socket socket) {
        this.nowSocket = socket;
    }
    public void run() {
        try {

            input = new DataInputStream(nowSocket.getInputStream());  // 输入流
            while (true) {
                String userName = input.readUTF();
                String textName = input.readUTF();
                long textLength = input.readLong();
                // 发送文件长度给所有客户端
                for (Socket socket : ServerImageThread.list) {
                    output = new DataOutputStream(socket.getOutputStream());  // 输出流
                    if (socket != nowSocket) {  // 发送给其它客户端
                        output.writeUTF(userName);
                        output.flush();
                        output.writeUTF(textName);
                        output.flush();
                        output.writeLong(textLength);
                        output.flush();
                    }
                }
                int length = -1;
                long curLength = 0;
                byte[] buff = new byte[1024];
                while ((length = input.read(buff)) > 0) {
                    curLength += length;
                    for(Socket socket: ServerImageThread.list) {
                        output = new DataOutputStream(socket.getOutputStream());  // 输出流
                        if(socket != nowSocket) {  // 发送给其它客户端
                            output.write(buff, 0, length);
                            output.flush();
                        }
                    }
                    if(curLength == textLength) {  // 强制退出
                        break;
                    }

                }
            }
        } catch (Exception e) {
            ServerImageThread.list.remove(nowSocket);  // 线程关闭，移除相应套接字
        }
    }

}
