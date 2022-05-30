import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientFileThread extends Thread{
    private Socket socket = null;
    private JFrame clientView = null;
    static String userName = null;
    static PrintWriter output = null;  // 普通消息的发送（Server.java传来的值）
    static DataInputStream fileIn = null;
    static DataOutputStream fileOut = null;
    static DataInputStream fileReader = null;
    static DataOutputStream fileWriter = null;

    // 用于存储用户文件的目录
    static File userDir = null;

    public ClientFileThread(String userName, JFrame clientView, PrintWriter output){
        ClientFileThread.userName = userName;
        this.clientView = clientView;
        ClientFileThread.output = output;
    }

    // 客户端接受文件
    public void run(){
        try{
            // InetAddress IP地址
            InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
            socket = new Socket(addr, Client.port);  // 客户端套接字
            // 每个Thread独有一个Socket（是否可以理解为与服务器端口？）
            fileIn = new DataInputStream(socket.getInputStream());  // 输入流
            fileOut = new DataOutputStream(socket.getOutputStream());  // 输出流
            // 接收文件
            while(true) {
                String textName = fileIn.readUTF();
                long titleLength = fileIn.readLong();
                // 弹出窗口
                int result = JOptionPane.showConfirmDialog(clientView, "是否接受？", "提示",
                        JOptionPane.YES_NO_OPTION);
                int length = -1;

                // buff?缓冲区？->只能读取1024位
                byte[] buff = new byte[1024];
                long curLength = 0;

                // 提示框选择结果，0为确定，1位取消
                if(result == 0){
                    File userFile;
                    // 选择存放的文件夹地址
                    JFileChooser dirChooser = new JFileChooser();
                    dirChooser.setCurrentDirectory(new File("."));
                    dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    dirChooser.setAcceptAllFileFilterUsed(false);

                    // 用户自己选择路径+用户名
                    if(dirChooser.showOpenDialog(clientView) == JFileChooser.APPROVE_OPTION){
                        userFile = new File(dirChooser.getSelectedFile().getAbsolutePath() + "\\" + userName);
                        if(!userFile.exists())
                        userFile.mkdir();
                    }
                    // 默认路径
                    else {
                        userFile = new File("D:\\接受文件\\" + userName);
                        if(!userFile.exists())
                        userFile.mkdir();
                    }

                    // 保存下来
                    userDir = userFile;

                    File file = new File(userFile.getAbsolutePath() + "\\"+ textName);
                    fileWriter = new DataOutputStream(new FileOutputStream(file));
                    while((length = fileIn.read(buff)) > 0) {  // 把文件写进本地
                        fileWriter.write(buff, 0, length);
                        fileWriter.flush();
                        curLength += length;
						output.println("【接收进度:" + curLength/titleLength*100 + "%】");
						output.flush();
                        if(curLength == titleLength) {  // 强制结束
                            break;
                        }
                    }
                    output.println("【" + userName + "接收了文件！】");
                    output.flush();
                    // 提示文件存放地址
                    JOptionPane.showMessageDialog(clientView, "文件存放地址：\n" +
                            "D:\\接受文件\\" +
                            userName + "\\" + textName, "提示", JOptionPane.INFORMATION_MESSAGE);
                }

                else {  // 不接受文件
                    while((length = fileIn.read(buff)) > 0) {
                        curLength += length;
                        if(curLength == titleLength) {  // 强制结束
                            break;
                        }
                    }
                }
                fileWriter.close();
            }
        } catch (Exception e) {}
    }
    // 客户端发送文件
    static void outFileToServer(String path) {
        try {
            File file = new File(path);
            fileReader = new DataInputStream(new FileInputStream(file));
            fileOut.writeUTF(file.getName());  // 发送文件名字
            fileOut.flush();
            fileOut.writeLong(file.length());  // 发送文件长度
            fileOut.flush();
            int length = -1;
            byte[] buff = new byte[1024];
            while ((length = fileReader.read(buff)) > 0) {  // 发送内容
                fileOut.write(buff, 0, length);
                fileOut.flush();
            }

            output.println("【" + userName + "已成功发送文件！】");
            output.flush();
        } catch (Exception e) {}
    }
}
