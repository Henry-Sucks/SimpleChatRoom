import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class LoginView {
    JTextField textField = null;
    JPasswordField pwdField = null;
    ClientReadAndPrint.LoginListen listener = null;

    // 构造函数
    public LoginView(){
        init();
    }

    void init() {
        // 登录界面
        JFrame jf = new JFrame("登录");
        jf.setBounds(500, 250, 310, 210);
        jf.setResizable(false);

        // 表头
        JPanel jp1 = new JPanel();
        JLabel headLabel = new JLabel("登录界面");
        headLabel.setFont(new Font(null, 0, 35));
        jp1.add(headLabel);

        // 登录输入框
        JPanel jp2 = new JPanel();
        JLabel nameJLabel = new JLabel("用户名：");
        textField = new JTextField(20);
        JLabel pwdJLabel = new JLabel("密码：    ");
        pwdField = new JPasswordField(20);
        JButton loginBtn = new JButton("登录");
        JButton registerBtn = new JButton("注册");  // 没设置功能
        jp2.add(nameJLabel);
        jp2.add(textField);
        jp2.add(pwdJLabel);
        jp2.add(pwdField);
        jp2.add(loginBtn);
        jp2.add(registerBtn);

        JPanel jp = new JPanel(new BorderLayout());  // BorderLayout布局
        jp.add(jp1, BorderLayout.NORTH);
        jp.add(jp2, BorderLayout.CENTER);

        // 设置监控
        listener = new ClientReadAndPrint.LoginListen();
        listener.setJTextField(textField);
        listener.setJPasswordField(pwdField);
        listener.setJFrame(jf);
        pwdField.addActionListener(listener);
        loginBtn.addActionListener(listener);

        jf.add(jp);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}