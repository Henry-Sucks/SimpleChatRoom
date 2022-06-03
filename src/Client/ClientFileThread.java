package Client;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;

public class ClientFileThread extends Thread{
    private int choose = 0; //判断是否选择了文件的存储路径
    private Socket socket = null;
    private Stage clientView = null;
    private File dir = null;
    private Optional<ButtonType> result = null;
    static String userName = null;
    static PrintWriter output = null;  // 普通消息的发送（Server.Server.java传来的值）
    static DataInputStream fileIn = null;
    static DataOutputStream fileOut = null;
    static DataInputStream fileReader = null;
    static DataOutputStream fileWriter = null;


    public ClientFileThread(String userName, Stage clientView, PrintWriter output){
        ClientFileThread.userName = userName;
        this.clientView = clientView;
        ClientFileThread.output = output;
    }

    // 客户端接受文件
    public void run(){
        try{
            // InetAddress IP地址
            InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
            socket = new Socket(addr, 9999);  // 客户端套接字
            // 每个Thread独有一个Socket（是否可以理解为与服务器端口？）
            fileIn = new DataInputStream(socket.getInputStream());  // 输入流
            fileOut = new DataOutputStream(socket.getOutputStream());  // 输出流
            // 接收文件
            while(true) {
                
                String textName = fileIn.readUTF();
                long titleLength = fileIn.readLong();
                // 弹出窗口
                /*
                Alert ifReceive = new Alert(Alert.AlertType.CONFIRMATION);
                ifReceive.setTitle("接收文件");
                ifReceive.setHeaderText("是否接收其他用户发来的文件");
                Optional<ButtonType> result = ifReceive.showAndWait();
                */
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert ifReceive = new Alert(Alert.AlertType.CONFIRMATION);
                        ifReceive.setTitle("接收文件");
                        ifReceive.setHeaderText("是否接收其他用户发来的文件");
                        result = ifReceive.showAndWait();
                    }
                });
                while(result == null){
                    System.out.println("result == null");
                }
                int length = -1;

                // buff?缓冲区？->只能读取1024位
                byte[] buff = new byte[1024];
                long curLength = 0;

                // 提示框选择结果，0为确定，1位取消
                if(result.get() == ButtonType.OK){
                    File userFile;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            DirectoryChooser chooser = new DirectoryChooser();
                            chooser.setInitialDirectory(new File("."));
                            dir = chooser.showDialog(clientView);
                            choose = 1;
                        }
                    });

                    //检测选择是否完成
                    while(choose == 0){
                        System.out.println("choose = 0");
                    }
                    /*
                    dirChooser.setCurrentDirectory(new File("."));
                    dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    dirChooser.setAcceptAllFileFilterUsed(false);
                     */

                    if(dir != null){
                        userFile = new File(dir.getAbsolutePath());
                        if(!userFile.exists())
                        userFile.mkdir();
                    }
                    else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                alert1.setTitle("自动选择路径");
                                alert1.setHeaderText("你还没有选择文件存放路径");
                                alert1.setContentText("文件将存放在默认存放地址" + "D:\\接受文件\\" + userName);
                                alert1.show();
                            }
                        });
                        userFile = new File("D:\\接受文件\\" + userName);
                        if(!userFile.exists())
                        userFile.mkdir();
                    }

                    File file = new File(userFile.getAbsolutePath() + "\\"+ textName);
                    fileWriter = new DataOutputStream(new FileOutputStream(file));
                    while((length = fileIn.read(buff)) > 0) {  // 把文件写进本地
                        fileWriter.write(buff, 0, length);
                        fileWriter.flush();
                        curLength += length;
						//output.println("【接收进度:" + curLength/titleLength*100 + "%】");
						output.flush();
                        if(curLength == titleLength) {  // 强制结束
                            break;
                        }
                    }
                    output.println("【" + userName + "接收了文件！】");
                    output.flush();
                    // 提示文件存放地址

                    choose = 0;//重置choose 下一次传输文件会用上
                    result = null;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setTitle("文件接收成功");
                            alert1.setHeaderText("成功接收到了其他用户发送来的文件");
                            alert1.setContentText("文件存放地址" + file.getAbsolutePath());
                            alert1.showAndWait();
                        }
                    });

                    /*
                    JOptionPane.showMessageDialog(clientView, "文件存放地址：\n" +
                            "D:\\接受文件\\" +
                            userName + "\\" + textName, "提示", JOptionPane.INFORMATION_MESSAGE);

                     */
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 客户端发送文件
    public static void outFileToServer(String path) {
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
