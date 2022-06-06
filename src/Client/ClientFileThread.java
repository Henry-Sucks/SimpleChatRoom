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

import SelectChat.*;
import Global.*;

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

    static ClientToServer messager = null; // 属于自己的与服务器沟通的工具

    // 用于存储用户文件的目录
    static File userDir = null;

    public ClientFileThread(String userName, Stage clientView, PrintWriter output){
        ClientFileThread.userName = userName;
        this.clientView = clientView;
        ClientFileThread.output = output;
    }

    // 客户端接受文件
    public void run(){
        try{
            InetAddress addr = InetAddress.getByName(null);
            socket = new Socket(addr, GlobalSettings.filePort);
            messager = new ClientToServer(socket);  // 设定工具
            String serverMsg = UserProtocol.LOGIN_ROUND + userName + UserProtocol.LOGIN_ROUND; // 发送登录信息
            messager.sendToServer(serverMsg);

            fileIn = new DataInputStream(socket.getInputStream());  // 输入流
            fileOut = new DataOutputStream(socket.getOutputStream());  // 输出流
            // 接收文件
            while(true) {
                // 收到提示信息
//                String otherName = Client.getRealMsg(messager.getMsg());
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
                        System.out.println("收到文件");
                        Alert ifReceive = new Alert(Alert.AlertType.CONFIRMATION);
                        ifReceive.setTitle("接收文件");
                        ifReceive.setHeaderText("是否接收用户" + "发来的文件？");
                        result = ifReceive.showAndWait();
                    }
                });
                while(result == null){
                }

                System.out.println("能够接收");
                int length = -1;

                // buff?缓冲区？->只能读取1024位
                byte[] buff = new byte[1024];
                long curLength = 0;

                // 提示框选择结果，0为确定，1位取消
                if(result.get() == ButtonType.OK){
                    System.out.println("允许接受");
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
                    }

                    if(dir != null){
                        userFile = new File(dir.getAbsolutePath());

                        if(!userFile.exists())
                        userFile.mkdirs();
                    }
                    // 默认路径
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
                        userFile.mkdirs();
                    }

                    System.out.println("已选好路径");
                    // 保存下来
                    userDir = userFile;

                    File file = new File(userFile.getAbsolutePath() + "\\"+ textName);
                    fileWriter = new DataOutputStream(new FileOutputStream(file));
                    while((length = fileIn.read(buff)) > 0) {  // 把文件写进本地
                        fileWriter.write(buff, 0, length);
                        fileWriter.flush();
                        curLength += length;

						// output.println("【接收进度:" + curLength/titleLength*100 + "%】");
                        // output.flush();
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
                            userFile.getAbsolutePath() + "\\" +
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
            /** 选择私发还是群发 **/
            /** 这里为了测试只写了私发的代码，三个用户1234,12345,123456，实现1234私发12345 **/
            String serverMsg = UserProtocol.SELECT_ROUND + "1" + UserProtocol.SPLIT_SIGN + "12345"  + UserProtocol.SELECT_ROUND;
            messager.sendToServer(serverMsg);


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

            /** 还有地方没解决：收发文件的提示消息还是和ServerReadAndPrint共用一个进程，也就是说想要提示信息私发而不改变
             * ServerReadAndPrint的逻辑，必须FileThread拥有自己的文字收发
             */
//            output.println(UserMapProtocol.MSG_ROUND + "【" + userName + "已成功发送文件！】" + UserMapProtocol.MSG_ROUND);
//            output.flush();
        } catch (Exception ignored) {}
    }

}
