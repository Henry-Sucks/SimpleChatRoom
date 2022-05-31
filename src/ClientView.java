import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class ClientView {
    String userName;
    JTextArea textArea;
    JScrollPane scrollPane;
    JTextField textField;
    JButton sendBtn = new JButton("发送");
    JButton sendFileBtn = new JButton("文件发送");

    ClientReadAndPrint.ChatViewListen sendListener;
    // 构造函数
    public ClientView(String userName) {
        this.userName = userName;
        init();
    }



    // 初始化
    void init(){
        // 初始化用户窗口
        JFrame clientView = new JFrame(userName);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("用户：" + userName);
        textArea = new JTextArea("**登录成功，欢迎来到多人聊天室！****************\n",12, 35);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(label);
        panel.add(scrollPane);

        textField = new JTextField(20);
        panel.add(textField);

        panel.add(sendBtn);
        panel.add(sendFileBtn);
        clientView.add(panel, BorderLayout.SOUTH);
        clientView.setBounds(500, 200, 400, 330);

        // 设置监听成功后作出的事件
        sendListener = new ClientReadAndPrint.ChatViewListen();
        sendListener.setJTextArea(textArea);
        sendListener.setJTextField(textField);
        sendListener.setClientView(clientView);
        textField.addActionListener(sendListener);

        sendBtn.addActionListener(sendListener);
        sendFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileOpenSelector(clientView);
            }
        });

        // 将panel附在frame上
        clientView.add(panel);

        // 关闭
        clientView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        clientView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientView.setVisible(true);
    }

    void showFileOpenSelector(JFrame parent){
        // 创建一个默认的文件选择器
        JFileChooser fileChooser = new JFileChooser();
        // 设置默认显示的文件夹
        fileChooser.setCurrentDirectory(new File("."));
        // 设置默认使用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
        fileChooser.setFileFilter(new FileNameExtensionFilter("(txt)", "txt"));
        // 打开文件选择框(线程将被堵塞，直到选择框被关闭)
        int result = fileChooser.showOpenDialog(parent);
        // 点击确定
        if(result == JFileChooser.APPROVE_OPTION) {
            // 获取路径
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            ClientFileThread.outFileToServer(path);
        }
    }

}
