import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerView {
    JTextArea textArea;

    // 用于向文本区域添加信息
    void setTextArea(String str){
        textArea.append(str+'\n');
        textArea.setCaretPosition(textArea.getDocument().getLength());  // 设置滚动条在最下面
    }

    // 构造函数
    public ServerView() {init();}

    void init(){
        JFrame jf = new JFrame("服务器");
        jf.setBounds(500, 100, 450, 500);
        jf.setResizable(false);

        JPanel jp = new JPanel();  // 新建容器
        JLabel label = new JLabel("==您已进入多人聊天系统（服务器端）==");
        textArea = new JTextArea(23, 38);  // 新建文本区域并设置长宽
        textArea.setEditable(false);  // 设置为不可修改
        JScrollPane scroll = new JScrollPane(textArea);  // 设置滚动面板（装入textArea）
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);  // 显示垂直条
        jp.add(label);
        jp.add(scroll);

        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        jf.add(jp);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
