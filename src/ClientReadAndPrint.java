import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

class ClientReadAndPrint extends Thread{
    static Socket socket = null;

    // 需要引用的UI部分
    static JTextField textInput;
    static JTextArea textOutput;
    static JFrame clientView;

    static BufferedReader input = null;
    static PrintWriter output = null;

    static String userName;

    // 用于接收从服务端发送来的消息
    public void run(){
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                // 获取服务端发送的信息
                String str = input.readLine();
                // 添加进聊天客户端的文本区域
                textOutput.append(str + '\n');
                textOutput.setCaretPosition(textOutput.getDocument().getLength());
            }
        } catch (Exception e) {}
    }

    /**聊天界面监听（内部类）**/
    class ChatViewListen implements ActionListener {
        public void setJTextField(JTextField text) {
            textInput = text;  // 放在外部类，因为其它地方也要用到
        }
        public void setJTextArea(JTextArea textArea) {
            textOutput = textArea;  // 放在外部类，因为其它地方也要用到
        }
        public void setClientView(JFrame jFrame) {
            clientView = jFrame;  // 放在外部类，因为其它地方也要用到
            // 设置关闭聊天界面的监听
            clientView.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
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
        public void actionPerformed(ActionEvent event) {
            try {
                String str = textInput.getText();
                // 如果文本框内容为空
                if("".equals(str)) {
                    textInput.grabFocus();  // 设置焦点（可行）
                    // 弹出消息对话框（警告消息）
                    JOptionPane.showMessageDialog(clientView, "输入为空，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                output.println(userName + "说：" + str);  // 输出给服务端
                output.flush();  // 清空缓冲区out中的数据

                textInput.setText("");  // 清空文本框
                textInput.grabFocus();  // 设置焦点（可行）
            } catch (Exception ignored) {}
        }
    }

    /**登录监听 内部类**/
    class LoginListen implements ActionListener{
        JTextField textField;
        JPasswordField pwdField;
        JFrame loginJFrame;  // 登录窗口本身

        ClientView clientView = null;

        public void setJTextField(JTextField textField) {
            this.textField = textField;
        }
        public void setJPasswordField(JPasswordField pwdField) {
            this.pwdField = pwdField;
        }
        public void setJFrame(JFrame jFrame) {
            this.loginJFrame = jFrame;
        }
        public void actionPerformed(ActionEvent event) {
            userName = textField.getText();
            String userPwd = String.valueOf(pwdField.getPassword());  // getPassword方法获得char数组
            if(userName.length() >= 1 && userPwd.equals("123")) {  // 密码为123并且用户名长度大于等于1
                clientView = new ClientView(userName);  // 新建聊天窗口,设置聊天窗口的用户名（静态）
                // 建立和服务器的联系
                try {
                    InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
                    socket = new Socket(addr, Client.port);  // 客户端套接字
                    loginJFrame.setVisible(false);  // 隐藏登录窗口
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
                ClientFileThread fileThread = new ClientFileThread(userName, loginJFrame, output);
                fileThread.start();
            }
            else {
                JOptionPane.showMessageDialog(loginJFrame, "账号或密码错误，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
