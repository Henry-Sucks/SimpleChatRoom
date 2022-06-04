package Client;

import UI.ClientChatView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import Global.*;

/** 客户端主逻辑：实现登录以及文字的发送与接收 **/

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

                //子线程改变主线程需要加这个选项 不太清楚为什么 不加会报错
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //更新JavaFX的主线程的代码放在此处
                        textOutput.getChildren().add(new Text(str + "\n"));
                    }
                });
                if (sp != null){
                    sp.setVvalue(1D);
                }

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
                    if (!Client.getOnline()){
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
                String serverMsg = "";
                /** 先告诉系统自己叫什么名字 **/
//                serverMsg = UserMapProtocol.CURNAME_ROUND + userName + UserMapProtocol.CURNAME_ROUND;
//                output.println(serverMsg);  // 输出给服务端
//                output.flush();  // 清空缓冲区out中的数据

                /** 用户输入 **/
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

                /** 将用户输入转为系统信息 **/
                /** 1.要求私聊 **/
                if(str.startsWith("send:")){
                    str = str.substring(5);
                    int userNum = Integer.parseInt(str.split(":")[0]);
                    serverMsg = UserProtocol.SELECT_ROUND;
                    serverMsg += userNum;
                    for(int i = 1; i <= userNum; i++){
                        serverMsg += UserProtocol.SPLIT_SIGN;
                        serverMsg += str.split(":")[i];
                    }
                    serverMsg += UserProtocol.SELECT_ROUND;
                }
                /** 2.文本信息 **/
                else
                    serverMsg = UserProtocol.MSG_ROUND + str + UserProtocol.MSG_ROUND;

                System.out.println(serverMsg);
                output.println(serverMsg);  // 输出给服务端
                output.flush();  // 清空缓冲区out中的数据

                textInput.setText("");  // 清空文本框
                textInput.requestFocus();  // 设置焦点（可行）
            } catch (Exception ignored) {}
        }
    }

    /**登录监听 内部类**/
   public class LoginHandler implements EventHandler<ActionEvent>{
        TextField textField;
        PasswordField pwdField;
        Stage loginStage;  // 登录窗口本身

        ClientChatView clientView = null;

        public void setTextField(TextField textField) {
            this.textField = textField;
        }
        public void setPasswordField(PasswordField pwdField) {
            this.pwdField = pwdField;
        }
        public void setStage(Stage stage) {
            this.loginStage = stage;
        }

        @Override
        public void handle(ActionEvent event) {
            userName = textField.getText();
            String userPwd = String.valueOf(pwdField.getText());  // getPassword方法获得char数组
            if(userName.length() >= 1 && userPwd.equals("123")) {  // 密码为123并且用户名长度大于等于1
                clientView = new ClientChatView();  // 新建聊天窗口,设置聊天窗口的用户名（静态）
                clientView.setUserName(userName);
                clientView.run();
                // 建立和服务器的联系
                try {
                    InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
                    /** 新建用于文字传输的Socket **/
                    socket = new Socket(addr, GlobalSettings.textPort);  // 客户端套接字
                    loginStage.hide();  // 隐藏登录窗口

                    output = new PrintWriter(socket.getOutputStream());  // 输出流
                    /** 将登陆信息传给服务器 **/
                    output.println(UserProtocol.LOGIN_ROUND + userName + UserProtocol.LOGIN_ROUND);
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
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("账号或者密码错误");
                alert.setTitle("输入错误");
                alert.setHeaderText("");
                alert.show();
            }
        }
    }
}



