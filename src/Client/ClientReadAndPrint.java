package Client;

import Global.GlobalSettings;
import Global.UserProtocol;
import ui.ClientChatView;
import ui.OthersChatFrame;
import ui.SelfChatFrame;
import ui.WordFrame;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.WordsSplit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static MediaPlayer.MediaPlayerGlobal.sysSrc;

public class ClientReadAndPrint extends Thread{
    static Socket socket = null;
    static TextField textInput;
    static TextFlow textOutput;
    static Stage clientView;
    static BufferedReader input = null;
    static PrintWriter output = null;
    static ScrollPane sp;

    static String userName;

    // 用于接收从服务端发送来的消息
    public void run(){
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                // 获取服务端发送的信息
                String str = input.readLine();
                //这一部分暂时代替用户头像
                Image image = new Image(sysSrc+"\\"+"default-icon.png");
                ImageView iv = new ImageView();
                iv.setFitWidth(45);
                iv.setFitHeight(45);
                iv.setImage(image);
                //子线程改变主线程需要加这个选项 不太清楚为什么 不加会报错
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //更新JavaFX的主线程的代码放在此处
                        String outStr;
                        String sign = str.substring(0,1);
                        String[] temp;
                        String outName;
                        if (str.charAt(1) != '用'){
                            temp = str.split("说：",2);
                            outName = temp[0].substring(1);
                            outStr = temp[1];
                        }else{
                            String regex1 = "【";
                            String regex2 = "】";
                            Pattern pattern1 = Pattern.compile(regex1);
                            Pattern pattern2 = Pattern.compile(regex2);
                            Matcher matcher1 = pattern1.matcher(str);
                            Matcher matcher2 = pattern2.matcher(str);
                            matcher1.find();
                            matcher2.find();
                            int begin = matcher1.start();
                            int end = matcher2.start();
                            outName = str.substring(begin + 1, end);
                            outStr = str.substring(1);
                        }

                        if (sign.equals("1")){//将输出内容集合到一个textflow里
                            SelfChatFrame chat = new SelfChatFrame();
                            chat.getChildren().add(iv);
                            ArrayList<String> str = WordsSplit.splitWords(outStr);
                            WordFrame wd = new WordFrame(str);
                            wd.setStyle("-fx-background-color: rgba(0, 90, 0, 0.3); -fx-background-radius: 7;");
                            chat.getChildren().add(wd);
                            Text name = new Text(userName);
                            name.setFill(Paint.valueOf("#d0aba4"));
                            name.setOpacity(0.8);
                            name.setWrappingWidth(150);
                            Pane namePane = new Pane();
                            namePane.getChildren().add(name);
                            namePane.setPrefWidth(150);
                            namePane.setPrefHeight(20);
                            chat.getChildren().add(namePane);
                            textOutput.getChildren().add(chat);
                            textOutput.getChildren().add(new Text("\n"));

                        }else if(sign.equals("2")){
                            OthersChatFrame chat = new OthersChatFrame();
                            chat.getChildren().add(iv);
                            ArrayList<String> str = WordsSplit.splitWords(outStr);
                            WordFrame wd = new WordFrame(str);
                            wd.setStyle("-fx-background-color: rgba(50, 50, 50, 0.3); -fx-background-radius: 7;");
                            chat.getChildren().add(wd);
                            Text name = new Text(outName);
                            name.setFill(Paint.valueOf("#d0aba4"));
                            name.setOpacity(0.8);
                            name.setWrappingWidth(150);
                            Pane namePane = new Pane();
                            namePane.getChildren().add(name);
                            namePane.setPrefWidth(150);
                            namePane.setPrefHeight(20);
                            chat.getChildren().add(namePane);
                            textOutput.getChildren().add(chat);
                            textOutput.getChildren().add(new Text("\n"));
                            sp.setVvalue(1D);
                        }else {
                            textOutput.getChildren().add(new Text(str + "\n"));
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**聊天界面监听（内部类）**/
    public class ChatViewHandler implements EventHandler<ActionEvent> {
        public void setJTextField(TextField text) {
            textInput = text;  // 放在外部类，因为其它地方也要用到
        }
        public void setJTextArea(TextFlow textArea) {
            textOutput = textArea;  // 放在外部类，因为其它地方也要用到
        }
        public void setScrollPane(ScrollPane scrollPane){
            sp = scrollPane;
        }
        public void setClientView(Stage jFrame) {
            clientView = jFrame;  // 放在外部类，因为其它地方也要用到
            // 设置关闭聊天界面的监听
            clientView.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    if (Client.getOnline() == false){
                        System.exit(0);
                    }
                    else {
                        output.println("用户【" + userName + "】离开聊天室！");
                        output.flush();
                        System.exit(0);
                    }
                }
            });

        }
        // 监听执行函数
        @Override
        public void handle(ActionEvent event) {
            try {
                String str = textInput.getText();
                // 如果文本框内容为空
                if("".equals(str)) {
                    textInput.requestFocus();  // 设置焦点（可行）
                    // 弹出消息对话框（警告消息）
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("没有输入内容");
                    alert.setTitle("输入错误");
                    alert.setHeaderText("");
                    alert.show();
                    return;
                }
                output.println(userName + "说：" + str);  // 输出给服务端
                output.flush();  // 清空缓冲区out中的数据

                textInput.setText("");  // 清空文本框
                textInput.requestFocus();  // 设置焦点（可行）
            } catch (Exception ignored) {}
        }
    }

    /** 聊天界面直接发送信息 内部类 **/
    public class ChatViewDirectSend {
        public void DirectSend(String msg){
            output.println(msg);
            output.flush();
        }
    }

    /**登录监听 内部类**/
    public class LoginHandler implements EventHandler<ActionEvent>{
        TextField nameField;
        PasswordField pwdField;

        TextField emailField;
        Stage loginStage;  // 登录窗口本身

        boolean state; // true是注册状态, false是登录状态

        ClientChatView clientView = null;

        public void setTextField(TextField textField) {
            this.nameField = textField;
        }
        public void setPasswordField(PasswordField pwdField) {
            this.pwdField = pwdField;
        }
        public void setEmailField(TextField textField){this.emailField = textField;}
        public void setStage(Stage stage) {
            this.loginStage = stage;
        }

        public void setState(boolean state){this.state = state;}

        @Override
        public void handle(ActionEvent event) {
            ClientMessageThread clientMessageThread = new ClientMessageThread();
            clientMessageThread.start();
            // 等clientLoginThread 100ms
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            userName = nameField.getText();
            String userPwd = String.valueOf(pwdField.getText());  // getPassword方法获得char数组
            String userEmail = null;

            if(userName.equals("") || userPwd.equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误！");
                alert.setContentText("用户名或密码不能为空！");
                alert.show();
                return;
            }
            if(state)
                userEmail = String.valueOf(emailField.getText());
            int res;
            // 发送系统消息...注册
            if(state){
                String msg = UserProtocol.REGISTER_ROUND + userName + UserProtocol.SPLIT_SIGN + userPwd
                        + UserProtocol.SPLIT_SIGN + userEmail;
                clientMessageThread.sendRegisterMsg(msg);
                res = 0;
            }
            // 发送系统消息...登录
            else{
                String msg = UserProtocol.LOGIN_ROUND + userName + UserProtocol.SPLIT_SIGN + userPwd;
                System.out.println("消息:" + msg);
                res = clientMessageThread.sendLoginMsg(msg);

            }
            // res == 0 成功登录/注册
            if(res == 0) {
                System.out.println("成功登入");
                clientView = new ClientChatView();  // 新建聊天窗口,设置聊天窗口的用户名（静态）
                clientView.setUserName(userName);
                clientView.setClientMessageThread(clientMessageThread);
                clientView.run();


                // 建立和服务器的联系
                try {
                    InetAddress addr = InetAddress.getByName(null);// 获取主机地址
                    socket = new Socket(addr, GlobalSettings.textPort);  // 客户端套接字
                    loginStage.hide();// 隐藏登录窗口
                    output = new PrintWriter(socket.getOutputStream());  // 输出流
                    output.println("用户【" + userName + "】进入聊天室！");  // 发送用户名给服务器
                    output.flush();  // 清空缓冲区out中的数据
                    Client.setOnline(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 新建普通读写线程并启动
                ClientReadAndPrint readAndPrint = new ClientReadAndPrint();
                readAndPrint.start();
                // 新建文件读写线程并启动
                ClientFileThread fileThread = new ClientFileThread(userName, loginStage, output);
                fileThread.start();
                ClientImageThread imageThread = new ClientImageThread(userName, loginStage,clientView.getTextFlow());
                imageThread.start();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if(res == -1)
                    alert.setContentText("不存在该账号");
                else
                    alert.setContentText("密码错误");
                alert.setTitle("请重试！");
                alert.setHeaderText("");
                alert.show();
            }
        }
    }
}


