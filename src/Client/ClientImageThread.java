package Client;

import UI.OtherImageFrame;
import UI.SelfImageFrame;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;

public class ClientImageThread extends Thread{

    private int choose = 0; //判断是否选择了文件的存储路径
    private Socket socket = null;
    private Stage clientView = null;
    private File dir = null;
    private Optional<ButtonType> result = null;
    private TextFlow textOut;
    private String userName = null;
    static PrintWriter output = null;  // 普通消息的发送（Server.Server.java传来的值）
    static DataInputStream fileIn = null;
    static DataOutputStream fileOut = null;
    static DataInputStream fileReader = null;
    static DataOutputStream fileWriter = null;



    public ClientImageThread(String userName, Stage clientView,  TextFlow textOut){
        ClientFileThread.userName = userName;
        this.clientView = clientView;
        this.textOut = textOut;
    }

    // 客户端接受文件
    public void run(){
        try{
            InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
            socket = new Socket(addr, 9000);  // 客户端套接字
            fileIn = new DataInputStream(socket.getInputStream());  // 输入流
            fileOut = new DataOutputStream(socket.getOutputStream());  // 输出流
            // 接收文件
            while(true) {
                String userName = fileIn.readUTF();
                String textName = fileIn.readUTF();
                long titleLength = fileIn.readLong();
                int length = -1;
                byte[] buff = new byte[1024];
                long curLength = 0;
                File userFile = new File( "Source\\Room\\room0\\Image");
                if(!userFile.exists())
                    userFile.mkdirs();
                File file = new File(userFile.getAbsolutePath() + "\\" + textName);
                fileWriter = new DataOutputStream(new FileOutputStream(file));
                while((length = fileIn.read(buff)) > 0) {  // 把文件写进本地
                    fileWriter.write(buff, 0, length);
                    fileWriter.flush();
                    curLength += length;
                    //output.flush();
                    if(curLength == titleLength) {  // 强制结束
                        break;
                    }
                }
                fileWriter.close();
                Image image1 = new Image("source\\Background\\登录背景.jpeg");
                ImageView iv = new ImageView();
                iv.setFitWidth(50);
                iv.setFitHeight(50);
                iv.setImage(image1);
                System.out.println(textName);
                Image outImage = new Image("file:" + file.getAbsolutePath());
                ImageView out = new ImageView(outImage);
                out.setFitWidth(100);
                out.setFitHeight(100);
                OtherImageFrame chat = new OtherImageFrame();
                chat.getChildren().add(iv);
                chat.getChildren().add(out);
                Text name1 = new Text(userName);
                name1.setFill(Paint.valueOf("#d0aba4"));
                name1.setOpacity(0.8);
                name1.setWrappingWidth(150);
                Pane namePane = new Pane();
                namePane.getChildren().add(name1);
                namePane.setPrefWidth(150);
                namePane.setPrefHeight(20);
                chat.getChildren().add(namePane);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        textOut.getChildren().add(chat);
                        textOut.getChildren().add(new Text("\n"));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 客户端发送文件
    public static void outImageToServer(String path, TextFlow textOut, String userName) {
        try {
            String urlPath = "file:" + path;
            Image outImage = new Image(urlPath);
            Image image1 = new Image("source\\Background\\登录背景.jpeg");
            ImageView iv = new ImageView();
            iv.setFitWidth(50);
            iv.setFitHeight(50);
            iv.setImage(image1);
            ImageView out = new ImageView(outImage);
            out.setFitWidth(100);
            out.setFitHeight(100);
            SelfImageFrame chat = new SelfImageFrame();
            chat.getChildren().add(iv);
            chat.getChildren().add(out);
            Text name1 = new Text(userName);
            name1.setFill(Paint.valueOf("#d0aba4"));
            name1.setOpacity(0.8);
            name1.setWrappingWidth(150);
            Pane namePane = new Pane();
            namePane.getChildren().add(name1);
            namePane.setPrefWidth(150);
            namePane.setPrefHeight(20);
            chat.getChildren().add(namePane);
            textOut.getChildren().add(chat);
            textOut.getChildren().add(new Text("\n"));
            File file = new File(path);
            fileReader = new DataInputStream(new FileInputStream(file));
            fileOut.writeUTF(userName);
            fileOut.flush();
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

        } catch (Exception e) {}

    }

}
